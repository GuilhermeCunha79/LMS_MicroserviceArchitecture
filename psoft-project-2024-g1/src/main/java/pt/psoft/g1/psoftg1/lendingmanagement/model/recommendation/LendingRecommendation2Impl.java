package pt.psoft.g1.psoftg1.lendingmanagement.model.recommendation;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.bookmanagement.repositories.BookRepository;
import pt.psoft.g1.psoftg1.lendingmanagement.api.LendingView;
import pt.psoft.g1.psoftg1.lendingmanagement.api.LendingViewMapper;
import pt.psoft.g1.psoftg1.lendingmanagement.model.Lending;
import pt.psoft.g1.psoftg1.lendingmanagement.model.LendingFactory;
import pt.psoft.g1.psoftg1.lendingmanagement.repositories.LendingRepository;
import pt.psoft.g1.psoftg1.lendingmanagement.services.CreateLendingRequest;
import pt.psoft.g1.psoftg1.readermanagement.model.ReaderDetails;
import pt.psoft.g1.psoftg1.readermanagement.repositories.ReaderRepository;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component("alg2")
public class LendingRecommendation2Impl implements LendingRecommendation {

    @Value("${fineValuePerDayInCents}")
    private int fineValuePerDayInCents;
    @Value("${lendingDurationInDays}")
    private int lendingDurationInDays;
    @Value("${universal.lendingRecommendation.x}")
    private int x;
    @Value("${universal.lendingRecommendation.minAge}")
    private int MIN_AGE;
    @Value("${universal.lendingRecommendation.adultAge}")
    private int ADULT_AGE;
    private final String CHILDREN_GENRE = "Infantil";
    private final String FANTASY_GENRE = "Fantasia";
    private final BookRepository bookRepository;
    private final LendingRepository lendingRepository;
    private final ReaderRepository readerRepository;

    private final LendingViewMapper lendingViewMapper;

    public LendingRecommendation2Impl(@Value("${reader.repository.type}") String readerRepositoryType,
                                      @Value("${book.repository.type}") String bookRepositoryType,
                                      ApplicationContext context,
                                      @Value("${lending.repository.type}") String lendingRepositoryType,
                                      LendingViewMapper lendingViewMapper) {
        this.bookRepository = context.getBean(bookRepositoryType, BookRepository.class);
        this.readerRepository = context.getBean(readerRepositoryType, ReaderRepository.class);
        this.lendingRepository = context.getBean(lendingRepositoryType, LendingRepository.class);
        this.lendingViewMapper = lendingViewMapper;
    }


    @Override
    public Iterable<LendingView> bookRecommendation(final CreateLendingRequest resource) {
        List<LendingView> lendingViewList;
        final ReaderDetails reader = readerRepository.findByReaderNumber(resource.getReaderNumber()).orElseThrow();
        int age = Period.between(reader.getBirthDate().getBirthDate(), LocalDate.now()).getYears();

        String genre = determineGenreByAge(age);
        List<Book> books = (genre != null) ? bookRepository.findXBooksByGenre(genre, x) :
                bookRepository.findTopXBooksFromMostLentGenreByReader(reader.getReaderNumber(), x);

        lendingViewList = books.stream()
                .map(book -> {
                    Lending newLending = LendingFactory.create(
                            book,
                            reader,
                            lendingRepository.getCountFromCurrentYear(),
                            lendingDurationInDays,
                            fineValuePerDayInCents
                    );
                    return lendingViewMapper.toLendingView(newLending);
                })
                .collect(Collectors.toList());

        return lendingViewList;
    }

    private String determineGenreByAge(int age) {
        if (age < MIN_AGE) {
            return CHILDREN_GENRE;
        } else if (age < ADULT_AGE) {
            return FANTASY_GENRE;
        }
        return null;
    }

}
