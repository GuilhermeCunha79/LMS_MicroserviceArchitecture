package pt.psoft.g1.psoftg1.lendingmanagement.infrastructure.repositories.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.context.annotation.Lazy;import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import pt.psoft.g1.psoftg1.lendingmanagement.model.Fine;
import pt.psoft.g1.psoftg1.lendingmanagement.repositories.FineRepository;

import java.util.Optional;

@Lazy
@Component("fineJpa")
public class RelationalFineRepository implements FineRepository {

    private final EntityManager entityManager;

    public RelationalFineRepository(EntityManager entityManager){
        this.entityManager=entityManager;
    }

    @Override
    public Optional<Fine> findByLendingNumber(String lendingNumber) {
        String jpql = "SELECT f FROM Fine f JOIN f.lending l WHERE l.lendingNumber.lendingNumber = :lendingNumber";
        TypedQuery<Fine> query = entityManager.createQuery(jpql, Fine.class);
        query.setParameter("lendingNumber", lendingNumber);

        return query.getResultStream().findFirst();
    }
    @Override
    public Iterable<Fine> findAll() {
        return null;
    }

    @Override
    public Fine save(Fine fine) {
        return null;
    }

}
