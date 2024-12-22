package pt.psoft.g1.psoftg1.bookmanagement.services;

import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.shared.api.BookViewAMQP;
import pt.psoft.g1.psoftg1.shared.api.LendingViewAMQP;


import java.util.List;

/**
 *
 */
public interface BookService {
    Book create(CreateBookRequest request, String isbn);
    Book create(BookViewAMQP bookViewAMQP); // AMQP request
    Book removeBookPhoto(String isbn, long desiredVersion);

    Book findByIsbn(String isbn);
    Book update(UpdateBookRequest request, Long currentVersion);
    Book update(BookViewAMQP bookViewAMQP);
    boolean verifyIfIsbnExists(LendingViewAMQP lendingViewAMQP);
}
