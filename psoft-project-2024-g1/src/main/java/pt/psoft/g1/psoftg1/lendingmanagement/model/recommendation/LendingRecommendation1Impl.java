package pt.psoft.g1.psoftg1.lendingmanagement.model.recommendation;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.bookmanagement.repositories.BookRepository;
import pt.psoft.g1.psoftg1.exceptions.NotFoundException;
import pt.psoft.g1.psoftg1.genremanagement.repositories.GenreRepository;
import pt.psoft.g1.psoftg1.lendingmanagement.api.LendingView;
import pt.psoft.g1.psoftg1.lendingmanagement.api.LendingViewMapper;
import pt.psoft.g1.psoftg1.lendingmanagement.model.Lending;
import pt.psoft.g1.psoftg1.lendingmanagement.model.LendingFactory;
import pt.psoft.g1.psoftg1.lendingmanagement.repositories.LendingRepository;
import pt.psoft.g1.psoftg1.lendingmanagement.services.CreateLendingRequest;
import pt.psoft.g1.psoftg1.lendingmanagement.services.LendingDTO;
import pt.psoft.g1.psoftg1.lendingmanagement.services.LendingMapper;
import pt.psoft.g1.psoftg1.lendingmanagement.services.LendingMapperImpl;
import pt.psoft.g1.psoftg1.readermanagement.model.ReaderDetails;
import pt.psoft.g1.psoftg1.readermanagement.repositories.ReaderRepository;

import java.util.ArrayList;
import java.util.List;

@Component("alg1")
@Primary
public class LendingRecommendation1Impl implements LendingRecommendation {

    private final BookRepository bookRepository;
    private LendingRepository lendingRepository;
    private final ReaderRepository readerRepository;
    @Value("${fineValuePerDayInCents}")
    private int fineValuePerDayInCents;
    @Value("${lendingDurationInDays}")
    private int lendingDurationInDays;
    @Value("${universal.lendingRecommendation.x}")
    private int x;
    @Value("${universal.lendingRecommendation.y}")
    private int y;

    private final LendingViewMapper lendingViewMapper;

    public LendingRecommendation1Impl(BookRepository bookRepository, ReaderRepository readerRepository, LendingRepository lendingRepository, LendingViewMapper lendingViewMapper) {
        this.bookRepository = bookRepository;
        this.readerRepository = readerRepository;
        this.lendingRepository=lendingRepository;
        this.lendingViewMapper=lendingViewMapper;
    }

    /* CORRECT
        public LendingRecommendation1Impl(@Value("${universal.repository.type}") String repositoryType,
                                      ApplicationContext context, BookRepository bookRepository, GenreRepository genreRepository) {
       // this.bookRepository = context.getBean(repositoryType, BookRepository.class);
       // this.genreRepository = context.getBean(repositoryType, GenreRepository.class);
    }
     */

    @Override
    public Iterable<LendingView> bookRecommendation(final CreateLendingRequest resource) {
        List<LendingView> lendingList = new ArrayList<>();

        List<Book> books = this.bookRepository.findXBooksByYGenre(x, y);

        final var r = readerRepository.findByReaderNumber(resource.getReaderNumber())
                .orElseThrow(() -> new NotFoundException("Reader not found"));

        books.forEach(book -> {
            Lending newLending = LendingFactory.create(
                    book,
                    r,
                    lendingRepository.getCountFromCurrentYear()+1,
                    lendingDurationInDays,
                    fineValuePerDayInCents
            );
            lendingList.add(lendingViewMapper.toLendingView(newLending));
        });

        return lendingList;
    }
}
