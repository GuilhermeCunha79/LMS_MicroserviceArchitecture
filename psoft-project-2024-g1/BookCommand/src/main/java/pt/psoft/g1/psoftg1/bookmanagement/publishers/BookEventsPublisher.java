package pt.psoft.g1.psoftg1.bookmanagement.publishers;

import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.shared.api.BookViewAMQP;
import pt.psoft.g1.psoftg1.shared.api.LendingViewAMQP;

public interface BookEventsPublisher {

    BookViewAMQP sendBookCreated(Book book);

    BookViewAMQP sendBookUpdated(Book book, Long currentVersion);

    BookViewAMQP sendBookDeleted(Book book, Long currentVersion);

    LendingViewAMQP sendBookValidated(LendingViewAMQP lendingViewAMQP);
}
