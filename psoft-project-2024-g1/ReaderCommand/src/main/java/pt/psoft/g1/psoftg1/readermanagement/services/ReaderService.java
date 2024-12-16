package pt.psoft.g1.psoftg1.readermanagement.services;

import pt.psoft.g1.psoftg1.readermanagement.api.LendingViewAMQP;
import pt.psoft.g1.psoftg1.readermanagement.api.ReaderDetailsViewAMQP;
import pt.psoft.g1.psoftg1.readermanagement.model.ReaderDetails;
import pt.psoft.g1.psoftg1.shared.services.Page;

import java.util.List;
import java.util.Optional;

/**
 *
 */
public interface ReaderService {
    ReaderDetails create(CreateReaderRequest request, String photoURI);
    ReaderDetails create(ReaderDetailsViewAMQP bookViewAMQP); // AMQP request
    boolean verifyIfReaderNumberExists(LendingViewAMQP lendingViewAMQP);
    ReaderDetails update(Long id, UpdateReaderRequest request, long desireVersion, String photoURI);
    Optional<ReaderDetails> findByUsername(final String username);
    Optional<ReaderDetails> findByReaderNumber(String readerNumber);
    List<ReaderDetails> findByPhoneNumber(String phoneNumber);
    Iterable<ReaderDetails> findAll();

    //Optional<Reader> update(UpdateReaderRequest request) throws Exception;
    Optional<ReaderDetails> removeReaderPhoto(String readerNumber, long desiredVersion);
    List<ReaderDetails> searchReaders(Page page, SearchReadersQuery query);
}
