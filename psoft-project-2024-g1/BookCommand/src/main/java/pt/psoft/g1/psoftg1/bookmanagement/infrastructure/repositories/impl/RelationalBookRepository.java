package pt.psoft.g1.psoftg1.bookmanagement.infrastructure.repositories.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.bookmanagement.repositories.BookRepository;
import pt.psoft.g1.psoftg1.bookmanagement.services.SearchBooksQuery;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@Profile("relational")
@RequiredArgsConstructor
public class RelationalBookRepository implements BookRepository {

    @PersistenceContext
    private final EntityManager entityManager;

    @Transactional
    @Override
    public Book save(Book book) {
        if (entityManager.contains(book)) {
            book = entityManager.merge(book);
        } else {
            entityManager.persist(book);
        }
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
    @Query("SELECT b FROM Book b WHERE b.isbn.isbn = :isbn")
    public Optional<Book> findByIsbn(@Param("isbn") String isbn) {

        return entityManager.createQuery("SELECT b FROM Book b WHERE b.isbn.isbn = :isbn", Book.class)
                .setParameter("isbn", isbn)
                .getResultList()
                .stream()
                .findFirst();
    }
}
