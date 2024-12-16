package pt.psoft.g1.psoftg1.shared.repositories;

import pt.psoft.g1.psoftg1.shared.model.ForbiddenName;

import java.util.List;
import java.util.Optional;

public interface ForbiddenNameRepository {
    Iterable<ForbiddenName> findAll();
    List<ForbiddenName> findByForbiddenNameIsContained(String pat);
    ForbiddenName save(ForbiddenName forbiddenName);

    Optional<ForbiddenName> findByForbiddenName(String forbiddenName);

    int deleteForbiddenName(String forbiddenName);

}
