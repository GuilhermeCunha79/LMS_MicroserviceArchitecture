package pt.psoft.g1.psoftg1.genremanagement.infrastructure.repositories.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import pt.psoft.g1.psoftg1.genremanagement.api.GenreViewMapper;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;
import pt.psoft.g1.psoftg1.genremanagement.model.GenreMongo;
import pt.psoft.g1.psoftg1.genremanagement.repositories.GenreRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@Profile("mongo")
@RequiredArgsConstructor
public class NoSQLGenreRepository implements GenreRepository {

    private final MongoTemplate mongoTemplate;
    private final GenreViewMapper genreMapper;

    @Override
    public Genre save(Genre genre) {
        mongoTemplate.save(genreMapper.toGenreMongo(genre));
        return genre;
    }

    @Override
    public List<Genre> findAll() {

        List<GenreMongo> bookList = mongoTemplate.findAll(GenreMongo.class);
        return bookList.stream()
                .map(genreMapper::toGenre)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void delete(Genre genre) {
        mongoTemplate.remove(genreMapper.toGenreMongo(genre));
    }

    @Override
    public Optional<Genre> findByString(String genreName) {
        Query query = new Query(Criteria.where("genre").is(genreName));
        List<Genre> list = mongoTemplate.find(query, Genre.class);

        return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
    }
}