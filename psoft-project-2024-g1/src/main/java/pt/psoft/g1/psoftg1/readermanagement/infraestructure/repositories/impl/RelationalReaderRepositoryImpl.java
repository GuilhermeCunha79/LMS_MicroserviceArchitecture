package pt.psoft.g1.psoftg1.readermanagement.infraestructure.repositories.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import org.springframework.context.annotation.Lazy;import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import pt.psoft.g1.psoftg1.readermanagement.services.SearchReadersQuery;
import pt.psoft.g1.psoftg1.readermanagement.model.ReaderDetails;
import pt.psoft.g1.psoftg1.readermanagement.repositories.ReaderRepository;

import org.springframework.data.domain.Pageable;
import pt.psoft.g1.psoftg1.readermanagement.services.ReaderBookCountDTO;
import pt.psoft.g1.psoftg1.usermanagement.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component("readerJpa")
public class RelationalReaderRepositoryImpl implements ReaderRepository {

    private final EntityManager entityManager;

    public RelationalReaderRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @Query("SELECT r " +
            "FROM ReaderDetails r " +
            "WHERE r.readerNumber.readerNumber = :readerNumber")
    public Optional<ReaderDetails> findByReaderNumber(@Param("readerNumber") @NotNull String readerNumber) {
        TypedQuery<ReaderDetails> query = entityManager.createQuery("SELECT r FROM ReaderDetails r WHERE r.readerNumber.readerNumber = :readerNumber", ReaderDetails.class);
        query.setParameter("readerNumber", readerNumber);
        return query.getResultList().stream().findFirst();
    }

    @Override
    @Query("SELECT r " +
            "FROM ReaderDetails r " +
            "WHERE r.phoneNumber.phoneNumber = :phoneNumber")
    public List<ReaderDetails> findByPhoneNumber(@Param("phoneNumber") @NotNull String phoneNumber) {
        TypedQuery<ReaderDetails> query = entityManager.createQuery("SELECT r FROM ReaderDetails r WHERE r.phoneNumber.phoneNumber = :phoneNumber", ReaderDetails.class);
        query.setParameter("phoneNumber", phoneNumber);
        return query.getResultList();
    }

    @Override
    @Query("SELECT r " +
            "FROM ReaderDetails r " +
            "JOIN User u ON r.reader.id = u.id " +
            "WHERE u.username = :username")
    public Optional<ReaderDetails> findByUsername(@Param("username") @NotNull String username) {
        TypedQuery<ReaderDetails> query = entityManager.createQuery("SELECT r FROM ReaderDetails r JOIN User u ON r.reader.id = u.id WHERE u.username = :username", ReaderDetails.class);
        query.setParameter("username", username);
        return query.getResultList().stream().findFirst();
    }

    @Override
    @Query("SELECT r " +
            "FROM ReaderDetails r " +
            "JOIN User u ON r.reader.id = u.id " +
            "WHERE u.id = :userId")
    public Optional<ReaderDetails> findByUserId(@Param("userId") @NotNull Long userId) {
        TypedQuery<ReaderDetails> query = entityManager.createQuery("SELECT r FROM ReaderDetails r JOIN User u ON r.reader.id = u.id WHERE u.id = :userId", ReaderDetails.class);
        query.setParameter("userId", userId);
        return query.getResultList().stream().findFirst();
    }

    @Override
    @Query("SELECT COUNT (rd) " +
            "FROM ReaderDetails rd " +
            "JOIN User u ON rd.reader.id = u.id " +
            "WHERE YEAR(u.createdAt) = YEAR(CURRENT_DATE)")
    public int getCountFromCurrentYear() {
        TypedQuery<Long> query = entityManager.createQuery("SELECT COUNT (rd) FROM ReaderDetails rd JOIN User u ON rd.reader.id = u.id WHERE YEAR(u.createdAt) = YEAR(CURRENT_DATE)", Long.class);
        return query.getSingleResult().intValue();
    }

    @Override
    public ReaderDetails save(ReaderDetails readerDetails) {
        if (Objects.equals(readerDetails.getReaderNumber(), readerDetails.getReaderNumber())) {
            // Se a entidade não existe (id nulo ou não encontrada), usa persist
            entityManager.merge(readerDetails);
        } else {
            // Caso contrário, usa merge
            entityManager.persist(readerDetails);
        }
        return readerDetails;
    }


    @Override
    public Iterable<ReaderDetails> findAll() {
        return entityManager.createQuery("SELECT rd FROM ReaderDetails rd", ReaderDetails.class).getResultList();
    }

    @Override
    @Query("SELECT rd " +
            "FROM ReaderDetails rd " +
            "JOIN Lending l ON l.readerDetails.pk = rd.pk " +
            "GROUP BY rd " +
            "ORDER BY COUNT(l) DESC")
    public Page<ReaderDetails> findTopReaders(Pageable pageable) {

        TypedQuery<ReaderDetails> query = entityManager.createQuery(
                "SELECT rd " +
                        "FROM ReaderDetails rd " +
                        "JOIN Lending l ON l.readerDetails.pk = rd.pk " +
                        "GROUP BY rd " +
                        "ORDER BY COUNT(l) DESC", ReaderDetails.class);


        int totalRows = query.getResultList().size();
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        List<ReaderDetails> results = query.getResultList();
        return new PageImpl<>(results, pageable, totalRows);
    }

    @Override
    @Query("SELECT NEW pt.psoft.g1.psoftg1.readermanagement.services.ReaderBookCountDTO(rd, count(l)) " +
            "FROM ReaderDetails rd " +
            "JOIN Lending l ON l.readerDetails.pk = rd.pk " +
            "JOIN Book b ON b.pk = l.book.pk " +
            "JOIN Genre g ON g.pk = b.genre.pk " +
            "WHERE g.genre = :genre " +
            "AND l.startDate >= :startDate " +
            "AND l.startDate <= :endDate " +
            "GROUP BY rd.pk " +
            "ORDER BY COUNT(l.pk) DESC")
    public Page<ReaderBookCountDTO> findTopByGenre(Pageable pageable,
                                                   @Param("genre") String genre,
                                                   @Param("startDate") LocalDate startDate,
                                                   @Param("endDate") LocalDate endDate) {

        TypedQuery<ReaderBookCountDTO> query = entityManager.createQuery(
                "SELECT NEW pt.psoft.g1.psoftg1.readermanagement.services.ReaderBookCountDTO(rd, count(l)) " +
                        "FROM ReaderDetails rd " +
                        "JOIN Lending l ON l.readerDetails.pk = rd.pk " +
                        "JOIN Book b ON b.pk = l.book.pk " +
                        "JOIN Genre g ON g.pk = b.genre.pk " +
                        "WHERE g.genre = :genre " +
                        "AND l.startDate >= :startDate " +
                        "AND l.startDate <= :endDate " +
                        "GROUP BY rd.pk " +
                        "ORDER BY COUNT(l.pk) DESC", ReaderBookCountDTO.class);


        query.setParameter("genre", genre);
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);

        int totalRows = query.getResultList().size();
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        List<ReaderBookCountDTO> results = query.getResultList();
        return new PageImpl<>(results, pageable, totalRows);
    }

    @Override
    public void delete(ReaderDetails readerDetails) {
        entityManager.remove(entityManager.contains(readerDetails) ? readerDetails : entityManager.merge(readerDetails));
    }

    @Override
    public List<ReaderDetails> searchReaderDetails(final pt.psoft.g1.psoftg1.shared.services.Page page, final SearchReadersQuery query) {

        final CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        final CriteriaQuery<ReaderDetails> cq = cb.createQuery(ReaderDetails.class);
        final Root<ReaderDetails> readerDetailsRoot = cq.from(ReaderDetails.class);
        Join<ReaderDetails, User> userJoin = readerDetailsRoot.join("reader");

        cq.select(readerDetailsRoot);

        final List<Predicate> where = new ArrayList<>();
        if (StringUtils.hasText(query.getName())) {
            where.add(cb.like(userJoin.get("name").get("name"), "%" + query.getName() + "%"));
            cq.orderBy(cb.asc(userJoin.get("name")));
        }
        if (StringUtils.hasText(query.getEmail())) {
            where.add(cb.equal(userJoin.get("username"), query.getEmail()));
            cq.orderBy(cb.asc(userJoin.get("username")));

        }
        if (StringUtils.hasText(query.getPhoneNumber())) {
            where.add(cb.equal(readerDetailsRoot.get("phoneNumber").get("phoneNumber"), query.getPhoneNumber()));
            cq.orderBy(cb.asc(readerDetailsRoot.get("phoneNumber").get("phoneNumber")));
        }

        if (!where.isEmpty()) {
            cq.where(cb.or(where.toArray(new Predicate[0])));
        }

        final TypedQuery<ReaderDetails> q = entityManager.createQuery(cq);
        q.setFirstResult((page.getNumber() - 1) * page.getLimit());
        q.setMaxResults(page.getLimit());

        return q.getResultList();
    }
}

