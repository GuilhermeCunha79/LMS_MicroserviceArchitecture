package pt.psoft.g1.psoftg1.bootstrapping;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pt.psoft.g1.psoftg1.readermanagement.model.ReaderDetails;
import pt.psoft.g1.psoftg1.readermanagement.repositories.ReaderRepository;
import pt.psoft.g1.psoftg1.usermanagement.Reader;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Profile("bootstrap")
@Order(1)
public class UserBootstrapper implements CommandLineRunner {

    private final ReaderRepository readerRepository;
    private final JdbcTemplate jdbcTemplate;
    private final List<String> queriesToExecute = new ArrayList<>();

    @Override
    @Transactional
    public void run(final String... args) {
        createReaders();
        executeQueries();
    }

    private void createReaders() {
        // Reader 1 - Manuel
        if (readerRepository.findByUsername("manuel@gmail.com").isEmpty()) {
            final Reader manuel = Reader.newReader("manuel@gmail.com", "Manuelino123!", "Manuel Sarapinto das Coives");

            String dateFormat = LocalDateTime.of(2024, 1, 20, 0, 0, 0, 0).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS"));
            String query = String.format("UPDATE PUBLIC.T_USER SET CREATED_AT = '%s' WHERE USERNAME = '%s'", dateFormat, manuel.getUsername());
            queriesToExecute.add(query);

            Optional<ReaderDetails> readerDetails1 = readerRepository.findByReaderNumber(LocalDate.now().getYear() + "/1");
            List<String> interestList = new ArrayList<>();
            interestList.add("Fantasia");
            interestList.add("Infantil");

            if (readerDetails1.isEmpty()) {
                ReaderDetails r1 = new ReaderDetails(
                        45,
                        manuel,
                        "2000-01-01",
                        "919191919",
                        true,
                        true,
                        true,
                        "readerPhotoTest.jpg",
                        interestList);
                readerRepository.save(r1);
            }
        }

        // Reader 2 - João
        if (readerRepository.findByUsername("joao@gmail.com").isEmpty()) {
            final Reader joao = Reader.newReader("joao@gmail.com", "Joaoratao!123", "João Ratao");

            String dateFormat = LocalDateTime.of(2024, 3, 20, 0, 0, 0, 0).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS"));
            String query = String.format("UPDATE PUBLIC.T_USER SET CREATED_AT = '%s' WHERE USERNAME = '%s'", dateFormat, joao.getUsername());
            queriesToExecute.add(query);

            Optional<ReaderDetails> readerDetails2 = readerRepository.findByReaderNumber(LocalDate.now().getYear() + "/2");
            if (readerDetails2.isEmpty()) {
                ReaderDetails r2 = new ReaderDetails(
                        46,
                        joao,
                        "1995-06-02",
                        "929292929",
                        true,
                        false,
                        false,
                        null,
                        null);
                readerRepository.save(r2);
            }
        }

        // Reader 3 - Pedro
        if (readerRepository.findByUsername("pedro@gmail.com").isEmpty()) {
            final Reader pedro = Reader.newReader("pedro@gmail.com", "Pedrodascenas!123", "Pedro Das Cenas");

            String dateFormat = LocalDateTime.of(2024, 1, 20, 0, 0, 0, 0).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS"));
            String query = String.format("UPDATE PUBLIC.T_USER SET CREATED_AT = '%s' WHERE USERNAME = '%s'", dateFormat, pedro.getUsername());
            queriesToExecute.add(query);

            Optional<ReaderDetails> readerDetails3 = readerRepository.findByReaderNumber(LocalDate.now().getYear() + "/3");
            if (readerDetails3.isEmpty()) {
                ReaderDetails r3 = new ReaderDetails(
                        47,
                        pedro,
                        "2001-12-03",
                        "939393939",
                        true,
                        false,
                        true,
                        null,
                        null);
                readerRepository.save(r3);
            }
        }

        // Reader 4 - Catarina
        if (readerRepository.findByUsername("catarina@gmail.com").isEmpty()) {
            final Reader catarina = Reader.newReader("catarina@gmail.com", "Catarinamartins!123", "Catarina Martins");

            String dateFormat = LocalDateTime.of(2024, 3, 20, 0, 0, 0, 0).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS"));
            String query = String.format("UPDATE PUBLIC.T_USER SET CREATED_AT = '%s' WHERE USERNAME = '%s'", dateFormat, catarina.getUsername());
            queriesToExecute.add(query);

            Optional<ReaderDetails> readerDetails4 = readerRepository.findByReaderNumber(LocalDate.now().getYear() + "/4");
            if (readerDetails4.isEmpty()) {
                ReaderDetails r4 = new ReaderDetails(
                        48,
                        catarina,
                        "2002-03-20",
                        "912345678",
                        true,
                        false,
                        true,
                        null,
                        null);
                readerRepository.save(r4);
            }
        }

        // Reader 5 - Marcelo
        if (readerRepository.findByUsername("marcelo@gmail.com").isEmpty()) {
            final Reader marcelo = Reader.newReader("marcelo@gmail.com", "Marcelosousa!123", "Marcelo Rebelo de Sousa");

            String dateFormat = LocalDateTime.of(2024, 1, 20, 0, 0, 0, 0).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS"));
            String query = String.format("UPDATE PUBLIC.T_USER SET CREATED_AT = '%s' WHERE USERNAME = '%s'", dateFormat, marcelo.getUsername());
            queriesToExecute.add(query);

            Optional<ReaderDetails> readerDetails5 = readerRepository.findByReaderNumber(LocalDate.now().getYear() + "/5");
            if (readerDetails5.isEmpty()) {
                ReaderDetails r5 = new ReaderDetails(
                        49,
                        marcelo,
                        "2000-06-03",
                        "912355678",
                        true,
                        false,
                        true,
                        null,
                        null);
                readerRepository.save(r5);
            }
        }
    }

    private void executeQueries() {
        for (String query : queriesToExecute) {
            jdbcTemplate.update(query);
        }
    }
}
