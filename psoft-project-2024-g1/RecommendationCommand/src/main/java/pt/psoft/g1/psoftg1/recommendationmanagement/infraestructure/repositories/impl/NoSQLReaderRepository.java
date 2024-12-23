package pt.psoft.g1.psoftg1.recommendationmanagement.infraestructure.repositories.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import pt.psoft.g1.psoftg1.recommendationmanagement.model.Recommendation;
import pt.psoft.g1.psoftg1.recommendationmanagement.repositories.RecommendationRepository;

import java.util.List;
import java.util.Optional;


@Profile("mongo")
@RequiredArgsConstructor
public class NoSQLReaderRepository implements RecommendationRepository {


    private final MongoTemplate mongoTemplate;

    @Override
    public Recommendation save(Recommendation recommendation) {
        return mongoTemplate.save(recommendation);
    }

    @Override
    public void delete(Recommendation recommendation) {
        mongoTemplate.remove(recommendation);
    }

    @Override
    public Optional<Recommendation> findByLendingNumber(String lendingNumber) {
        // Realiza a consulta no MongoDB
        Recommendation recommendation = mongoTemplate.findOne(
                Query.query(Criteria.where("lendingNumber").is(lendingNumber)),
                Recommendation.class
        );
        return Optional.ofNullable(recommendation);
    }

    @Override
    public List<Recommendation> findAll() {
        return mongoTemplate.findAll(Recommendation.class);
    }

}
