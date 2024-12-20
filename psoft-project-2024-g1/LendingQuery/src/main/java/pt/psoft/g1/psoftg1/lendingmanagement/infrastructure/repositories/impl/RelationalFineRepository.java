package pt.psoft.g1.psoftg1.lendingmanagement.infrastructure.repositories.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import pt.psoft.g1.psoftg1.lendingmanagement.model.Fine;
import pt.psoft.g1.psoftg1.lendingmanagement.repositories.FineRepository;

@Repository
@Profile("relational")
@RequiredArgsConstructor
public class RelationalFineRepository implements FineRepository {

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public Fine save(Fine fine) {
        return null;
    }

}
