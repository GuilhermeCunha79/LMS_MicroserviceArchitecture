package pt.psoft.g1.psoftg1.publishers;

import pt.psoft.g1.psoftg1.recommendationmanagement.model.Recommendation;

public interface RecommendationEventsPublisher {

    void sendRecommendationCreatedToLending(Recommendation recommendation);
    void sendRecommendationCreatedFailedToLending(Recommendation recommendation);
}
