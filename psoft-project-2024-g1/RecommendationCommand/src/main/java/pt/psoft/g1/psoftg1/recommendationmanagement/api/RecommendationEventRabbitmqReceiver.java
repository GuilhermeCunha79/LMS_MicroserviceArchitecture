package pt.psoft.g1.psoftg1.recommendationmanagement.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import pt.psoft.g1.psoftg1.recommendationmanagement.services.RecommendationService;

import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class RecommendationEventRabbitmqReceiver {

    private final RecommendationService recommendationService;

    @RabbitListener(queues = "#{autoDeleteQueue_Lending_Returned.name}")
    public void receiveLendingReturned(Message msg) {

        try {
            ObjectMapper objectMapper = new ObjectMapper();

            String jsonReceived = new String(msg.getBody(), StandardCharsets.UTF_8);
            RecommendationViewAMQP bookViewAMQP = objectMapper.readValue(jsonReceived, RecommendationViewAMQP.class);

            System.out.println(" [x] Received Lending Returned by AMQP (Recommendation): " + msg + ".");
            try {
                recommendationService.create(bookViewAMQP);
                System.out.println(" [x] New Recommendation inserted from AMQP (Recommendation): " + msg + ".");
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        catch(Exception ex) {
            System.out.println(" [x] Exception receiving Lending Returned event from AMQP (Recommendation): '" + ex.getMessage() + "'");
        }
    }
}