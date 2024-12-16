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

import java.util.List;
import java.util.Optional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import pt.psoft.g1.psoftg1.exceptions.NotFoundException;
import pt.psoft.g1.psoftg1.usermanagement.model.User;
import pt.psoft.g1.psoftg1.usermanagement.repositories.UserRepository;

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
    @CacheEvict(allEntries = true)
    public <S extends User> List<S> saveAll(Iterable<S> entities) {
        for (S entity : entities) {
            entityManager.persist(entity); // ou entityManager.merge(entity) se for uma atualização
        }
        return (List<S>) entities; // Retorna a lista de entidades salvas
    }

    @Override
    @Caching(evict = {
            @CacheEvict(key = "#p0.id", condition = "#p0.id != null"),
            @CacheEvict(key = "#p0.username", condition = "#p0.username != null")
    })
    public <S extends User> S save(S entity) {
        if (entity.getId() == null) {
            entityManager.persist(entity); // Persistir novo usuário
        } else {
            entityManager.merge(entity); // Atualizar usuário existente
        }
        return entity;
    }

    @Override
    @Cacheable
    @Transactional
    public Optional<User> findByUsername(String username) {
        TypedQuery<User> query = entityManager.createQuery(
                "SELECT u FROM User u WHERE u.username = :username", User.class);
        query.setParameter("username", username);
        return query.getResultStream().findFirst();
    }

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
    public void delete(User user) {
        if (entityManager.contains(user)) {
            entityManager.remove(user);
        } else {
            entityManager.remove(entityManager.merge(user)); // Garante que o usuário está gerenciado antes de remover
        }
    }
}
