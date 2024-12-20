package pt.psoft.g1.psoftg1.lendingmanagement.repositories;

import pt.psoft.g1.psoftg1.lendingmanagement.model.Lending;
import pt.psoft.g1.psoftg1.shared.services.Page;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface LendingRepository {
    List<Lending> listOutstandingByReaderDetailsId(String readerNumber);
    Optional<Lending> findByLendingNumber(String lendingNumber);
    List<Lending> listByReaderNumberAndIsbn(String readerNumber, String isbn);
    Lending save(Lending lending);
    void delete(Lending lending);
    int getCountFromCurrentYear();
    Double getAverageDuration();
    Double getAvgLendingDurationByIsbn(String isbn);
    List<Lending> getOverdue(Page page);
    List<Lending> searchLendings(Page page, String readerNumber, String isbn, Boolean returned, LocalDate startDate, LocalDate endDate);;
}
