/*
 * Copyright (c) 2022-2024 the original author or authors.
 *
 * MIT License
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package pt.psoft.g1.psoftg1.usermanagement.infrastructure.repositories.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import pt.psoft.g1.psoftg1.exceptions.NotFoundException;
import pt.psoft.g1.psoftg1.shared.services.Page;
import pt.psoft.g1.psoftg1.usermanagement.model.User;
import pt.psoft.g1.psoftg1.usermanagement.repositories.UserRepository;
import pt.psoft.g1.psoftg1.usermanagement.services.SearchUsersQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Based on https://github.com/Yoh0xFF/java-spring-security-example
 */
@Repository
@Profile("relational")
@RequiredArgsConstructor
public class RelationalUserRepository implements UserRepository {

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    @Cacheable
    public Optional<User> findById(Long objectId) {
        User user = entityManager.find(User.class, objectId);
        return Optional.ofNullable(user);
    }

    @Override
    public User getById(final Long id) {
        final Optional<User> maybeUser = findById(id);
        return maybeUser.filter(User::isEnabled)
                .orElseThrow(() -> new NotFoundException(User.class, id));
    }

    @Override
    @Cacheable
    public Optional<User> findByUsername(String username) {
        TypedQuery<User> query = entityManager.createQuery(
                "SELECT u FROM User u WHERE u.username = :username", User.class);
        query.setParameter("username", username);
        return query.getResultStream().findFirst();
    }

    @Override
    @Cacheable
    public List<User> findByNameName(String name) {
        TypedQuery<User> query = entityManager.createQuery(
                "SELECT u FROM User u WHERE u.name = :name", User.class);
        query.setParameter("name", name);
        return query.getResultList();
    }

    @Override
    public List<User> findByNameNameContains(String name) {
        TypedQuery<User> query = entityManager.createQuery(
                "SELECT u FROM User u WHERE u.name.name LIKE :name", User.class);
        query.setParameter("name", "%" + name + "%");
        return query.getResultList();
    }

    @Override
    public List<User> searchUsers(final Page page, final SearchUsersQuery query) {

        final CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        final CriteriaQuery<User> cq = cb.createQuery(User.class);
        final Root<User> root = cq.from(User.class);
        cq.select(root);

        final List<Predicate> where = new ArrayList<>();
        if (StringUtils.hasText(query.getUsername())) {
            where.add(cb.equal(root.get("username"), query.getUsername()));
        }
        if (StringUtils.hasText(query.getFullName())) {
            where.add(cb.like(root.get("fullName"), "%" + query.getFullName() + "%"));
        }

        // search using OR
        if (!where.isEmpty()) {
            cq.where(cb.or(where.toArray(new Predicate[0])));
        }

        cq.orderBy(cb.desc(root.get("createdAt")));

        final TypedQuery<User> q = entityManager.createQuery(cq);
        q.setFirstResult((page.getNumber() - 1) * page.getLimit());
        q.setMaxResults(page.getLimit());

        return q.getResultList();
    }
}
