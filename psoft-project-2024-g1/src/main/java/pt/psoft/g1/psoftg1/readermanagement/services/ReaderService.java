package pt.psoft.g1.psoftg1.readermanagement.services;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import pt.psoft.g1.psoftg1.readermanagement.model.ReaderDetails;
import pt.psoft.g1.psoftg1.shared.services.Page;

/**
 *
 */
public interface ReaderService {
    ReaderDetails create(CreateReaderRequest request, String photoURI);
    ReaderDetails update(Long id, UpdateReaderRequest request, long desireVersion, String photoURI);
    Optional<ReaderDetails> findByUsername(final String username);
    Optional<ReaderDetails> findByReaderNumber(String readerNumber);
    List<ReaderDetails> findByPhoneNumber(String phoneNumber);
    Iterable<ReaderDetails> findAll();
    List<ReaderDetails> findTopReaders(int minTop);
    List<ReaderBookCountDTO> findTopByGenre(String genre, LocalDate startDate, LocalDate endDate);
    //Optional<Reader> update(UpdateReaderRequest request) throws Exception;
    Optional<ReaderDetails> removeReaderPhoto(String readerNumber, long desiredVersion);
    List<ReaderDetails> searchReaders(Page page, SearchReadersQuery query);
}
