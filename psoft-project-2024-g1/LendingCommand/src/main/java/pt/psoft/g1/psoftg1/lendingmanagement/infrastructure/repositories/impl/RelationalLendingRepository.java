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
import java.util.*;

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
        // Certifique-se de que a entidade está no estado "managed"
        Lending managedLending = entityManager.contains(lending)
                ? lending // Já está gerida
                : entityManager.merge(lending); // Tornar gerida, se estiver detached

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
                "SELECT l FROM Lending l WHERE l.lendingNumber.lendingNumber = :lendingNumber", Lending.class);
        query.setParameter("lendingNumber", lendingNumber);
        return query.getResultStream().findFirst();
    }

    @Override
    public int getCountFromCurrentYear() {
        return ((Number) entityManager.createQuery(
                        "SELECT COUNT(l) FROM Lending l WHERE YEAR(l.startDate) = YEAR(CURRENT_DATE)")
                .getSingleResult()).intValue();
    }


}