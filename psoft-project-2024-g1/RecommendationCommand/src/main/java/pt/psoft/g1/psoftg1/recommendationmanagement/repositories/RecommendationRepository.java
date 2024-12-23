package pt.psoft.g1.psoftg1.recommendationmanagement.repositories;


import pt.psoft.g1.psoftg1.recommendationmanagement.model.Recommendation;

import java.util.List;
import java.util.Optional;


/**
 *
 */
public interface RecommendationRepository {
    Recommendation save(Recommendation recommendation);

    void delete(Recommendation author);

    Optional<Recommendation> findByLendingNumber(String lendingNumber);
    List<Recommendation> findAll();
}
