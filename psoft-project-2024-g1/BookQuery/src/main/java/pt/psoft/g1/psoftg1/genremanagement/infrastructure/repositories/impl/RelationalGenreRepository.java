package pt.psoft.g1.psoftg1.genremanagement.infrastructure.repositories.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Tuple;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.bookmanagement.services.GenreBookCountDTO;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;
import pt.psoft.g1.psoftg1.genremanagement.repositories.GenreRepository;
import pt.psoft.g1.psoftg1.genremanagement.services.GenreLendingsDTO;
import pt.psoft.g1.psoftg1.genremanagement.services.GenreLendingsPerMonthDTO;

import java.time.LocalDate;
import java.util.*;

@Repository
@Profile("relational")
@RequiredArgsConstructor
public class RelationalGenreRepository implements GenreRepository {

    @PersistenceContext
    private final EntityManager entityManager;


    @Override
    @Transactional
    public Genre save(Genre genre) {
        entityManager.persist(genre);
        return genre;
    }

    @Override
    public Iterable<Genre> findAll() {

        return entityManager.createQuery("SELECT g FROM Genre g", Genre.class).getResultList();
    }

    @Override
    @Transactional
    public void delete(Genre genre) {
        entityManager.remove(genre);
    }

    @Override
    @Transactional
    public Optional<Genre> findByString(String genreName) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Genre> query = cb.createQuery(Genre.class);
        Root<Genre> genreRoot = query.from(Genre.class);
        query.select(genreRoot).where(cb.equal(genreRoot.get("genre"), genreName));
        return entityManager.createQuery(query).getResultStream().findFirst();
    }
}