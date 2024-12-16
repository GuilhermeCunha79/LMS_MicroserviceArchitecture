package pt.psoft.g1.psoftg1.bookmanagement.services;


import pt.psoft.g1.psoftg1.bookmanagement.api.BookViewAMQP;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.shared.services.Page;

import java.util.List;

/**
 *
 */
public interface BookService {
    Book create(CreateBookRequest request, String isbn);
    Book create(BookViewAMQP bookViewAMQP); // AMQP request
    Book save(Book book);
    Book findByIsbn(String isbn);
    Book update(UpdateBookRequest request, Long currentVersion);
    Book update(BookViewAMQP bookViewAMQP);
    List<Book> findByGenre(String genre);
    List<Book> findByTitle(String title);
    List<Book> findByAuthorName(String authorName);
    Book removeBookPhoto(String isbn, long desiredVersion);
    List<Book> searchBooks(Page page, SearchBooksQuery query);
}
