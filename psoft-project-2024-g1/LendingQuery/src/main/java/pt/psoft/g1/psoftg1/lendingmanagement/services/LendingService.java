package pt.psoft.g1.psoftg1.lendingmanagement.services;

import pt.psoft.g1.psoftg1.lendingmanagement.api.LendingView;
import pt.psoft.g1.psoftg1.lendingmanagement.api.LendingViewAMQP;
import pt.psoft.g1.psoftg1.lendingmanagement.model.Lending;
import pt.psoft.g1.psoftg1.shared.services.Page;

import java.util.List;
import java.util.Optional;

public interface LendingService {
    /**
     * @param lendingNumber
     * @return {@code Optional<Lending>}
     */
    Optional<Lending> findByLendingNumber(String lendingNumber);
    Lending create(LendingViewAMQP resource);
    List<Lending> listByReaderNumberAndIsbn(String readerNumber, String isbn, Optional<Boolean> returned);

    Double getAverageDuration();

    List<Lending> getOverdue(Page page);

    Double getAvgLendingDurationByIsbn(String isbn);

    List<Lending> searchLendings(Page page, SearchLendingQuery request);
     //Iterable<LendingView> generateLendingRecommendations(final CreateLendingRequest resource);
}
