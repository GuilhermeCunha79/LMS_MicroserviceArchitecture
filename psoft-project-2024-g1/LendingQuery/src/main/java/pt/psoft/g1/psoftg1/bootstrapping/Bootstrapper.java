package pt.psoft.g1.psoftg1.bootstrapping;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import pt.psoft.g1.psoftg1.lendingmanagement.model.Lending;
import pt.psoft.g1.psoftg1.lendingmanagement.repositories.LendingRepository;
import pt.psoft.g1.psoftg1.shared.repositories.PhotoRepository;
import pt.psoft.g1.psoftg1.shared.services.ForbiddenNameService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Profile("bootstrap")
@PropertySource({"classpath:config/library.properties"})
@Order(2)
public class Bootstrapper implements CommandLineRunner {
    @Value("${lendingDurationInDays}")
    private int lendingDurationInDays;
    @Value("${fineValuePerDayInCents}")
    private int fineValuePerDayInCents;

    private final LendingRepository lendingRepository;
    private final PhotoRepository photoRepository;

    private final ForbiddenNameService forbiddenNameService;

    @Override
    public void run(final String... args) {

            loadForbiddenNames();
            createLendings();
    }


    protected void loadForbiddenNames() {
        String fileName = "forbiddenNames.txt";
        forbiddenNameService.loadDataFromFile(fileName);
    }
    private void createLendings() {
        int i;
        int seq = 0;

        List<String> books = new ArrayList<>();

        books.add("9789720706386");
        books.add("9789723716160");
        books.add("9789895612864");
        books.add("9782722203402");
        books.add("9789722328296");
        books.add("9789895702756");
        books.add("9789897776090");
        books.add("9789896379636");
        books.add("9789896378905");
        books.add("9789896375225");

        List<String> readerNumbers = new ArrayList<>();

        readerNumbers.add("2024/1");
        readerNumbers.add("2024/2");
        readerNumbers.add("2024/3");
        readerNumbers.add("2024/4");
        readerNumbers.add("2024/5");
        readerNumbers.add("2024/6");

        LocalDate startDate;
        LocalDate returnedDate;
        Lending lending;

        //Lendings 1 through 3 (late, returned)
        for (i = 0; i < 3; i++) {
            ++seq;
            if (lendingRepository.findByLendingNumber("2024/" + seq).isEmpty()) {
                startDate = LocalDate.of(2024, 1, 31 - i);
                returnedDate = LocalDate.of(2025, 2, 15 + i);
                lending = Lending.newBootstrappingLending(books.get(i), readerNumbers.get(i * 2), 2024, seq, startDate, returnedDate, lendingDurationInDays, fineValuePerDayInCents);
                lendingRepository.save(lending);
            }
        }

        //Lendings 4 through 6 (overdue, not returned)
        for (i = 0; i < 3; i++) {
            ++seq;
            if (lendingRepository.findByLendingNumber("2024/" + seq).isEmpty()) {
                startDate = LocalDate.of(2024, 3, 25 + i);
                lending = Lending.newBootstrappingLending(books.get(1 + i), readerNumbers.get(1 + i * 2), 2024, seq, startDate, null, lendingDurationInDays, fineValuePerDayInCents);
                lendingRepository.save(lending);
            }
        }
        //Lendings 7 through 9 (late, overdue, not returned)
        for (i = 0; i < 3; i++) {
            ++seq;
            if (lendingRepository.findByLendingNumber("2024/" + seq).isEmpty()) {
                startDate = LocalDate.of(2024, 4, (1 + 2 * i));
                lending = Lending.newBootstrappingLending(books.get(3 / (i + 1)), readerNumbers.get(i * 2), 2024, seq, startDate, null, lendingDurationInDays, fineValuePerDayInCents);
                lendingRepository.save(lending);
            }
        }

        //Lendings 10 through 12 (returned)
        for (i = 0; i < 3; i++) {
            ++seq;
            if (lendingRepository.findByLendingNumber("2024/" + seq).isEmpty()) {
                startDate = LocalDate.of(2024, 5, (i + 1));
                returnedDate = LocalDate.of(2025, 5, (i + 2));
                lending = Lending.newBootstrappingLending(books.get(3 - i), readerNumbers.get(1 + i * 2), 2024, seq, startDate, returnedDate, lendingDurationInDays, fineValuePerDayInCents);
                lendingRepository.save(lending);
            }
        }

        //Lendings 13 through 18 (returned)
        for (i = 0; i < 6; i++) {
            ++seq;
            if (lendingRepository.findByLendingNumber("2024/" + seq).isEmpty()) {
                startDate = LocalDate.of(2024, 5, (i + 2));
                returnedDate = LocalDate.of(2025, 5, (i + 2 * 2));
                lending = Lending.newBootstrappingLending(books.get(i), readerNumbers.get(i), 2024, seq, startDate, returnedDate, lendingDurationInDays, fineValuePerDayInCents);
                lendingRepository.save(lending);
            }
        }

        //Lendings 19 through 23 (returned)
        for (i = 0; i < 6; i++) {
            ++seq;
            if (lendingRepository.findByLendingNumber("2024/" + seq).isEmpty()) {
                startDate = LocalDate.of(2024, 5, (i + 8));
                returnedDate = LocalDate.of(2025, 5, (2 * i + 8));
                lending = Lending.newBootstrappingLending(books.get(i), readerNumbers.get(1 + i % 4), 2024, seq, startDate, returnedDate, lendingDurationInDays, fineValuePerDayInCents);
                lendingRepository.save(lending);
            }
        }

        //Lendings 24 through 29 (returned)
        for (i = 0; i < 6; i++) {
            ++seq;
            if (lendingRepository.findByLendingNumber("2024/" + seq).isEmpty()) {
                startDate = LocalDate.of(2024, 5, (i + 18));
                returnedDate = LocalDate.of(2025, 5, (2 * i + 18));
                lending = Lending.newBootstrappingLending(books.get(i), readerNumbers.get(i % 2 + 2), 2024, seq, startDate, returnedDate, lendingDurationInDays, fineValuePerDayInCents);
                lendingRepository.save(lending);
            }
        }

        //Lendings 30 through 35 (not returned, not overdue)
        for (i = 0; i < 6; i++) {
            ++seq;
            if (lendingRepository.findByLendingNumber("2024/" + seq).isEmpty()) {
                startDate = LocalDate.of(2024, 6, (i / 3 + 1));
                lending = Lending.newBootstrappingLending(books.get(i), readerNumbers.get(i % 2 + 3), 2024, seq, startDate, null, lendingDurationInDays, fineValuePerDayInCents);
                lendingRepository.save(lending);
            }
        }

        //Lendings 36 through 45 (not returned, not overdue)
        for (i = 0; i < 10; i++) {
            ++seq;
            if (lendingRepository.findByLendingNumber("2024/" + seq).isEmpty()) {
                startDate = LocalDate.of(2024, 6, (2 + i / 4));
                lending = Lending.newBootstrappingLending(books.get(i), readerNumbers.get(4 - i % 4), 2024, seq, startDate, null, lendingDurationInDays, fineValuePerDayInCents);
                lendingRepository.save(lending);
            }
        }
    }
}


