package pt.psoft.g1.psoftg1.lendingmanagement.infrastructure.repositories.impl;

import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import pt.psoft.g1.psoftg1.lendingmanagement.model.Fine;
import pt.psoft.g1.psoftg1.lendingmanagement.model.FineMongo;
import pt.psoft.g1.psoftg1.lendingmanagement.repositories.FineRepository;

import java.util.Optional;

@Component("fineMongo")
@Primary
public class NoSQLFineRepository implements FineRepository {

    private final MongoTemplate mongoTemplate;

    public NoSQLFineRepository(MongoTemplate mongoTemplate) {
        this.mongoTemplate=mongoTemplate;
    }

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

    @Override
    public Fine save(Fine fine) {
        return null;
    }
}
