package pt.psoft.g1.psoftg1.recommendationmanagement.services;

import pt.psoft.g1.psoftg1.recommendationmanagement.api.RecommendationViewAMQP;
import pt.psoft.g1.psoftg1.recommendationmanagement.model.Recommendation;
import pt.psoft.g1.psoftg1.shared.api.LendingViewAMQP;

/**
 *
 */
public interface RecommendationService {
    Recommendation create(CreateRecommendationRequest request);
    Recommendation create(LendingViewAMQP lendingViewAMQP); // AMQP request
    Recommendation update(LendingViewAMQP lendingViewAMQP); // AMQP request
    void delete(String recommendationNumber); // AMQP request
}
