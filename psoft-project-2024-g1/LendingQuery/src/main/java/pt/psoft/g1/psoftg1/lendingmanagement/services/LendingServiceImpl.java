package pt.psoft.g1.psoftg1.lendingmanagement.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import pt.psoft.g1.psoftg1.exceptions.LendingForbiddenException;
import pt.psoft.g1.psoftg1.exceptions.NotFoundException;
import pt.psoft.g1.psoftg1.lendingmanagement.api.LendingViewAMQP;
import pt.psoft.g1.psoftg1.lendingmanagement.model.Fine;
import pt.psoft.g1.psoftg1.lendingmanagement.model.Lending;
import pt.psoft.g1.psoftg1.lendingmanagement.model.LendingFactory;
import pt.psoft.g1.psoftg1.lendingmanagement.model.LendingStatus;
import pt.psoft.g1.psoftg1.lendingmanagement.publishers.LendingEventsPublisher;
import pt.psoft.g1.psoftg1.lendingmanagement.repositories.FineRepository;
import pt.psoft.g1.psoftg1.lendingmanagement.repositories.LendingRepository;
import pt.psoft.g1.psoftg1.shared.model.generateID.GenerateIDService;
import pt.psoft.g1.psoftg1.shared.services.Page;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@PropertySource({"classpath:config/library.properties"})
public class LendingServiceImpl implements LendingService {
    private final LendingRepository lendingRepository;
    private final FineRepository fineRepository;
    private final LendingEventsPublisher lendingEventsPublisher;

    @Value("${lendingDurationInDays}")
    private int lendingDurationInDays;
    @Value("${fineValuePerDayInCents}")
    private int fineValuePerDayInCents;

    //AMQP
    @Override
    public Lending create(LendingViewAMQP lendingViewAMQP) {
        return createLending(lendingViewAMQP, "Book");
    }

    @Override
    public Lending createReader(LendingViewAMQP lendingViewAMQP) {
        return createLending(lendingViewAMQP, "Reader");
    }

    private Lending createLending(LendingViewAMQP lendingViewAMQP, String entityType) {
        final String lendingNumber = lendingViewAMQP.getLendingNumber();
        final String isbn = lendingViewAMQP.getIsbn();
        final String readerNumber = lendingViewAMQP.getReaderDetailsId();
        final int status = lendingViewAMQP.getStatus();

        // Retorna imediatamente se o status for LENDING_INVALIDATED
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

    //HTTP
    @Override
    public Optional<Lending> findByLendingNumber(String lendingNumber) {
        return lendingRepository.findByLendingNumber(lendingNumber);
    }

    @Override
    public List<Lending> listByReaderNumberAndIsbn(String readerNumber, String isbn, Optional<Boolean> returned) {
        List<Lending> lendings = lendingRepository.listByReaderNumberAndIsbn(readerNumber, isbn);
        if (returned.isEmpty()) {
            return lendings;
        } else {
            for (int i = 0; i < lendings.size(); i++) {
                if ((lendings.get(i).getReturnedDate() == null) == returned.get()) {
                    lendings.remove(i);
                    i--;
                }
            }
        }
        return lendings;
    }
    @Override
    public Double getAverageDuration() {
        Double avg = lendingRepository.getAverageDuration();
        return Double.valueOf(String.format(Locale.US, "%.1f", avg));
    }

    @Override
    public List<Lending> getOverdue(Page page) {
        if (page == null) {
            page = new Page(1, 10);
        }
        return lendingRepository.getOverdue(page);
    }

    @Override
    public Double getAvgLendingDurationByIsbn(String isbn) {
        Double avg = lendingRepository.getAvgLendingDurationByIsbn(isbn);
        return Double.valueOf(String.format(Locale.US, "%.1f", avg));
    }

    @Override
    public List<Lending> searchLendings(Page page, SearchLendingQuery query) {
        LocalDate startDate = null;
        LocalDate endDate = null;

        if (page == null) {
            page = new Page(1, 10);
        }
        if (query == null)
            query = new SearchLendingQuery("",
                    "",
                    null,
                    LocalDate.now().minusDays(10L).toString(),
                    null);

        try {
            if (query.getStartDate() != null)
                startDate = LocalDate.parse(query.getStartDate());
            if (query.getEndDate() != null)
                endDate = LocalDate.parse(query.getEndDate());
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Expected format is YYYY-MM-DD");
        }

        return lendingRepository.searchLendings(page,
                query.getReaderNumber(),
                query.getIsbn(),
                query.getReturned(),
                startDate,
                endDate);

    }


}
