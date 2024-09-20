package pt.psoft.g1.psoftg1.genremanagement.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pt.psoft.g1.psoftg1.bookmanagement.services.GenreBookCountDTO;
import pt.psoft.g1.psoftg1.exceptions.NotFoundException;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;
import pt.psoft.g1.psoftg1.genremanagement.repositories.GenreRepository;
import pt.psoft.g1.psoftg1.shared.services.Page;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;


    public Optional<Genre> findByString(String name) {
        return genreRepository.findByString(name);
    }

    @Override
    public Iterable<Genre> findAll() {
        return genreRepository.findAll();
    }

    @Override
    public List<GenreBookCountDTO> findTopGenreByBooks(){
        Pageable pageableRules = PageRequest.of(0,5);
        return this.genreRepository.findTop5GenreByBookCount(pageableRules).getContent();
    }

    @Override
    public Genre save(Genre genre) {
        return this.genreRepository.save(genre);
    }

    @Override
    public List<GenreLendingsPerMonthDTO> getLendingsPerMonthLastYearByGenre() {
        return genreRepository.getLendingsPerMonthLastYearByGenre();
    }

    @Override
    public List<GenreLendingsDTO> getAverageLendings(GetAverageLendingsQuery query, Page page){
        if (page == null)
            page = new Page(1, 10);

        final var month = LocalDate.of(query.getYear(), query.getMonth(), 1);

        return genreRepository.getAverageLendingsInMonth(month, page);
    }

    @Override
    public List<GenreLendingsPerMonthDTO> getLendingsAverageDurationPerMonth(String start, String end){
        LocalDate startDate;
        LocalDate endDate;

        try {
            startDate = LocalDate.parse(start);
            endDate = LocalDate.parse(end);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Expected format is YYYY-MM-DD");
        }

        if(startDate.isAfter(endDate))
            throw new IllegalArgumentException("Start date cannot be after end date");

        final var list = genreRepository.getLendingsAverageDurationPerMonth(startDate, endDate);

        if (list.isEmpty())
            throw new NotFoundException("No objects match the provided criteria");

        return list;
    }
}
