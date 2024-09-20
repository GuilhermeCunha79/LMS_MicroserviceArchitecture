package pt.psoft.g1.psoftg1.bookmanagement.infrastructure.repositories.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.util.StringUtils;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.bookmanagement.services.BookCountDTO;
import pt.psoft.g1.psoftg1.bookmanagement.model.Isbn;
import pt.psoft.g1.psoftg1.bookmanagement.repositories.BookRepository;
import pt.psoft.g1.psoftg1.bookmanagement.services.SearchBooksQuery;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface SpringDataBookRepository  extends BookRepository, BookRepoCustom, CrudRepository<Book, Isbn> {

    @Query("SELECT b " +
            "FROM Book b " +
            "WHERE b.isbn.isbn = :isbn")
    Optional<Book> findByIsbn(@Param("isbn") String isbn);

    @Override
    @Query("SELECT new pt.psoft.g1.psoftg1.bookmanagement.services.BookCountDTO(b, COUNT(l)) " +
                "FROM Book b " +
                "JOIN Lending l ON l.book = b " +
                "WHERE l.startDate > :oneYearAgo " +
                "GROUP BY b " +
                "ORDER BY COUNT(l) DESC")
    Page<BookCountDTO> findTop5BooksLent(@Param("oneYearAgo") LocalDate oneYearAgo, Pageable pageable);


    @Override
    @Query("SELECT b " +
            "FROM Book b " +
            "WHERE b.genre.genre LIKE %:genre%")
    List<Book> findByGenre(@Param("genre") String genre);

    @Override
    @Query("SELECT b FROM Book b WHERE b.title.title LIKE %:title%")
    List<Book> findByTitle(@Param("title") String title);

    @Override
    @Query(value =
            "SELECT b.* " +
                    "FROM Book b " +
                    "JOIN BOOK_AUTHORS on b.pk = BOOK_AUTHORS.BOOK_PK " +
                    "JOIN AUTHOR a on BOOK_AUTHORS.AUTHORS_AUTHOR_NUMBER = a.AUTHOR_NUMBER " +
                    "WHERE a.NAME LIKE :authorName"
            , nativeQuery = true)
    List<Book> findByAuthorName(@Param("authorName") String authorName);

    @Override
    @Query(value =
            "SELECT b.* " +
            "FROM Book b " +
            "JOIN BOOK_AUTHORS on b.pk = BOOK_AUTHORS.BOOK_PK " +
            "JOIN AUTHOR a on BOOK_AUTHORS.AUTHORS_AUTHOR_NUMBER = a.AUTHOR_NUMBER " +
            "WHERE a.AUTHOR_NUMBER = :authorNumber "
            , nativeQuery = true)
    List<Book> findBooksByAuthorNumber(Long authorNumber);

}


interface BookRepoCustom {
    List<Book> searchBooks(pt.psoft.g1.psoftg1.shared.services.Page page, SearchBooksQuery query);

}

@RequiredArgsConstructor
class BookRepoCustomImpl implements BookRepoCustom {
    // get the underlying JPA Entity Manager via spring thru constructor dependency
    // injection
    private final EntityManager em;

    @Override
    public List<Book> searchBooks(pt.psoft.g1.psoftg1.shared.services.Page page, SearchBooksQuery query)
    {
        String title = query.getTitle();
        String genre = query.getGenre();
        String authorName = query.getAuthorName();

        final CriteriaBuilder cb = em.getCriteriaBuilder();
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

        final TypedQuery<Book> q = em.createQuery(cq);
        q.setFirstResult((page.getNumber() - 1) * page.getLimit());
        q.setMaxResults(page.getLimit());

        return q.getResultList();
    }
}
