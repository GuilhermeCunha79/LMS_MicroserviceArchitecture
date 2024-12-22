package pt.psoft.g1.psoftg1.readermanagement.services;

import pt.psoft.g1.psoftg1.readermanagement.model.ReaderDetails;
import pt.psoft.g1.psoftg1.shared.api.LendingViewAMQP;
import pt.psoft.g1.psoftg1.shared.api.ReaderDetailsViewAMQP;
import pt.psoft.g1.psoftg1.shared.services.Page;

import java.util.List;
import java.util.Optional;

/**
 *
 */
public interface ReaderService {
    ReaderDetails create(CreateReaderRequest request, String photoURI);
    ReaderDetails create(ReaderDetailsViewAMQP bookViewAMQP); // AMQP request
    void verifyIfReaderNumberExists(LendingViewAMQP lendingViewAMQP);
    Optional<ReaderDetails> findByReaderNumber(String readerNumber);
}
