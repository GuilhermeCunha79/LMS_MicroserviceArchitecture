package pt.psoft.g1.psoftg1.bookmanagement.model.CDC.publisher;

import au.com.dius.pact.core.model.Interaction;
import au.com.dius.pact.core.model.Pact;
import au.com.dius.pact.provider.MessageAndMetadata;
import au.com.dius.pact.provider.PactVerifyProvider;
import au.com.dius.pact.provider.junit.Provider;
import au.com.dius.pact.provider.junit.loader.PactFolder;
import au.com.dius.pact.provider.junit5.MessageTestTarget;
import au.com.dius.pact.provider.junit5.PactVerificationContext;
import au.com.dius.pact.provider.junit5.PactVerificationInvocationContextProvider;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.messaging.Message;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.bookmanagement.api.BookViewAMQPMapperImpl;
import pt.psoft.g1.psoftg1.bookmanagement.infrastructure.publishers.impl.BookEventsRabbitmqPublisherImpl;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.bookmanagement.publishers.BookEventsPublisher;
import pt.psoft.g1.psoftg1.bookmanagement.services.BookService;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;
import pt.psoft.g1.psoftg1.shared.api.BookViewAMQP;
import pt.psoft.g1.psoftg1.testutils.TestConfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


@Import(TestConfig.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.NONE
        ,classes = {BookEventsRabbitmqPublisherImpl.class, BookService.class, BookViewAMQPMapperImpl.class}
        , properties = {
        "stubrunner.amqp.mockConnection=true",
        "spring.profiles.active=test"
}
)
@Provider("book_event-producer")
@PactFolder("target/pacts")
@ExtendWith(PactVerificationInvocationContextProvider.class)
public class BooksProducerCDCIT {
    private static final Logger LOGGER = LoggerFactory.getLogger(BooksProducerCDCIT.class);

    @Autowired
    BookEventsPublisher bookEventsPublisher;

    @Autowired
    BookViewAMQPMapperImpl bookViewAMQPMapper;

    @MockBean
    RabbitTemplate template;

    @MockBean
    DirectExchange direct;
/*
    @TestTemplate
    void testTemplate(Pact pact, Interaction interaction, PactVerificationContext context) {
        context.verifyInteraction();
    }
*/
    @BeforeEach
    void before(PactVerificationContext context) {
        context.setTarget(new MessageTestTarget());
    }

    @PactVerifyProvider("a book created event")
    public MessageAndMetadata bookCreated() throws JsonProcessingException {

        Genre genre = new Genre("Infantil");

        Author author = new Author("José Saramago", "shot bio", "");
        List<Author> authors = new ArrayList<>();
        authors.add(author);

        Book book = new Book(
                "6475803429671",
                "title",
                "description",
                genre,
                authors,
                "");

        BookViewAMQP bookViewAMQP = bookEventsPublisher.sendBookCreated(book);

        Message<String> message = new BookMessageBuilder().withBook(bookViewAMQP).build();

        return generateMessageAndMetadata(message);
    }

    @PactVerifyProvider("a book updated event")
    public MessageAndMetadata bookUpdated() throws JsonProcessingException {

        Genre genre = new Genre("Infantil");

        Author author = new Author("José Saramago", "shot bio", "");
        List<Author> authors = new ArrayList<>();
        authors.add(author);

        Book book = new Book(
                "6475803429671",
                "title",
                "description",
                genre,
                authors,
                "");

        BookViewAMQP bookViewAMQP = bookEventsPublisher.sendBookUpdated(book, 1L);

        Message<String> message = new BookMessageBuilder().withBook(bookViewAMQP).build();

        return generateMessageAndMetadata(message);
    }

    private MessageAndMetadata generateMessageAndMetadata(Message<String> message) {
        HashMap<String, Object> metadata = new HashMap<>(message.getHeaders());

        return new MessageAndMetadata(message.getPayload().getBytes(), metadata);
    }
}