package pt.psoft.g1.psoftg1.genremanagement.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class GenreMongoTest {

    @Test
    void ensureGenreMustNotBeNull() {
        assertThrows(IllegalArgumentException.class, () -> GenreFactory.createMongo(null));
    }

    @Test
    void ensureGenreMustNotBeBlank() {
        assertThrows(IllegalArgumentException.class, () -> GenreFactory.createMongo(""));
    }

    @Test
    void ensureGenreIsSet() {
        final var genre = GenreFactory.createMongo("Some genre");
        assertEquals("Some genre", genre.toString());
    }

}
