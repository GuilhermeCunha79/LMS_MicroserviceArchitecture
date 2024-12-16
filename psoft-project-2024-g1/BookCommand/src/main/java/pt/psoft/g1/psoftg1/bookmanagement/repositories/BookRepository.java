package pt.psoft.g1.psoftg1.bookmanagement.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.bookmanagement.services.BookCountDTO;
import pt.psoft.g1.psoftg1.bookmanagement.services.SearchBooksQuery;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


/**
 *
 */
public interface BookRepository {
    Book save(Book book);
    void delete(Book book);
    Optional<Book> findByIsbn(@Param("isbn") String isbn);
}
