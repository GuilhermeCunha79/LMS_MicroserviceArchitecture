package pt.psoft.g1.psoftg1.lendingmanagement.infrastructure.repositories.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import pt.psoft.g1.psoftg1.lendingmanagement.model.Fine;
import pt.psoft.g1.psoftg1.lendingmanagement.repositories.FineRepository;

import java.util.Optional;

@Repository
@Profile("mongo")
@RequiredArgsConstructor
public class NoSQLFineRepository implements FineRepository {

    private final MongoTemplate mongoTemplate;

    public Optional<Fine> findByLendingNumber(String lendingNumber) {
        Query query = new Query();
        query.addCriteria(Criteria.where("lending.lendingNumber").is(lendingNumber));
        Fine fine = mongoTemplate.findOne(query, Fine.class);

        return Optional.ofNullable(fine);
    }

    @Override
    public Iterable<Fine> findAll() {
        return null;
    }
}
