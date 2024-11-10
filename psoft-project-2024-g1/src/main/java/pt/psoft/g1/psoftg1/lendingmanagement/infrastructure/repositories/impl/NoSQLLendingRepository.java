package pt.psoft.g1.psoftg1.lendingmanagement.infrastructure.repositories.impl;

import jakarta.transaction.Transactional;
import org.springframework.context.annotation.Lazy;import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;
import pt.psoft.g1.psoftg1.lendingmanagement.model.Lending;
import pt.psoft.g1.psoftg1.lendingmanagement.model.LendingMongo;
import pt.psoft.g1.psoftg1.lendingmanagement.repositories.LendingRepository;
import pt.psoft.g1.psoftg1.lendingmanagement.services.LendingMapper;
import pt.psoft.g1.psoftg1.shared.services.Page;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Component("lendingMongo")
@Primary
@Lazy
public class NoSQLLendingRepository implements LendingRepository {

    private final MongoTemplate mongoTemplate;
    private final LendingMapper lendingMapper;

    public NoSQLLendingRepository(MongoTemplate mongoTemplate, LendingMapper lendingMapper) {
        this.mongoTemplate = mongoTemplate;
        this.lendingMapper = lendingMapper;
    }

    @Override
    public Optional<Lending> findByLendingNumber(String lendingNumber) {
        Query query = new Query(Criteria.where("lendingNumber.lendingNumber").is(lendingNumber));

        Lending lending = lendingMapper.toLending(mongoTemplate.findOne(query, LendingMongo.class));

        return Optional.ofNullable(lending);
    }

    @Override
    public List<Lending> listByReaderNumberAndIsbn(String readerNumber, String isbn) {
        Query query = new Query(Criteria.where("book.isbn").is(isbn)
                .and("readerDetails.readerNumber").is(readerNumber));

        List<LendingMongo> list = mongoTemplate.find(query, LendingMongo.class);

        return list.stream()
                .map(lendingMapper::toLending)
                .collect(Collectors.toList());
    }

    @Override
    public int getCountFromCurrentYear() {
        Query query = new Query(Criteria.where("startDate").gte(getStartOfYear()).lte(getEndOfYear()));
        return (int) mongoTemplate.count(query, LendingMongo.class);
    }

    @Override
    public List<Lending> listOutstandingByReaderNumber(String readerNumber) {
        Query query = new Query(Criteria.where("readerDetails.readerNumber").is(readerNumber)
                .and("returnedDate").is(null));

        List<LendingMongo> lendingMongo = mongoTemplate.find(query, LendingMongo.class);
        return lendingMongo.stream()
                .map(lendingMapper::toLending)
                .collect(Collectors.toList());
    }

    @Override
    public Double getAverageDuration() {
        List<LendingMongo> lendings = mongoTemplate.findAll(LendingMongo.class);
        return lendings.stream()
                .filter(lending -> lending.getReturnedDate() != null)
                .mapToLong(lending ->
                        lending.getReturnedDate().toEpochDay() - lending.getStartDate().toEpochDay())
                .average()
                .orElse(0.0);
    }

    @Override
    public Double getAvgLendingDurationByIsbn(String isbn) {
        List<LendingMongo> lendings = mongoTemplate.find(
                Query.query(Criteria.where("book.isbn").is(isbn)
                        .and("returnedDate").ne(null)),
                LendingMongo.class);

        return lendings.stream()
                .mapToLong(lending ->
                        lending.getReturnedDate().toEpochDay() - lending.getStartDate().toEpochDay())
                .average()
                .orElse(0.0);
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

    @Override
    public List<Lending> getOverdue(Page page) {
        Query query = new Query();
        query.addCriteria(Criteria.where("returnedDate").is(null)
                .and("limitDate").lt(LocalDate.now()));
        query.with(Sort.by(Sort.Order.asc("limitDate")));

        query.with(PageRequest.of(page.getNumber() - 1, page.getLimit()));

        List<LendingMongo> lendingMongoList = mongoTemplate.find(query, LendingMongo.class);

        return lendingMongoList.stream()
                .map(lendingMapper::toLending)
                .collect(Collectors.toList());
    }

    @Override
    public List<Lending> searchLendings(Page page, String readerNumber, String isbn, Boolean returned, LocalDate startDate, LocalDate endDate) {
        Query query = new Query();
        List<Criteria> criteriaList = new ArrayList<>();

        if (StringUtils.hasText(readerNumber)) {
            criteriaList.add(Criteria.where("readerDetails.readerNumber.readerNumber").regex(readerNumber));
        }
        if (StringUtils.hasText(isbn)) {
            criteriaList.add(Criteria.where("book.isbn.isbn").regex(isbn));
        }
        if (returned != null) {
            if (returned) {
                criteriaList.add(Criteria.where("returnedDate").ne(null));
            } else {
                criteriaList.add(Criteria.where("returnedDate").is(null));
            }
        }
        if (startDate != null) {
            criteriaList.add(Criteria.where("startDate").gte(startDate));
        }
        if (endDate != null) {
            criteriaList.add(Criteria.where("startDate").lte(endDate));
        }

        if (!criteriaList.isEmpty()) {
            query.addCriteria(new Criteria().andOperator(criteriaList.toArray(new Criteria[0])));
        }

        query.with(Sort.by(Sort.Order.asc("lendingNumber")));
        query.with(PageRequest.of(page.getNumber() - 1, page.getLimit()));

        List<LendingMongo> list = mongoTemplate.find(query, LendingMongo.class);

        return list.stream()
                .map(lendingMapper::toLending)
                .collect(Collectors.toList());
    }

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
}
