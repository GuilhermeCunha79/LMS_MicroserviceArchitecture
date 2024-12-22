package pt.psoft.g1.psoftg1.readermanagement.services;

import pt.psoft.g1.psoftg1.readermanagement.model.ReaderDetails;
import pt.psoft.g1.psoftg1.shared.services.Page;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 *
 */
public interface ReaderService {
    Optional<ReaderDetails> findByUsername(final String username);
    Optional<ReaderDetails> findByReaderNumber(String readerNumber);
    List<ReaderDetails> findByPhoneNumber(String phoneNumber);
    Iterable<ReaderDetails> findAll();
    Optional<ReaderDetails> removeReaderPhoto(String readerNumber, long desiredVersion);
    List<ReaderDetails> searchReaders(Page page, SearchReadersQuery query);
}
