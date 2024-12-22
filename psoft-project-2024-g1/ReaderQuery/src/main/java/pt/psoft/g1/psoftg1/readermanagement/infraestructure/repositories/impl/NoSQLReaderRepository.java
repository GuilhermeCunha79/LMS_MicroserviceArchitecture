package pt.psoft.g1.psoftg1.readermanagement.infraestructure.repositories.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import pt.psoft.g1.psoftg1.readermanagement.model.ReaderDetails;
import pt.psoft.g1.psoftg1.readermanagement.repositories.ReaderRepository;
import pt.psoft.g1.psoftg1.readermanagement.services.SearchReadersQuery;
import pt.psoft.g1.psoftg1.shared.services.Page;

import java.util.*;

@Repository
@Profile("mongo")
@RequiredArgsConstructor
public class NoSQLReaderRepository implements ReaderRepository {


    private final MongoTemplate mongoTemplate;

    @Override
    public Optional<ReaderDetails> findByReaderNumber(String readerNumber) {
        Query query = new Query(Criteria.where("readerNumber.readerNumber").is(readerNumber));
        ReaderDetails readerDetails = mongoTemplate.findOne(query, ReaderDetails.class);
        return Optional.ofNullable(readerDetails);
    }

    @Override
    public List<ReaderDetails> findByPhoneNumber(String phoneNumber) {
        Query query = new Query(Criteria.where("phoneNumber.phoneNumber").is(phoneNumber));
        return mongoTemplate.find(query, ReaderDetails.class);
    }

    @Override
    public Optional<ReaderDetails> findByUsername(String username) {
        Query query = new Query(Criteria.where("user.username").is(username));
        ReaderDetails readerDetails = mongoTemplate.findOne(query, ReaderDetails.class);
        return Optional.ofNullable(readerDetails);
    }

    @Override
    public Optional<ReaderDetails> findByUserId(Long userId) {
        Query query = new Query(Criteria.where("user.id").is(userId));
        ReaderDetails readerDetails = mongoTemplate.findOne(query, ReaderDetails.class);
        return Optional.ofNullable(readerDetails);
    }

    @Override
    public int getCountFromCurrentYear() {
        Query query = new Query();
        query.addCriteria(Criteria.where("user.createdAt").gte(getStartOfYear()).lt(getStartOfNextYear()));
        return (int) mongoTemplate.count(query, ReaderDetails.class);
    }

    @Override
    public ReaderDetails save(ReaderDetails readerDetails) {
        return mongoTemplate.save(readerDetails);
    }

    @Override
    public Iterable<ReaderDetails> findAll() {
        return mongoTemplate.findAll(ReaderDetails.class);
    }
    private Date getStartOfYear() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, Calendar.JANUARY);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }
    private Date getStartOfNextYear() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, cal.get(Calendar.YEAR) + 1);
        cal.set(Calendar.MONTH, Calendar.JANUARY);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    @Override
    public void delete(ReaderDetails readerDetails) {
        mongoTemplate.remove(readerDetails);
    }

    @Override
    public List<ReaderDetails> searchReaderDetails(final Page page, final SearchReadersQuery query) {

        Query mongoQuery = new Query();


        List<Criteria> criteriaList = new ArrayList<>();

        if (StringUtils.hasText(query.getName())) {

            criteriaList.add(Criteria.where("reader.name").regex(".*" + query.getName() + ".*", "i"));
            mongoQuery.with(Sort.by(Sort.Direction.ASC, "reader.name"));
        }

        if (StringUtils.hasText(query.getEmail())) {

            criteriaList.add(Criteria.where("reader.username").is(query.getEmail()));
            mongoQuery.with(Sort.by(Sort.Direction.ASC, "reader.username"));
        }

        if (StringUtils.hasText(query.getPhoneNumber())) {
            criteriaList.add(Criteria.where("phoneNumber.phoneNumber").is(query.getPhoneNumber()));
            mongoQuery.with(Sort.by(Sort.Direction.ASC, "phoneNumber.phoneNumber"));
        }

        if (!criteriaList.isEmpty()) {
            mongoQuery.addCriteria(new Criteria().orOperator(criteriaList.toArray(new Criteria[0])));
        }

        mongoQuery.skip((long) (page.getNumber() - 1) * page.getLimit()).limit(page.getLimit());

        return mongoTemplate.find(mongoQuery, ReaderDetails.class);
    }
}
