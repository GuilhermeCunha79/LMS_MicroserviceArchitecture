package pt.psoft.g1.psoftg1.lendingmanagement.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import pt.psoft.g1.psoftg1.exceptions.ConflictException;
import pt.psoft.g1.psoftg1.lendingmanagement.api.LendingViewAMQP;
import pt.psoft.g1.psoftg1.lendingmanagement.model.Lending;
import pt.psoft.g1.psoftg1.lendingmanagement.model.LendingFactory;
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
    private final LendingFactory lendingFactory;

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
    public Lending create(LendingViewAMQP lendingViewAMQP) {

        final String lendingNumber = lendingViewAMQP.getLendingNumber();
        final String isbn = lendingViewAMQP.getIsbn();
        final String readerNumber = lendingViewAMQP.getReaderDetailsId();

        return create(lendingNumber, isbn, readerNumber);
    }

    private Lending create(String lendingNumber,
                           String isbn, String readerNumber) {

        if (lendingRepository.findByLendingNumber(lendingNumber).isPresent()) {
            throw new ConflictException("Lending with LendingNumber " + lendingNumber + " already exists");
        }

        Lending newBook = LendingFactory.create(isbn, readerNumber, lendingRepository.getCountFromCurrentYear() + 1, lendingDurationInDays, fineValuePerDayInCents);

        return lendingRepository.save(newBook);
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
