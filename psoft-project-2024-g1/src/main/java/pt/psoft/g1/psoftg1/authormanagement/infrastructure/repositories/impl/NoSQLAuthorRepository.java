package pt.psoft.g1.psoftg1.authormanagement.infrastructure.repositories.impl;

import jakarta.persistence.LockModeType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import pt.psoft.g1.psoftg1.authormanagement.api.AuthorLendingView;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.authormanagement.model.AuthorMongo;
import pt.psoft.g1.psoftg1.authormanagement.repositories.AuthorRepository;
import pt.psoft.g1.psoftg1.authormanagement.services.AuthorMapper;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component("authorMongo")
@Primary
public class NoSQLAuthorRepository implements AuthorRepository {

    private final MongoTemplate mongoTemplate;
    private final AuthorMapper authorMapper;

    @Autowired
    public NoSQLAuthorRepository(MongoTemplate mongoTemplate, AuthorMapper authorMapper) {
        this.mongoTemplate = mongoTemplate;
        this.authorMapper = authorMapper;
    }

    @Override
    public Optional<Author> findByAuthorNumber(String authorNumber) {
        Query query = new Query(Criteria.where("authorNumber").is(authorNumber));
        Author author = authorMapper.toAuthor(mongoTemplate.findOne(query, AuthorMongo.class));

        return Optional.ofNullable(author);
    }

    @Override
    public List<Author> searchByNameNameStartsWith(String name) {
        Query query = new Query(Criteria.where("name").regex("^" + name));
        List<AuthorMongo> authorMongoList = mongoTemplate.find(query, AuthorMongo.class);

        return authorMongoList.stream()
                .map(authorMapper::toAuthor)
                .collect(Collectors.toList());
    }

    @Override
    public List<Author> searchByNameName(String name) {
        Query query = new Query(Criteria.where("name").is(name));
        List<AuthorMongo> authorMongoList = mongoTemplate.find(query, AuthorMongo.class);

        return authorMongoList.stream()
                .map(authorMapper::toAuthor)
                .collect(Collectors.toList());
    }

    @Override
    public Author save(Author author) {
        if (author == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        mongoTemplate.save(authorMapper.toAuthorMongo(author));

        return author;
    }


    @Override
    public Iterable<Author> findAll() {
        List<AuthorMongo> authorMongoList = mongoTemplate.findAll(AuthorMongo.class);

        return authorMongoList.stream()
                .map(authorMapper::toAuthor)
                .collect(Collectors.toList());
    }
    
    @Override
    public void delete(Author author) {
        mongoTemplate.remove(authorMapper.toAuthorMongo(author));
    }

    @Override
    public List<Author> findCoAuthorsByAuthorNumber(String authorNumber) {
        Query query = new Query(Criteria.where("authors.authorNumber").is(authorNumber));
        List<AuthorMongo> authorMongoList = mongoTemplate.find(query, AuthorMongo.class);

        return authorMongoList.stream()
                .map(authorMapper::toAuthor)
                .collect(Collectors.toList());
    }

    @Override
    public Page<AuthorLendingView> findTopAuthorByLendings(Pageable pageableRules) {
        return findTopAuthorsByLendingCount(pageableRules);
    }

    private Page<AuthorLendingView> findTopAuthorsByLendingCount(Pageable pageable) {
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.project("authorName")
                        .and("lendings").size().as("lendingCount"),
                Aggregation.sort(Sort.by(Sort.Direction.DESC, "lendingCount")),
                Aggregation.skip((long) pageable.getPageNumber() * pageable.getPageSize()),
                Aggregation.limit(pageable.getPageSize())
        );

        AggregationResults<AuthorLendingView> results = mongoTemplate.aggregate(aggregation, "author", AuthorLendingView.class);
        List<AuthorLendingView> authorLendingViews = results.getMappedResults();

        return new PageImpl<>(authorLendingViews, pageable, authorLendingViews.size());
    }
}
