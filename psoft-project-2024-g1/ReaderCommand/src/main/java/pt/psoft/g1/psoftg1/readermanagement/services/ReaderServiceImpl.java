package pt.psoft.g1.psoftg1.readermanagement.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pt.psoft.g1.psoftg1.exceptions.ConflictException;
import pt.psoft.g1.psoftg1.exceptions.NotFoundException;
import pt.psoft.g1.psoftg1.readermanagement.model.ReaderDetails;
import pt.psoft.g1.psoftg1.readermanagement.publishers.ReaderEventsPublisher;
import pt.psoft.g1.psoftg1.readermanagement.repositories.ReaderRepository;
import pt.psoft.g1.psoftg1.shared.api.LendingViewAMQP;
import pt.psoft.g1.psoftg1.shared.api.ReaderDetailsViewAMQP;
import pt.psoft.g1.psoftg1.shared.model.LendingStatus;
import pt.psoft.g1.psoftg1.shared.repositories.ForbiddenNameRepository;
import pt.psoft.g1.psoftg1.usermanagement.Reader;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReaderServiceImpl implements ReaderService {
    private final ReaderRepository readerRepo;
    private final ReaderMapper readerMapper;
    private final ForbiddenNameRepository forbiddenNameRepository;
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
    public void verifyIfReaderNumberExists(LendingViewAMQP lendingViewAMQP) {
        boolean exists = readerRepo.findByReaderNumber(lendingViewAMQP.getReaderDetailsId()).isPresent();
        lendingViewAMQP.setStatus(exists ? LendingStatus.LENDING_VALIDATED_READERS : LendingStatus.LENDING_INVALIDATED);

        readerEventsPublisher.sendReaderValidatedToLending(lendingViewAMQP);
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
    public Optional<ReaderDetails> findByReaderNumber(String readerNumber) {
        return this.readerRepo.findByReaderNumber(readerNumber);
    }
}
