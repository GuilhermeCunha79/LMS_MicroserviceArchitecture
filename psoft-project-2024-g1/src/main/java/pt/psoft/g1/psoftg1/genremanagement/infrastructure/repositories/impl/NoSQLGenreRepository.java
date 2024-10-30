package pt.psoft.g1.psoftg1.genremanagement.infrastructure.repositories.impl;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.authormanagement.model.AuthorMongo;
import pt.psoft.g1.psoftg1.bookmanagement.services.GenreBookCountDTO;
import pt.psoft.g1.psoftg1.genremanagement.api.GenreViewMapper;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;
import pt.psoft.g1.psoftg1.genremanagement.model.GenreMongo;
import pt.psoft.g1.psoftg1.genremanagement.repositories.GenreRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import pt.psoft.g1.psoftg1.genremanagement.services.GenreLendingsDTO;
import pt.psoft.g1.psoftg1.genremanagement.services.GenreLendingsPerMonthDTO;
import pt.psoft.g1.psoftg1.lendingmanagement.model.Lending;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Component("genreMongo")
public class NoSQLGenreRepository implements GenreRepository {

    private final MongoTemplate mongoTemplate;
    private final GenreViewMapper genreMapper;

    @Autowired
    public NoSQLGenreRepository(MongoTemplate mongoTemplate, GenreViewMapper genreMapper) {
        this.mongoTemplate = mongoTemplate;
        this.genreMapper = genreMapper;
    }

    @Override
    public Genre save(Genre genre) {
        mongoTemplate.save(genreMapper.toGenreMongo(genre));
        return genre;
    }

    @Override
    public List<Genre> findAll() {

        List<GenreMongo> bookList = mongoTemplate.findAll(GenreMongo.class);
        return bookList.stream()
                .map(genreMapper::toGenre)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void delete(Genre genre) {
        mongoTemplate.remove(genreMapper.toGenreMongo(genre));
    }

    @Override
    public Optional<Genre> findByString(String genreName) {
        Query query = new Query(Criteria.where("genre").is(genreName));
        List<Genre> list = mongoTemplate.find(query, Genre.class);

        return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
    }

    @Override
    public Page<GenreBookCountDTO> findTop5GenreByBookCount(Pageable pageable) {

        GroupOperation groupOperation = Aggregation.group("name")
                .count().as("bookCount");

        SortOperation sortOperation = Aggregation.sort(Sort.by(Sort.Direction.DESC, "bookCount"));
        LimitOperation limitOperation = Aggregation.limit(5);

        Aggregation aggregation = Aggregation.newAggregation(groupOperation, sortOperation, limitOperation);

        AggregationResults<GenreBookCountDTO> results = mongoTemplate.aggregate(aggregation, GenreMongo.class, GenreBookCountDTO.class);

        long total = mongoTemplate.count(new Query(), GenreMongo.class);

        return new PageImpl<>(results.getMappedResults(), pageable, total);
    }

    @Override
    public List<GenreLendingsDTO> getAverageLendingsInMonth(LocalDate month, pt.psoft.g1.psoftg1.shared.services.Page page) {
        int days = month.lengthOfMonth();
        LocalDate firstOfMonth = LocalDate.of(month.getYear(), month.getMonth(), 1);
        LocalDate lastOfMonth = LocalDate.of(month.getYear(), month.getMonth(), days);

        MatchOperation matchOperation = Aggregation.match(Criteria.where("startDate").gte(firstOfMonth).lte(lastOfMonth));

        GroupOperation groupOperation = Aggregation.group("book.genre")
                .count().as("loanCount")
                .avg("loanCount").as("dailyAvgLoans");

        SortOperation sortOperation = Aggregation.sort(Sort.by(Sort.Direction.ASC, "genre"));
        SkipOperation skipOperation = Aggregation.skip((page.getNumber() - 1) * page.getLimit());
        LimitOperation limitOperation = Aggregation.limit(page.getLimit());

        Aggregation aggregation = Aggregation.newAggregation(matchOperation, groupOperation, sortOperation, skipOperation, limitOperation);

        AggregationResults<GenreLendingsDTO> results = mongoTemplate.aggregate(aggregation, Lending.class, GenreLendingsDTO.class);

        return results.getMappedResults();
    }

    @Override
    public List<GenreLendingsPerMonthDTO> getLendingsPerMonthLastYearByGenre() {
        LocalDate now = LocalDate.now();
        LocalDate twelveMonthsAgo = now.minusMonths(12);

        ProjectionOperation project = Aggregation.project("book.genre", "startDate");

        GroupOperation group = Aggregation.group("book.genre", "year", "month")
                .count().as("count");

        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("startDate").gte(twelveMonthsAgo).lte(now)),
                project,
                group,
                Aggregation.sort(Sort.by(Sort.Direction.ASC, "year", "month", "book.genre"))
        );

        AggregationResults<GenreLendingsPerMonthDTO> results = mongoTemplate.aggregate(aggregation, Lending.class, GenreLendingsPerMonthDTO.class);

        return results.getMappedResults();
    }

    @Override
    public List<GenreLendingsPerMonthDTO> getLendingsAverageDurationPerMonth(LocalDate startDate, LocalDate endDate) {
        return null;
    }
}