package pt.psoft.g1.psoftg1.genremanagement.repositories;

import pt.psoft.g1.psoftg1.genremanagement.model.Genre;

import java.util.Optional;

public interface GenreRepository {

    Iterable<Genre> findAll();
    Optional<Genre> findByString(String genreName);
    Genre save(Genre genre);
    void delete(Genre genre);
}
