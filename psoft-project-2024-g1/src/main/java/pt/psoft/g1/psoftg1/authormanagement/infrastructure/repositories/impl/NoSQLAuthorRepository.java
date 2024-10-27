package pt.psoft.g1.psoftg1.authormanagement.infrastructure.repositories.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import pt.psoft.g1.psoftg1.authormanagement.api.AuthorLendingView;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.authormanagement.repositories.AuthorRepository;

import java.util.List;
import java.util.Optional;

@Repository("mongoo")
@Qualifier("mongoo")
public class NoSQLAuthorRepository implements AuthorRepository {

    private final MongoTemplate mongoTemplate;

    public NoSQLAuthorRepository(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Optional<Author> findByAuthorNumber(Long authorNumber) {
        Query query = new Query(Criteria.where("authorNumber").is(authorNumber));
        Author author = mongoTemplate.findOne(query, Author.class);
        return Optional.ofNullable(author);
    }

    @Override
    public List<Author> searchByNameNameStartsWith(String name) {
        Query query = new Query(Criteria.where("name").regex("^" + name));
        return mongoTemplate.find(query, Author.class);
    }

    @Override
    public List<Author> searchByNameName(String name) {
        Query query = new Query(Criteria.where("name").is(name));
        return mongoTemplate.find(query, Author.class);
    }

    @Override
    public Author save(Author author) {
        return mongoTemplate.save(author);
    }

    @Override
    public Iterable<Author> findAll() {
        return mongoTemplate.findAll(Author.class);
    }

    @Override
    public void delete(Author author) {
        mongoTemplate.remove(author);
    }

    @Override
    public List<Author> findCoAuthorsByAuthorNumber(Long authorNumber) {
        Query query = new Query(Criteria.where("authors.authorNumber").is(authorNumber));
        return mongoTemplate.find(query, Author.class);
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
