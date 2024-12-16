package pt.psoft.g1.psoftg1.recommendationmanagement.services;

import pt.psoft.g1.psoftg1.recommendationmanagement.api.RecommendationViewAMQP;
import pt.psoft.g1.psoftg1.recommendationmanagement.model.Recommendation;

/**
 *
 */
public interface RecommendationService {
    Recommendation create(CreateRecommendationRequest request);
    Recommendation create(RecommendationViewAMQP RecommendationViewAMQP); // AMQP request
}
