package pt.psoft.g1.psoftg1.lendingmanagement.infrastructure.repositories.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import pt.psoft.g1.psoftg1.lendingmanagement.model.Lending;
import pt.psoft.g1.psoftg1.lendingmanagement.model.LendingMongo;
import pt.psoft.g1.psoftg1.lendingmanagement.repositories.LendingRepository;
import pt.psoft.g1.psoftg1.lendingmanagement.services.LendingMapper;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Profile("mongo")
@RequiredArgsConstructor
public class NoSQLLendingRepository implements LendingRepository {

    private final MongoTemplate mongoTemplate;
    private final LendingMapper lendingMapper;

    @Override
    @Transactional
    public Lending save(Lending lending) {
        mongoTemplate.save(lendingMapper.toLendingMongo(lending));
        return lending;
    }

    @Override
    @Transactional
    public void delete(Lending lending) {
        mongoTemplate.remove(lendingMapper.toLendingMongo(lending));
    }

    @Override
    public Optional<Lending> findByLendingNumber(String lendingNumber) {
        Query query = new Query(Criteria.where("lendingNumber.lendingNumber").is(lendingNumber));

        Lending lending = lendingMapper.toLending(mongoTemplate.findOne(query, LendingMongo.class));

        return Optional.ofNullable(lending);
    }
    @Override
    public int getCountFromCurrentYear() {
        Query query = new Query(Criteria.where("startDate").gte(getStartOfYear()).lte(getEndOfYear()));
        return (int) mongoTemplate.count(query, LendingMongo.class);
    }

    @Override
    public List<Lending> listOutstandingByReaderDetailsId(String readerNumber) {
        Query query = new Query(Criteria.where("readerDetails.readerNumber").is(readerNumber)
                .and("returnedDate").is(null));

        List<LendingMongo> lendingMongo = mongoTemplate.find(query, LendingMongo.class);
        return lendingMongo.stream()
                .map(lendingMapper::toLending)
                .collect(Collectors.toList());
    }
    private Date getStartOfYear() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, Calendar.JANUARY);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return calendar.getTime();
    }

    private Date getEndOfYear() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, Calendar.DECEMBER);
        calendar.set(Calendar.DAY_OF_MONTH, 31);
        return calendar.getTime();
    }


}
