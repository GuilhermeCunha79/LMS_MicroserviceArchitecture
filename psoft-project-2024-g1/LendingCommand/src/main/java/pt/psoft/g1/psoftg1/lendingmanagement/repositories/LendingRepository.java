package pt.psoft.g1.psoftg1.lendingmanagement.repositories;

import pt.psoft.g1.psoftg1.lendingmanagement.model.Lending;
import pt.psoft.g1.psoftg1.shared.services.Page;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface LendingRepository {
    Lending save(Lending lending);
    void delete(Lending lending);
    List<Lending> listOutstandingByReaderDetailsId(String readerNumber);
    Optional<Lending> findByLendingNumber(String lendingNumber);
    int getCountFromCurrentYear();
}
