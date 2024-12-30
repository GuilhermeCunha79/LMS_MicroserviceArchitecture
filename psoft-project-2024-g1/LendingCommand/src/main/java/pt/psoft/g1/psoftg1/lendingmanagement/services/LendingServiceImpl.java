package pt.psoft.g1.psoftg1.lendingmanagement.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import pt.psoft.g1.psoftg1.exceptions.LendingForbiddenException;
import pt.psoft.g1.psoftg1.exceptions.NotFoundException;
import pt.psoft.g1.psoftg1.lendingmanagement.api.LendingViewAMQPMapper;
import pt.psoft.g1.psoftg1.lendingmanagement.model.Fine;
import pt.psoft.g1.psoftg1.lendingmanagement.model.Lending;
import pt.psoft.g1.psoftg1.lendingmanagement.model.LendingFactory;
import pt.psoft.g1.psoftg1.lendingmanagement.model.LendingStatus;
import pt.psoft.g1.psoftg1.lendingmanagement.publishers.LendingEventsPublisher;
import pt.psoft.g1.psoftg1.lendingmanagement.repositories.FineRepository;
import pt.psoft.g1.psoftg1.lendingmanagement.repositories.LendingRepository;
import pt.psoft.g1.psoftg1.shared.api.LendingViewAMQP;
import pt.psoft.g1.psoftg1.shared.model.generateID.GenerateIDService;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@PropertySource({"classpath:config/library.properties"})
public class LendingServiceImpl implements LendingService {
    private final LendingRepository lendingRepository;
    private final FineRepository fineRepository;
    private final LendingEventsPublisher lendingEventsPublisher;
    private final LendingViewAMQPMapper lendingViewAMQPMapper;
    @Value("${lendingDurationInDays}")
    private int lendingDurationInDays;
    @Value("${fineValuePerDayInCents}")
    private int fineValuePerDayInCents;

    private final GenerateIDService generateIDService;

    @Override
    public Optional<Lending> findByLendingNumber(String lendingNumber) {
        return lendingRepository.findByLendingNumber(lendingNumber);
    }

    @Override
    public Lending create(final CreateLendingRequest resource) {
        int count = 0;

        Iterable<Lending> lendingList = lendingRepository.listOutstandingByReaderDetailsId(resource.getReaderNumber());
        for (Lending lending : lendingList) {
            //Business rule: cannot create a lending if user has late outstanding books to return.
            if (lending.getDaysDelayed() > 0) {
                throw new LendingForbiddenException("Reader has book(s) past their due date");
            }
            count++;
            //Business rule: cannot create a lending if user already has 3 outstanding books to return.
            if (count >= 3) {
                throw new LendingForbiddenException("Reader has three books outstanding already");
            }
        }

        int seq = lendingRepository.getCountFromCurrentYear() + 1;
        final Lending l = LendingFactory.create(resource.getIsbn(), resource.getReaderNumber(), seq, lendingDurationInDays, fineValuePerDayInCents);
        l.setPk(String.valueOf(generateIDService.generateID()));

        lendingEventsPublisher.sendLendingCreatedToBook(l);

        return lendingRepository.save(l);
    }

    @Override
    public Lending create(LendingViewAMQP lendingViewAMQP) {
        return createLending(lendingViewAMQP, "Book");
    }

    @Override
    public Lending createReader(LendingViewAMQP lendingViewAMQP) {
        return createLending(lendingViewAMQP, "Reader");
    }

    @Override
    public Lending updateLendingRecommendation(LendingViewAMQP resource) {
        Optional<Lending> lending = findByLendingNumber(resource.getLendingNumber());

        if (lending.isPresent()) {
            lending.get().setRecommendationNumber(resource.getRecommendationNumber());
            lending.get().setStatus(LendingStatus.LENDING_VALIDATED_READERS);

            lendingEventsPublisher.sendLendingReturnedFinal(lending.get());

            return lendingRepository.save(lending.get());
        }

        lendingEventsPublisher.sendLendingFailed(resource.getRecommendationNumber());

        return null;
    }

    @Override
    public Lending updateLendingRecommendationFailed(LendingViewAMQP resource) {
        Lending lending = findByLendingNumber(resource.getLendingNumber())
                .orElseThrow(() -> new NotFoundException("Lending not found: " + resource.getLendingNumber()));

        lending.setReturnedDate(null);
        lending.setCommentary(null);

        return lendingRepository.save(lending);
    }

    private Lending createLending(LendingViewAMQP lendingViewAMQP, String entityType) {
        final String lendingNumber = lendingViewAMQP.getLendingNumber();
        final String isbn = lendingViewAMQP.getIsbn();
        final String readerNumber = lendingViewAMQP.getReaderDetailsId();
        final int status = lendingViewAMQP.getStatus();

        // Retorna se o status for LENDING_INVALIDATED
        if (status == LendingStatus.LENDING_INVALIDATED) {
            lendingRepository.findByLendingNumber(lendingNumber)
                    .ifPresent(lending -> {
                        lendingRepository.delete(lending);
                        System.out.println("Lending with LendingNumber " + lendingNumber +
                                " has been deleted, due to an invalid " + entityType +
                                " provided upon Lending validation. (Lending)");
                    });
            System.out.println("No operation performed because the " + entityType + " is invalid.");
            return null;
        }

        // Processa caso o status seja válido
        return lendingRepository.findByLendingNumber(lendingNumber)
                .map(lending -> {
                    lending.setValidated(status);
                    System.out.println("Lending with LendingNumber " + lendingNumber + " already exists");
                    return lendingRepository.save(lending);
                })
                .orElseGet(() -> {
                    Lending newLending = LendingFactory.create(
                            lendingNumber, isbn, readerNumber,
                            lendingDurationInDays, fineValuePerDayInCents, status);
                    if ("Book".equals(entityType)) {
                        System.out.println("Publishing event for new Lending with LendingNumber " + lendingNumber);
                        lendingEventsPublisher.sendLendingCreatedToReader(newLending);
                    }
                    return lendingRepository.save(newLending);
                });
    }


    @Override
    public Lending setReturned(final String lendingNumber, final SetLendingReturnedRequest resource) {

        var lending = lendingRepository.findByLendingNumber(lendingNumber)
                .orElseThrow(() -> new NotFoundException("Cannot update lending with this lending number"));

        lending.setReturned(resource.getCommentary());

        if (lending.getDaysDelayed() > 0) {
            final var fine = new Fine(lending);
            fineRepository.save(fine);
        }

        lendingEventsPublisher.sendLendingReturned(lending);

        return lendingRepository.save(lending);
    }

    @Override
    public Lending setReturned(LendingViewAMQP lendingViewAMQP) {

        final String lendingNumber = lendingViewAMQP.getLendingNumber();
        final String commentary = lendingViewAMQP.getCommentary();

        return setReturned(lendingNumber, commentary);
    }


    private Lending setReturned(String lendingNumber,
                                String commentary) {

        var lending = lendingRepository.findByLendingNumber(lendingNumber)
                .orElseThrow(() -> new NotFoundException("Cannot update lending with this lending number"));

        lending.setReturned(commentary);

        if (lending.getDaysDelayed() > 0) {
            final var fine = new Fine(lending);
            fineRepository.save(fine);
        }

        return lendingRepository.save(lending);
    }


    @Override
    public Optional<Lending> update(LendingViewAMQP lendingViewAMQP) {
        Lending lending = lendingRepository.findByLendingNumber(lendingViewAMQP.getLendingNumber())
                .orElseThrow(() -> new NotFoundException("Lending not found with number: " + lendingViewAMQP.getLendingNumber()));

        if (lendingViewAMQP.getStatus() == LendingStatus.LENDING_INVALIDATED) {
            lendingRepository.delete(lending);
            return Optional.empty();
        }

        lending.setValidated(LendingStatus.LENDING_VALIDATED_BOOKS);

        return Optional.of(lendingRepository.save(lending));
    }


}
