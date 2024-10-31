package pt.psoft.g1.psoftg1.bookmanagement.infrastructure.repositories.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.bookmanagement.services.BookCountDTO;
import pt.psoft.g1.psoftg1.bookmanagement.repositories.BookRepository;
import pt.psoft.g1.psoftg1.bookmanagement.services.SearchBooksQuery;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component("bookJpa")
public class RelationalBookRepository implements BookRepository {

    @Autowired
    private EntityManager entityManager;

    public RelationalBookRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @Query("SELECT b FROM Book b WHERE b.isbn.isbn = :isbn")
    public Optional<Book> findByIsbn(@Param("isbn") String isbn) {

        return entityManager.createQuery("SELECT b FROM Book b WHERE b.isbn.isbn = :isbn", Book.class)
                .setParameter("isbn", isbn)
                .getResultList()
                .stream()
                .findFirst();
    }

    @Override
    @Query("SELECT b FROM Book b WHERE b.genre.genre LIKE %:genre%")
    public List<Book> findXBooksByGenre(@Param("genre") String genre, @Param("x") int x) {
        return entityManager.createQuery("SELECT b FROM Book b WHERE b.genre.genre LIKE %:genre%", Book.class)
                .setParameter("genre", genre)
                .setMaxResults(x)
                .getResultList();
    }
    @Override
    public List<Book> findTopXBooksFromMostLentGenreByReader(String readerNumber, @Param("x") int x) {
        return entityManager.createNativeQuery(
                        "SELECT b.* " +
                                "FROM Book b " +
                                "WHERE b.GENRE_PK = ( " +
                                "    SELECT g.PK " +
                                "    FROM Lending la " +
                                "    JOIN Book b2 ON la.BOOK_PK = b2.PK " +
                                "    JOIN Genre g ON b2.GENRE_PK = g.PK " +
                                "    WHERE la.READER_DETAILS_PK = (SELECT rd.PK FROM READER_DETAILS rd WHERE rd.READER_NUMBER = :readerId) " +
                                "    GROUP BY g.PK " +
                                "    ORDER BY COUNT(la.PK) DESC " +
                                "    LIMIT 1 " +
                                ") " +
                                "ORDER BY ( " +
                                "    SELECT COUNT(l2.PK) " +
                                "    FROM Lending l2 " +
                                "    WHERE l2.BOOK_PK = b.PK " +
                                ") DESC",
                        Book.class)
                .setParameter("readerId", readerNumber)
                .setMaxResults(x)
                .getResultList();
    }

    @Override
    public Page<BookCountDTO> findTop5BooksLent(LocalDate oneYearAgo, Pageable pageable) {
        String jpql = "SELECT new pt.psoft.g1.psoftg1.bookmanagement.services.BookCountDTO(b, COUNT(l)) " +
                "FROM Book b JOIN Lending l ON l.book = b " +
                "WHERE l.startDate > :oneYearAgo " +
                "GROUP BY b ORDER BY COUNT(l) DESC";

        TypedQuery<BookCountDTO> query = entityManager.createQuery(jpql, BookCountDTO.class)
                .setParameter("oneYearAgo", oneYearAgo);

        int totalRows = query.getResultList().size(); // Obter total de linhas
        List<BookCountDTO> results = query.setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        return new PageImpl<>(results, pageable, totalRows);
    }


    @Override
    @Query("SELECT b FROM Book b WHERE b.genre.genre LIKE %:genre%")
    public List<Book> findByGenre(@Param("genre") String genre) {
        return entityManager.createQuery("SELECT b FROM Book b WHERE b.genre.genre LIKE %:genre%", Book.class)
                .setParameter("genre", genre)
                .getResultList();
    }

    @Override
    @Transactional
    public Book save(Book book) {
        entityManager.persist(book);
        return book;
    }

    @Override
    public void delete(Book book) {
        if (entityManager.contains(book)) {
            entityManager.remove(book);
        } else {
            Book managedBook = entityManager.find(Book.class, book.getIsbn());
            if (managedBook != null) {
                entityManager.remove(managedBook);
            }
        }
    }

    @Override
    public List<Book> findXBooksByYGenre(@Param("x") int x, @Param("y") int y) {
        return entityManager.createNativeQuery("""
                            SELECT b.*
                                                    FROM (
                                                             SELECT
                                                                 b.*,
                                                                 ROW_NUMBER() OVER (PARTITION BY b.GENRE_PK ORDER BY b.TITLE) AS rn
                                                             FROM Book b
                                                                      JOIN Genre g ON b.GENRE_PK = g.PK
                                                             WHERE g.PK IN (
                                                                 SELECT g.PK
                                                                 FROM Genre g
                                                                          JOIN Book b ON b.GENRE_PK = g.PK
                                                                 GROUP BY g.PK
                                                                 ORDER BY COUNT(b.PK) DESC
                                                                 LIMIT :y
                                                                 )
                                                         ) AS b
                                                    WHERE rn <= :x
                                                    ORDER BY b.GENRE_PK, b.TITLE;
                        """, Book.class)
                .setParameter("x", x)
                .setParameter("y", y)
                .getResultList();
    }


    @Override
    @Query("SELECT b FROM Book b WHERE b.title.title LIKE %:title%")
    public List<Book> findByTitle(@Param("title") String title) {
        return entityManager.createQuery("SELECT b FROM Book b WHERE b.title.title LIKE %:title%", Book.class)
                .setParameter("title", title)
                .getResultList();
    }

    @Override
    @Query(value = """
            SELECT b.* 
            FROM Book b 
            JOIN BOOK_AUTHORS on b.pk = BOOK_AUTHORS.BOOK_PK 
            JOIN AUTHOR a on BOOK_AUTHORS.AUTHORS_AUTHOR_NUMBER = a.AUTHOR_NUMBER 
            WHERE a.NAME LIKE %:authorName%
            """, nativeQuery = true)
    public List<Book> findByAuthorName(@Param("authorName") String authorName) {
        // O parâmetro authorName é passado corretamente pela anotação @Query.
        return entityManager.createNativeQuery("""
                        SELECT b.* 
                        FROM Book b 
                        JOIN BOOK_AUTHORS on b.pk = BOOK_AUTHORS.BOOK_PK 
                        JOIN AUTHOR a on BOOK_AUTHORS.AUTHORS_AUTHOR_NUMBER = a.AUTHOR_NUMBER 
                        WHERE a.NAME LIKE %:authorName%
                        """, Book.class)
                .setParameter("authorName", authorName)
                .getResultList();
    }


    @Override
    @Query(value = """
            SELECT b.* 
            FROM Book b 
            JOIN BOOK_AUTHORS on b.pk = BOOK_AUTHORS.BOOK_PK 
            JOIN AUTHOR a on BOOK_AUTHORS.AUTHORS_AUTHOR_NUMBER = a.AUTHOR_NUMBER 
            WHERE a.AUTHOR_NUMBER = :authorNumber 
            """, nativeQuery = true)
    public List<Book> findBooksByAuthorNumber(@Param("authorNumber") Long authorNumber) {
        return findBooksByAuthorNumber(authorNumber);
    }


    @Override
    public List<Book> searchBooks(pt.psoft.g1.psoftg1.shared.services.Page page, SearchBooksQuery query) {
        String title = query.getTitle();
        String genre = query.getGenre();
        String authorName = query.getAuthorName();

        final CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        final CriteriaQuery<Book> cq = cb.createQuery(Book.class);
        final Root<Book> root = cq.from(Book.class);
        final Join<Book, Genre> genreJoin = root.join("genre");
        final Join<Book, Author> authorJoin = root.join("authors");
        cq.select(root);

        final List<Predicate> where = new ArrayList<>();

        if (StringUtils.hasText(title))
            where.add(cb.like(root.get("title").get("title"), title + "%"));

        if (StringUtils.hasText(genre))
            where.add(cb.like(genreJoin.get("genre"), genre + "%"));

        if (StringUtils.hasText(authorName))
            where.add(cb.like(authorJoin.get("name").get("name"), authorName + "%"));

        cq.where(where.toArray(new Predicate[0]));
        cq.orderBy(cb.asc(root.get("title"))); // Order by title, alphabetically

        final TypedQuery<Book> q = entityManager.createQuery(cq);
        q.setFirstResult((page.getNumber() - 1) * page.getLimit());
        q.setMaxResults(page.getLimit());

        return q.getResultList();
    }
}
