package pt.psoft.g1.psoftg1.bookmanagement.model.CDC.publisher;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import pt.psoft.g1.psoftg1.shared.api.BookViewAMQP;


public class BookMessageBuilder {
    private ObjectMapper mapper = new ObjectMapper();
    private BookViewAMQP bookViewAMQP;

    public BookMessageBuilder withBook(BookViewAMQP bookViewAMQP) {
        this.bookViewAMQP = bookViewAMQP;
        return this;
    }

    public Message<String> build() throws JsonProcessingException {
        return MessageBuilder.withPayload(this.mapper.writeValueAsString(this.bookViewAMQP))
                .setHeader("Content-Type", "application/json; charset=utf-8")
                .build();
    }

}