package pt.psoft.g1.psoftg1.bookmanagement.model.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import pt.psoft.g1.psoftg1.bookmanagement.api.BookEventRabbitmqReceiver;
import pt.psoft.g1.psoftg1.bookmanagement.services.BookService;
import pt.psoft.g1.psoftg1.shared.api.BookViewAMQP;
import pt.psoft.g1.psoftg1.shared.api.LendingViewAMQP;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
public class BookEventRabbitmqReceiverTest {

    @InjectMocks
    private BookEventRabbitmqReceiver bookEventRabbitmqReceiver;

    @Mock
    private BookService bookService;

    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testReceiveBookUpdated_ValidMessage() throws Exception {
        // Arrange
        BookViewAMQP bookViewAMQP = new BookViewAMQP();
        bookViewAMQP.setIsbn("isbn123");
        bookViewAMQP.setTitle("Updated Book");
        bookViewAMQP.setAuthorIds(List.of("author1"));
        bookViewAMQP.setGenre("fiction");
        bookViewAMQP.setVersion(1L);

        String json = objectMapper.writeValueAsString(bookViewAMQP);
        Message message = MessageBuilder.withBody(json.getBytes(StandardCharsets.UTF_8)).build();

        // Act
        bookEventRabbitmqReceiver.receiveBookUpdated(message);

        ArgumentCaptor<BookViewAMQP> captor = ArgumentCaptor.forClass(BookViewAMQP.class);
        verify(bookService, times(1)).update(captor.capture());

        // Assert
        BookViewAMQP capturedArgument = captor.getValue();
        assertEquals("isbn123", capturedArgument.getIsbn());
        assertEquals("Updated Book", capturedArgument.getTitle());
        assertEquals(List.of("author1"), capturedArgument.getAuthorIds());
        assertEquals("fiction", capturedArgument.getGenre());
        assertEquals(Long.valueOf(1), capturedArgument.getVersion());
    }

    @Test
    public void testReceiveBookCreated_ValidMessage() throws Exception {
        // Arrange
        BookViewAMQP bookViewAMQP = new BookViewAMQP();
        bookViewAMQP.setTitle("Updated Book");
        bookViewAMQP.setAuthorIds(List.of("Author"));
        bookViewAMQP.setGenre("Fiction");
        bookViewAMQP.setIsbn("isbn123");
        bookViewAMQP.setVersion(1L);

        String json = objectMapper.writeValueAsString(bookViewAMQP);
        Message message = MessageBuilder.withBody(json.getBytes(StandardCharsets.UTF_8)).build();

        // Act
        bookEventRabbitmqReceiver.receiveBookCreated(message);  // Recebendo a mensagem de criação

        ArgumentCaptor<BookViewAMQP> captor = ArgumentCaptor.forClass(BookViewAMQP.class);
        verify(bookService, times(1)).create(captor.capture());

        // Assert
        BookViewAMQP capturedArgument = captor.getValue();
        assertEquals("isbn123", capturedArgument.getIsbn());
        assertEquals("Updated Book", capturedArgument.getTitle());
        assertEquals(List.of("Author"), capturedArgument.getAuthorIds());
        assertEquals("Fiction", capturedArgument.getGenre());
        assertEquals(Long.valueOf(1), capturedArgument.getVersion());
    }



    @Test
    public void testReceiveBookCreated_InvalidMessage() {
        // Arrange
        String invalidJson = "Invalid JSON";
        Message message = MessageBuilder.withBody(invalidJson.getBytes(StandardCharsets.UTF_8)).build();

        // Act
        bookEventRabbitmqReceiver.receiveBookCreated(message);

        // Assert
        verifyNoInteractions(bookService);
    }

    @Test
    public void testReceiveLendingCreatedUpdated_ValidMessage() throws Exception {
        // Arrange
        LendingViewAMQP lendingViewAMQP = new LendingViewAMQP();
        lendingViewAMQP.setLendingNumber("lending123");
        lendingViewAMQP.setIsbn("isbn123");
        lendingViewAMQP.setStatus(1);
        lendingViewAMQP.setReaderDetailsId("reader123");
        lendingViewAMQP.setReturnedDate("2024-12-31");
        lendingViewAMQP.setCommentary("No commentary");
        lendingViewAMQP.setVersion(1L);

        String json = objectMapper.writeValueAsString(lendingViewAMQP);
        Message message = MessageBuilder.withBody(json.getBytes(StandardCharsets.UTF_8)).build();

        // Act
        bookEventRabbitmqReceiver.receiveLendingCreatedUpdated(message);

        // Assert
        verify(bookService, times(1)).verifyIfIsbnExists(lendingViewAMQP);
    }




}
