package pt.psoft.g1.psoftg1.readermanagement.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import pt.psoft.g1.psoftg1.readermanagement.services.ReaderService;

import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class ReaderEventRabbitmqReceiver {

    private final ReaderService readerService;

    @RabbitListener(queues = "#{autoDeleteQueue_Reader_Created.name}")
    public void receiveBookCreated(Message msg) {

        try {
            ObjectMapper objectMapper = new ObjectMapper();

            String jsonReceived = new String(msg.getBody(), StandardCharsets.UTF_8);
            ReaderDetailsViewAMQP readerViewAMQP = objectMapper.readValue(jsonReceived, ReaderDetailsViewAMQP.class);

            System.out.println(" [x] Received Reader Created by AMQP (Reader): " + msg + ".");
            try {
                readerService.create(readerViewAMQP);
                System.out.println(" [x] New Reader inserted from AMQP (Reader): " + msg + ".");
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        } catch (Exception ex) {
            System.out.println(" [x] Exception receiving Reader event from AMQP (Reader): '" + ex.getMessage() + "'");
        }
    }


    @RabbitListener(queues = "#{autoDeleteQueue_Lending_Validation_Reader.name}")
    public void receiveLendingValidated(Message msg) {

        try {
            ObjectMapper objectMapper = new ObjectMapper();

            String jsonReceived = new String(msg.getBody(), StandardCharsets.UTF_8);
            LendingViewAMQP bookViewAMQP = objectMapper.readValue(jsonReceived, LendingViewAMQP.class);

            System.out.println(" [x] Received Lending from Lending by AMQP for validation (<Reader>): " + msg + ".");
            try {
                readerService.verifyIfReaderNumberExists(bookViewAMQP);
                System.out.println(" [x] New Lending inserted from AMQP upon Book validation (Reader): " + msg + ".");
            } catch (Exception e) {
                System.out.println(" [x] Reader provided during Lending creation is invalid. The Lending has been removed (Reader): "
                        + msg + ". Exception: " + e.getMessage());
            }
        } catch (
                Exception ex) {
            System.out.println(" [x] Exception receiving Lending event from AMQP (Reader): '" + ex.getMessage() + "'");
        }
    }


}