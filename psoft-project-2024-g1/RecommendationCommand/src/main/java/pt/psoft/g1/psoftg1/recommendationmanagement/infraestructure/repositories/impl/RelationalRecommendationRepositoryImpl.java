package pt.psoft.g1.psoftg1.recommendationmanagement.infraestructure.repositories.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import pt.psoft.g1.psoftg1.recommendationmanagement.model.Recommendation;
import pt.psoft.g1.psoftg1.recommendationmanagement.repositories.RecommendationRepository;

import java.util.Optional;

@Repository
@Profile("relational")
@RequiredArgsConstructor
public class RelationalRecommendationRepositoryImpl implements RecommendationRepository {

    private final EntityManager entityManager;

    @Transactional
    @Override
    public Recommendation save(Recommendation recommendation) {
        if (entityManager.contains(recommendation)) {
            recommendation = entityManager.merge(recommendation);
        } else {
            entityManager.persist(recommendation);
        }
        return recommendation;
    }


    @Override
    public void delete(Recommendation recommendation) {
        if (entityManager.contains(recommendation)) {
            entityManager.remove(recommendation);
        } else {
            Recommendation managedBook = entityManager.find(Recommendation.class, recommendation.getRecommendationNumber());
            if (managedBook != null) {
                entityManager.remove(managedBook);
            }
        }
    }

    @Override
    public Optional<Recommendation> findByLendingNumber(String lendingNumber) {
        try {
            return Optional.ofNullable(
                    entityManager.createQuery(
                                    "SELECT r FROM Recommendation r WHERE r.lendingNumber = :lendingNumber", Recommendation.class)
                            .setParameter("lendingNumber", lendingNumber)
                            .getSingleResult()
            );
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

}

