package pt.psoft.g1.psoftg1.recommendationmanagement.infraestructure.publishers.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pt.psoft.g1.psoftg1.publishers.RecommendationEventsPublisher;
import pt.psoft.g1.psoftg1.recommendationmanagement.api.RecommendationViewAMQP;
import pt.psoft.g1.psoftg1.recommendationmanagement.api.RecommendationViewAMQPMapper;
import pt.psoft.g1.psoftg1.recommendationmanagement.model.Recommendation;
import pt.psoft.g1.psoftg1.shared.model.RecommendationEvents;

@Service
@RequiredArgsConstructor
public class RecommendationEventsRabbitmqPublisherImpl implements RecommendationEventsPublisher {

    @Autowired
    private RabbitTemplate template;
    @Autowired
    private DirectExchange direct;
    private final RecommendationViewAMQPMapper recommendationViewAMQPMapper;

    private int count = 0;

    @Override
    public void sendRecommendationCreatedToLending(Recommendation recommendation) {
        sendRecommendationEvent(recommendation, RecommendationEvents.RECOMMENDATION_CREATED);
    }

    @Override
    public void sendRecommendationCreatedFailedToLending(Recommendation recommendation) {
        sendRecommendationEvent(recommendation, RecommendationEvents.RECOMMENDATION_CREATED_FAILED);
    }

    public void sendRecommendationEvent(Recommendation recommendation, String bookEventType) {

        try {
            ObjectMapper objectMapper = new ObjectMapper();

            RecommendationViewAMQP recommendationViewAMQP = recommendationViewAMQPMapper.toRecommendationViewAMQP(recommendation);

            String jsonString = objectMapper.writeValueAsString(recommendationViewAMQP);

            this.template.convertAndSend(direct.getName(), bookEventType, jsonString);

            System.out.println(" [x] Sent '" + jsonString + "'");
        }
        catch( Exception ex ) {
            System.out.println(" [x] Exception sending Recommendation event: '" + ex.getMessage() + "'");
        }
    }
}