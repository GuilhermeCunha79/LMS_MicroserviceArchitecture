package pt.psoft.g1.psoftg1.lendingmanagement.infrastructure.repositories.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import pt.psoft.g1.psoftg1.lendingmanagement.model.Fine;
import pt.psoft.g1.psoftg1.lendingmanagement.repositories.FineRepository;

@Repository
@Profile("mongo")
@RequiredArgsConstructor
public class NoSQLFineRepository implements FineRepository {

    @Override
    public Fine save(Fine fine) {
        return null;
    }
}
