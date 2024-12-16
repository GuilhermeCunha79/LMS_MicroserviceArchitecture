package pt.psoft.g1.psoftg1.authormanagement.infrastructure.repositories.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;
import pt.psoft.g1.psoftg1.authormanagement.api.AuthorLendingView;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.authormanagement.model.AuthorMongo;
import pt.psoft.g1.psoftg1.authormanagement.repositories.AuthorRepository;
import pt.psoft.g1.psoftg1.authormanagement.services.AuthorMapper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Profile("authorMongo")
@Repository
@RequiredArgsConstructor
public class NoSQLAuthorRepository implements AuthorRepository {

    private final MongoTemplate mongoTemplate;
    private final AuthorMapper authorMapper;

    @Override
    public Author save(Author author) {
        if (author == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        mongoTemplate.save(authorMapper.toAuthorMongo(author));

        return author;
    }
    
    @Override
    public void delete(Author author) {
        mongoTemplate.remove(authorMapper.toAuthorMongo(author));
    }

    @Override
    public Optional<Author> findByAuthorNumber(String authorNumber) {
        Query query = new Query(Criteria.where("authorNumber").is(authorNumber));
        Author author = authorMapper.toAuthor(mongoTemplate.findOne(query, AuthorMongo.class));

        return Optional.ofNullable(author);
    }

    @Override
    public List<Author> searchByNameName(String name) {
        Query query = new Query(Criteria.where("name").is(name));
        List<AuthorMongo> authorMongoList = mongoTemplate.find(query, AuthorMongo.class);

        return authorMongoList.stream()
                .map(authorMapper::toAuthor)
                .collect(Collectors.toList());
    }
}
