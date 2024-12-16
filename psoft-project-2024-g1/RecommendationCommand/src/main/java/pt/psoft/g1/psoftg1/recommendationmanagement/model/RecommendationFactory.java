package pt.psoft.g1.psoftg1.recommendationmanagement.model;

import pt.psoft.g1.psoftg1.shared.model.Commentary;

public class RecommendationFactory {

    public static Recommendation create( String lendingNumber, String readerNumber, String bookId, Commentary status) {
        return new Recommendation(lendingNumber,readerNumber,bookId,status);
    }
}
