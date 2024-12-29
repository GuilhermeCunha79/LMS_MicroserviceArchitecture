package pt.psoft.g1.psoftg1.lendingmanagement.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import pt.psoft.g1.psoftg1.lendingmanagement.services.LendingService;
import pt.psoft.g1.psoftg1.shared.api.LendingViewAMQP;

import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class LendingEventRabbitmqReceiver {

    private final LendingService lendingService;


    @RabbitListener(queues = "#{autoDeleteQueue_Lending_Returned.name}")
    public void receiveLendingUpdated(Message msg) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();

            String jsonReceived = new String(msg.getBody(), StandardCharsets.UTF_8);
            LendingViewAMQP lendingViewAMQP = objectMapper.readValue(jsonReceived, LendingViewAMQP.class);

            System.out.println(" [x] Received Lending Updated by AMQP (Lending): " + msg + ".");
            try {
                lendingService.setReturned(lendingViewAMQP);
                System.out.println(" [x] Lending updated from AMQP (Lending): " + msg + ".");
            } catch (Exception e) {
                System.out.println(" [x] Lending does not exists or wrong version. Nothing stored. (Lending)");
            }
        } catch (Exception ex) {
            System.out.println(" [x] Exception receiving Lending event from AMQP (Lending): '" + ex.getMessage() + "'");
        }
    }

    @RabbitListener(queues = "#{autoDeleteQueue_Lending_Validation_Book.name}")
    public void receiveLendingValidationBook(Message msg) {

        try {
            ObjectMapper objectMapper = new ObjectMapper();

            String jsonReceived = new String(msg.getBody(), StandardCharsets.UTF_8);
            LendingViewAMQP bookViewAMQP = objectMapper.readValue(jsonReceived, LendingViewAMQP.class);

            System.out.println(" [x] Received Lending Validation from Books by AMQP (Lending): " + msg + ".");
            try {
                lendingService.create(bookViewAMQP);
                System.out.println(" [x] New Lending inserted from AMQP upon Book validation (Lending): " + msg + ".");
            } catch (Exception e) {
                System.out.println(" [x] Book provided during Lending creation is invalid. The Lending has been removed (Lending): "
                        + msg + ". Exception: " + e.getMessage());
            }
        } catch (
                Exception ex) {
            System.out.println(" [x] Exception receiving Lending event from AMQP (Lending): '" + ex.getMessage() + "'");
        }
    }
    @RabbitListener(queues = "#{autoDeleteQueue_Lending_Validation_Reader.name}")
    public void receiveLendingValidationReader(Message msg) {

        try {
            ObjectMapper objectMapper = new ObjectMapper();

            String jsonReceived = new String(msg.getBody(), StandardCharsets.UTF_8);
            LendingViewAMQP bookViewAMQP = objectMapper.readValue(jsonReceived, LendingViewAMQP.class);

            System.out.println(" [x] Received Lending Validation from Reader by AMQP (Lending): " + msg + ".");
            try {
                lendingService.createReader(bookViewAMQP);
                System.out.println(" [x] New Lending inserted from AMQP upon Reader validation (Lending): " + msg + ".");
            } catch (Exception e) {
                System.out.println(" [x] Reader provided during Lending creation is invalid. The Lending has been removed (Lending): "
                        + msg + ". Exception: " + e.getMessage());
            }
        } catch (
                Exception ex) {
            System.out.println(" [x] Exception receiving Lending event from AMQP (Lending): '" + ex.getMessage() + "'");
        }
    }

    @RabbitListener(queues = "#{autoDeleteQueue_Recommendation_Created.name}")
    public void receiveRecommendationCreated(Message msg) {

        try {
            ObjectMapper objectMapper = new ObjectMapper();

            String jsonReceived = new String(msg.getBody(), StandardCharsets.UTF_8);
            LendingViewAMQP bookViewAMQP = objectMapper.readValue(jsonReceived, LendingViewAMQP.class);

            System.out.println(" [x] Received Lending Validation from Reader by AMQP (Lending): " + msg + ".");
            try {
                lendingService.updateLendingRecommendation(bookViewAMQP);
                System.out.println(" [x] New Lending inserted from AMQP upon Reader validation (Lending): " + msg + ".");
            } catch (Exception e) {
                System.out.println(" [x] Reader provided during Lending creation is invalid. The Lending has been removed (Lending): "
                        + msg + ". Exception: " + e.getMessage());
            }
        } catch (
                Exception ex) {
            System.out.println(" [x] Exception receiving Lending event from AMQP (Lending): '" + ex.getMessage() + "'");
        }
    }

    @RabbitListener(queues = "#{autoDeleteQueue_Recommendation_Created_Failed.name}")
    public void receiveRecommendationFailed(Message msg) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();

            String jsonReceived = new String(msg.getBody(), StandardCharsets.UTF_8);
            LendingViewAMQP lendingViewAMQP = objectMapper.readValue(jsonReceived, LendingViewAMQP.class);

            System.out.println(" [x] Received Recommendation Failed from Reader by AMQP (Lending): " + msg + ".");
            try {
                lendingService.updateLendingRecommendationFailed(lendingViewAMQP);
                System.out.println(" [x] Lending with number " + lendingViewAMQP.getLendingNumber() +
                        " failed to process return due to an invalid commentary. Please try again! Message: " + msg + ".");
            } catch (Exception e) {
                System.out.println(" [x] Failed to delete Lending with number " + lendingViewAMQP.getLendingNumber() +"."+
                         msg + ". Exception: " + e.getMessage());
            }
        } catch (
                Exception ex) {
            System.out.println(" [x] Exception receiving Lending event from AMQP (Lending): '" + ex.getMessage() + "'");
        }
    }



}