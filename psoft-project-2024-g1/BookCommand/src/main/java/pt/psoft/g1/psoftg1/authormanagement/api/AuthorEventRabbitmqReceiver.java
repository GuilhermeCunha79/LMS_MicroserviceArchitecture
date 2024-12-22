package pt.psoft.g1.psoftg1.authormanagement.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import pt.psoft.g1.psoftg1.authormanagement.services.AuthorService;
import pt.psoft.g1.psoftg1.shared.api.AuthorViewAMQP;

import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class AuthorEventRabbitmqReceiver {

    private final AuthorService authorService;

    @RabbitListener(queues = "#{autoDeleteQueue_Author_Created.name}")
    public void receiveAuthorCreated(Message msg) {

        try {
            ObjectMapper objectMapper = new ObjectMapper();

            String jsonReceived = new String(msg.getBody(), StandardCharsets.UTF_8);
            AuthorViewAMQP authorViewAMQP = objectMapper.readValue(jsonReceived, AuthorViewAMQP.class);
            System.out.println(" [x] Received Author Created by AMQP: " + msg + ".");
            try {
                authorService.create(authorViewAMQP);
                System.out.println(" [x] New Author inserted from AMQP: " + msg + ".");
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        catch(Exception ex) {
            System.out.println(" [x] Exception receiving Author event from AMQP: '" + ex.getMessage() + "'");
        }
    }
}