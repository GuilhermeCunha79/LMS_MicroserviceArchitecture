package pt.psoft.g1.psoftg1.bookmanagement.infrastructure.publishers.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pt.psoft.g1.psoftg1.bookmanagement.api.BookViewAMQPMapper;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.bookmanagement.publishers.BookEventsPublisher;
import pt.psoft.g1.psoftg1.shared.api.BookViewAMQP;
import pt.psoft.g1.psoftg1.shared.api.LendingViewAMQP;
import pt.psoft.g1.psoftg1.shared.model.BookEvents;

@Service
@RequiredArgsConstructor
public class BookEventsRabbitmqPublisherImpl implements BookEventsPublisher {

    @Autowired
    private RabbitTemplate template;
    @Autowired
    private DirectExchange direct;

    @Autowired
    private DirectExchange direct1;  // Exchange de empréstimos (LMS.lending)
    private final BookViewAMQPMapper bookViewAMQPMapper;

    private int count = 0;

    @Override
    public BookViewAMQP sendBookCreated(Book book) {
        return sendBookEvent(book, book.getVersion(), BookEvents.BOOK_CREATED);
    }

    @Override
    public BookViewAMQP sendBookUpdated(Book book, Long currentVersion) {
        return sendBookEvent(book, currentVersion, BookEvents.BOOK_UPDATED);
    }

    @Override
    public BookViewAMQP sendBookDeleted(Book book, Long currentVersion) {
        return sendBookEvent(book, currentVersion, BookEvents.BOOK_DELETED);
    }

    @Override
    public LendingViewAMQP sendBookValidated(LendingViewAMQP lendingViewAMQP) {
        return sendLendingEvent(lendingViewAMQP, BookEvents.BOOK_VALIDATED);
    }

    private BookViewAMQP sendBookEvent(Book book, Long currentVersion, String bookEventType) {

        try {
            ObjectMapper objectMapper = new ObjectMapper();

            BookViewAMQP bookViewAMQP = bookViewAMQPMapper.toBookViewAMQP(book);
            bookViewAMQP.setVersion(currentVersion);

            String jsonString = objectMapper.writeValueAsString(bookViewAMQP);

            this.template.convertAndSend(direct.getName(), bookEventType, jsonString);

            System.out.println(" [x] Sent '" + jsonString + "'");

            return bookViewAMQP;
        }
        catch( Exception ex ) {
            System.out.println(" [x] Exception sending book event: '" + ex.getMessage() + "'");
            return null;
        }
    }

    public LendingViewAMQP sendLendingEvent(LendingViewAMQP lendingViewAMQP, String bookEventType) {

        try {
            ObjectMapper objectMapper = new ObjectMapper();

            String jsonString = objectMapper.writeValueAsString(lendingViewAMQP);

            this.template.convertAndSend(direct1.getName(), bookEventType, jsonString);

            System.out.println(" [x] Sent Lending Validated '" + jsonString + "'");
            return lendingViewAMQP;
        }
        catch( Exception ex ) {
            System.out.println(" [x] Exception sending Lending Validated event: '" + ex.getMessage() + "'");
            return null;
        }

    }
}