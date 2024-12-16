package pt.psoft.g1.psoftg1.readermanagement.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pt.psoft.g1.psoftg1.exceptions.ConflictException;
import pt.psoft.g1.psoftg1.exceptions.NotFoundException;
import pt.psoft.g1.psoftg1.readermanagement.api.LendingViewAMQP;
import pt.psoft.g1.psoftg1.readermanagement.api.ReaderDetailsViewAMQP;
import pt.psoft.g1.psoftg1.readermanagement.model.ReaderDetails;
import pt.psoft.g1.psoftg1.readermanagement.repositories.ReaderRepository;
import pt.psoft.g1.psoftg1.shared.model.LendingStatus;
import pt.psoft.g1.psoftg1.shared.repositories.ForbiddenNameRepository;
import pt.psoft.g1.psoftg1.shared.repositories.PhotoRepository;
import pt.psoft.g1.psoftg1.usermanagement.Reader;
import publishers.ReaderEventsPublisher;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReaderServiceImpl implements ReaderService {
    private final ReaderRepository readerRepo;
    private final ReaderMapper readerMapper;
    private final ForbiddenNameRepository forbiddenNameRepository;
    private final PhotoRepository photoRepository;
    private final ReaderEventsPublisher readerEventsPublisher;

    @Override
    public ReaderDetails create(CreateReaderRequest request, String photoURI) {

        Iterable<String> words = List.of(request.getFullName().split("\\s+"));
        for (String word : words) {
            if (!forbiddenNameRepository.findByForbiddenNameIsContained(word).isEmpty()) {
                throw new IllegalArgumentException("Name contains a forbidden word");
            }
        }

        List<Long> stringInterestList = request.getInterestList();

        MultipartFile photo = request.getPhoto();
        if (photo == null && photoURI != null || photo != null && photoURI == null) {
            request.setPhoto(null);
        }

        int count = readerRepo.getCountFromCurrentYear();
        Reader reader = readerMapper.createReader(request);
        ReaderDetails rd = readerMapper.createReaderDetails(count + 1, reader, request, photoURI, stringInterestList);

        return readerRepo.save(rd);
    }


    @Override
    public ReaderDetails create(ReaderDetailsViewAMQP readerDetailsViewAMQP) {

        final String email = readerDetailsViewAMQP.getEmail();
        final String readerNumber = readerDetailsViewAMQP.getReaderNumber();
        final List<String> interestList = readerDetailsViewAMQP.getInterestList();
        final String phoneNumber = readerDetailsViewAMQP.getPhoneNumber();
        final String birtDate = readerDetailsViewAMQP.getBirthDate();
        final String fullName = readerDetailsViewAMQP.getFullName();

        return create(email, readerNumber, fullName, interestList, phoneNumber, birtDate);
    }

    @Override
    public boolean verifyIfReaderNumberExists(LendingViewAMQP lendingViewAMQP) {
        boolean exists = readerRepo.findByReaderNumber(lendingViewAMQP.getReaderDetailsId()).isPresent();
        lendingViewAMQP.setStatus(exists ? LendingStatus.LENDING_VALIDATED_READERS : LendingStatus.LENDING_INVALIDATED);

        readerEventsPublisher.sendReaderValidatedToLending(lendingViewAMQP);
        return exists;
    }

    private ReaderDetails create(String email, String readerNumber, String fullName, List<String> interestList,
                                 String phoneNumber, String birthDate) {

        if (readerRepo.findByUsername(email).isPresent()) {
            throw new ConflictException("Reader with Email " + email + " already exists");
        }
        Reader reader = Reader.newReader(email, "Mariaroberta!123", fullName);
        ReaderDetails newBook = new ReaderDetails(readerNumber, reader, birthDate, phoneNumber, true,
                true, true, null, interestList);

        return readerRepo.save(newBook);
    }

    @Override
    public ReaderDetails update(final Long id, final UpdateReaderRequest request, final long desiredVersion, String photoURI) {
        final ReaderDetails readerDetails = readerRepo.findByUserId(id)
                .orElseThrow(() -> new NotFoundException("Cannot find reader"));

        List<Long> stringInterestList = request.getInterestList();
        //List<Long> interestList = this.getGenreListFromStringList(stringInterestList);

        /*
         * Since photos can be null (no photo uploaded) that means the URI can be null as well.
         * To avoid the client sending false data, photoURI has to be set to any value / null
         * according to the MultipartFile photo object
         *
         * That means:
         * - photo = null && photoURI = null -> photo is removed
         * - photo = null && photoURI = validString -> ignored
         * - photo = validFile && photoURI = null -> ignored
         * - photo = validFile && photoURI = validString -> photo is set
         * */

        MultipartFile photo = request.getPhoto();
        if (photo == null && photoURI != null || photo != null && photoURI == null) {
            request.setPhoto(null);
        }

        //readerDetails.applyPatch(desiredVersion, request, photoURI, interestList);

        return readerRepo.save(readerDetails);
    }


    @Override
    public Optional<ReaderDetails> findByReaderNumber(String readerNumber) {
        return this.readerRepo.findByReaderNumber(readerNumber);
    }

    @Override
    public List<ReaderDetails> findByPhoneNumber(String phoneNumber) {
        return this.readerRepo.findByPhoneNumber(phoneNumber);
    }

    @Override
    public Optional<ReaderDetails> findByUsername(final String username) {
        return this.readerRepo.findByUsername(username);
    }


    @Override
    public Iterable<ReaderDetails> findAll() {
        return this.readerRepo.findAll();
    }

    @Override
    public Optional<ReaderDetails> removeReaderPhoto(String readerNumber, long desiredVersion) {
        ReaderDetails readerDetails = readerRepo.findByReaderNumber(readerNumber)
                .orElseThrow(() -> new NotFoundException("Cannot find reader"));

        String photoFile = readerDetails.getPhoto().getPhotoFile();
        readerDetails.removePhoto(desiredVersion);
        Optional<ReaderDetails> updatedReader = Optional.of(readerRepo.save(readerDetails));
        photoRepository.deleteByPhotoFile(photoFile);
        return updatedReader;
    }

    @Override
    public List<ReaderDetails> searchReaders(pt.psoft.g1.psoftg1.shared.services.Page page, SearchReadersQuery query) {
        if (page == null)
            page = new pt.psoft.g1.psoftg1.shared.services.Page(1, 10);

        if (query == null)
            query = new SearchReadersQuery("", "", "");

        final var list = readerRepo.searchReaderDetails(page, query);

        if (list.isEmpty())
            throw new NotFoundException("No results match the search query");

        return list;
    }
}
