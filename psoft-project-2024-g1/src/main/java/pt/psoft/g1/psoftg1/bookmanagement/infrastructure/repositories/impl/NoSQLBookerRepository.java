package pt.psoft.g1.psoftg1.bookmanagement.infrastructure.repositories.impl;

import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.bookmanagement.repositories.BookRepository;
import pt.psoft.g1.psoftg1.bookmanagement.services.BookCountDTO;
import pt.psoft.g1.psoftg1.bookmanagement.services.SearchBooksQuery;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component("bookMongo")
@Primary
public class NoSQLBookerRepository implements BookRepository {
    @Override
    public List<Book> findByGenre(String genre) {
        return null;
    }

    @Override
    public List<Book> findXBooksByYGenre(int x, int y) {
        return null;
    }

    @Override
    public List<Book> findByTitle(String title) {
        return null;
    }

    @Override
    public List<Book> findByAuthorName(String authorName) {
        return null;
    }

    @Override
    public Optional<Book> findByIsbn(String isbn) {
        return Optional.empty();
    }

    @Override
    public Page<BookCountDTO> findTop5BooksLent(LocalDate oneYearAgo, Pageable pageable) {
        return null;
    }

    @Override
    public List<Book> findBooksByAuthorNumber(Long authorNumber) {
        return null;
    }

    @Override
    public List<Book> searchBooks(pt.psoft.g1.psoftg1.shared.services.Page page, SearchBooksQuery query) {
        return null;
    }

    @Override
    public Book save(Book book) {
        return null;
    }

    @Override
    public void delete(Book book) {

    }
}
