package pt.psoft.g1.psoftg1.lendingmanagement.infrastructure.repositories.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import pt.psoft.g1.psoftg1.lendingmanagement.model.Lending;
import pt.psoft.g1.psoftg1.lendingmanagement.repositories.LendingRepository;
import pt.psoft.g1.psoftg1.shared.services.Page;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@Profile("relational")
@RequiredArgsConstructor
public class RelationalLendingRepository implements LendingRepository {

    private final EntityManager entityManager;

    @Override
    @Transactional
    public Lending save(Lending lending) {
        return entityManager.merge(lending);
    }


    @Override
    @Transactional
    public void delete(Lending lending) {
        Lending managedLending = entityManager.contains(lending)
                ? lending
                : entityManager.merge(lending);

        entityManager.remove(managedLending);
    }


    @Override
    public List<Lending> listOutstandingByReaderDetailsId(String readerNumber) {
        TypedQuery<Lending> query = entityManager.createQuery(
                "SELECT l FROM Lending l " +
                        "WHERE l.readerDetailsId = :readerNumber " +
                        "AND l.returnedDate IS NULL", Lending.class);
        query.setParameter("readerNumber", readerNumber);
        return query.getResultList();
    }

    @Override
    @Transactional
    public Optional<Lending> findByLendingNumber(String lendingNumber) {
        TypedQuery<Lending> query = entityManager.createQuery(
                "SELECT l FROM Lending l WHERE l.lendingNumber.lendingNumber = :lendingNumber AND l.status = 2", Lending.class);
        query.setParameter("lendingNumber", lendingNumber);
        return query.getResultStream().findFirst();
    }

    @Override
    public int getCountFromCurrentYear() {
        return ((Number) entityManager.createQuery(
                        "SELECT COUNT(l) FROM Lending l WHERE YEAR(l.startDate) = YEAR(CURRENT_DATE)")
                .getSingleResult()).intValue();
    }

    @Override
    public List<Lending> listByReaderNumberAndIsbn(String readerNumber, String isbn) {
        TypedQuery<Lending> query = entityManager.createQuery(
                "SELECT l FROM Lending l " +
                        "WHERE l.isbn = :isbn " +
                        "AND l.readerDetailsId = :readerNumber", Lending.class);
        query.setParameter("isbn", isbn);
        query.setParameter("readerNumber", readerNumber);
        return query.getResultList();
    }

    @Override
    public Double getAverageDuration() {
        return (Double) entityManager.createNativeQuery(
                        "SELECT AVG(DATEDIFF(day, l.start_date, l.returned_date)) FROM Lending l")
                .getSingleResult();
    }

    @Override
    public Double getAvgLendingDurationByIsbn(String isbn) {
        return (Double) entityManager.createNativeQuery(
                        "SELECT AVG(DATEDIFF(day, l.start_date, l.returned_date)) " +
                                "FROM Lending l JOIN BOOK b ON l.BOOK_PK = b.PK WHERE b.ISBN = :isbn")
                .setParameter("isbn", isbn)
                .getSingleResult();
    }

    @Override
    public List<Lending> getOverdue(Page page) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Lending> cq = cb.createQuery(Lending.class);
        Root<Lending> root = cq.from(Lending.class);
        cq.select(root);

        List<Predicate> where = new ArrayList<>();
        where.add(cb.isNull(root.get("returnedDate")));
        where.add(cb.lessThan(root.get("limitDate"), LocalDate.now()));
        cq.where(where.toArray(new Predicate[0]));
        cq.orderBy(cb.asc(root.get("limitDate")));

        TypedQuery<Lending> query = entityManager.createQuery(cq);
        query.setFirstResult((page.getNumber() - 1) * page.getLimit());
        query.setMaxResults(page.getLimit());

        return query.getResultList();
    }

    @Override
    public List<Lending> searchLendings(Page page, String readerNumber, String isbn, Boolean returned, LocalDate startDate, LocalDate endDate) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Lending> cq = cb.createQuery(Lending.class);
        Root<Lending> lendingRoot = cq.from(Lending.class);
        Join<Lending, Long> bookJoin = lendingRoot.join("bookId");
        Join<Lending, Long> readerDetailsJoin = lendingRoot.join("readerDetailsId");
        cq.select(lendingRoot);

        List<Predicate> where = new ArrayList<>();

        if (StringUtils.hasText(readerNumber))
            where.add(cb.like(readerDetailsJoin.get("readerNumber").get("readerNumber"), readerNumber));
        if (StringUtils.hasText(isbn))
            where.add(cb.like(bookJoin.get("isbn").get("isbn"), isbn));
        if (returned != null) {
            if (returned) {
                where.add(cb.isNotNull(lendingRoot.get("returnedDate")));
            } else {
                where.add(cb.isNull(lendingRoot.get("returnedDate")));
            }
        }
        if (startDate != null)
            where.add(cb.greaterThanOrEqualTo(lendingRoot.get("startDate"), startDate));
        if (endDate != null)
            where.add(cb.lessThanOrEqualTo(lendingRoot.get("startDate"), endDate));

        cq.where(where.toArray(new Predicate[0]));
        cq.orderBy(cb.asc(lendingRoot.get("lendingNumber")));

        TypedQuery<Lending> query = entityManager.createQuery(cq);
        query.setFirstResult((page.getNumber() - 1) * page.getLimit());
        query.setMaxResults(page.getLimit());

        return query.getResultList();
    }

}