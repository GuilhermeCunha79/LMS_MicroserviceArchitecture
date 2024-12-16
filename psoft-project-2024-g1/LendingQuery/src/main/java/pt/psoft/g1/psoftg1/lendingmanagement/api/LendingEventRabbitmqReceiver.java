package pt.psoft.g1.psoftg1.lendingmanagement.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import pt.psoft.g1.psoftg1.lendingmanagement.services.LendingService;

import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class LendingEventRabbitmqReceiver {

    private final LendingService lendingService;

    @RabbitListener(queues = "#{autoDeleteQueue_Lending_Validation_Reader.name}")
    public void receiveLendingValidationReader(Message msg) {

        try {
            ObjectMapper objectMapper = new ObjectMapper();

            String jsonReceived = new String(msg.getBody(), StandardCharsets.UTF_8);
            LendingViewAMQP bookViewAMQP = objectMapper.readValue(jsonReceived, LendingViewAMQP.class);

            System.out.println(" [x] Received Lending Validation from Reader by AMQP (LendingQuery): " + msg + ".");
            try {
                lendingService.create(bookViewAMQP);
                System.out.println(" [x] New Lending inserted from AMQP upon Reader validation (LendingQuery): " + msg + ".");
            } catch (Exception e) {
                System.out.println(" [x] Reader provided during Lending creation is invalid. The Lending has been removed (LendingQuery): "
                        + msg + ". Exception: " + e.getMessage());
            }
        } catch (
                Exception ex) {
            System.out.println(" [x] Exception receiving Lending event from AMQP (LendingQuery): '" + ex.getMessage() + "'");
        }
    }

}