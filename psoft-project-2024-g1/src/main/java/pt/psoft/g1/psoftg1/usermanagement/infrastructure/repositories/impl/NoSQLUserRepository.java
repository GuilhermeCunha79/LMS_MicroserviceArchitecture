package pt.psoft.g1.psoftg1.usermanagement.infrastructure.repositories.impl;

import jakarta.persistence.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.context.annotation.Lazy;import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import pt.psoft.g1.psoftg1.exceptions.NotFoundException;
import pt.psoft.g1.psoftg1.shared.services.Page;
import pt.psoft.g1.psoftg1.usermanagement.model.Librarian;import pt.psoft.g1.psoftg1.usermanagement.model.User;
import pt.psoft.g1.psoftg1.usermanagement.repositories.UserRepository;
import pt.psoft.g1.psoftg1.usermanagement.services.SearchUsersQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Component("userMongo")
@Primary
public class NoSQLUserRepository implements UserRepository {
    private final MongoTemplate mongoTemplate;

    public NoSQLUserRepository(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    @CacheEvict(allEntries = true)
    public <S extends User> List<S> saveAll(Iterable<S> entities) {
        entities.forEach(mongoTemplate::save);
        return (List<S>) entities;
    }

    @Override
    @Caching(evict = {
            @CacheEvict(key = "#p0.id", condition = "#p0.id != null"),
            @CacheEvict(key = "#p0.username", condition = "#p0.username != null")
    })
    public <S extends User> S save(S entity) {
        mongoTemplate.save(entity);
        return entity;
    }

    @Override
    public Optional<User> findById(Long objectId) {
        Query query = new Query(Criteria.where("id").is(objectId));
        User user = mongoTemplate.findOne(query, User.class);
        return Optional.ofNullable(user);
    }

    @Override
    public User getById(final Long id) {
        final Optional<User> maybeUser = findById(id);
        return maybeUser.filter(User::isEnabled)
                .orElseThrow(() -> new NotFoundException(User.class, id));
    }

    @Override
    public Optional<User> findByUsername(String username) {
        Query query = new Query(Criteria.where("username").is(username));
        Librarian librarian = mongoTemplate.findOne(query, Librarian.class);
        User user = null;
        if(librarian !=null) {
             user= new User(librarian.getUsername(),librarian.getPassword());

        }
        return Optional.ofNullable(user);
    }

    @Override
    public List<User> findByNameName(String name) {
        Query query = new Query(Criteria.where("name").is(name));
        return mongoTemplate.find(query, User.class);
    }

    @Override
    public List<User> findByNameNameContains(String name) {
        Query query = new Query(Criteria.where("name").regex(".*" + name + ".*", "i"));
        return mongoTemplate.find(query, User.class);
    }

    @Override
    public void delete(User user) {
        mongoTemplate.remove(user);
    }

    @Override
    public List<User> searchUsers(final Page page, final SearchUsersQuery query) {
        Query mongoQuery = new Query();
        List<Criteria> criteriaList = new ArrayList<>();

        if (StringUtils.hasText(query.getUsername())) {
            criteriaList.add(Criteria.where("username").is(query.getUsername()));
        }
        if (StringUtils.hasText(query.getFullName())) {
            criteriaList.add(Criteria.where("fullName").regex(query.getFullName(), "i"));
        }

        if (!criteriaList.isEmpty()) {
            mongoQuery.addCriteria(new Criteria().orOperator(criteriaList.toArray(new Criteria[0])));
        }

        mongoQuery.with(Sort.by(Sort.Direction.DESC, "createdAt"));

        Pageable pageable = PageRequest.of(page.getNumber() - 1, page.getLimit());
        mongoQuery.with(pageable);

        return mongoTemplate.find(mongoQuery, User.class);
    }
}
