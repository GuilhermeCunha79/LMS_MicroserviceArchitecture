package pt.psoft.g1.psoftg1.bookmanagement.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.psoft.g1.psoftg1.authormanagement.model.AuthorFactory;
import pt.psoft.g1.psoftg1.authormanagement.model.AuthorMongo;
import pt.psoft.g1.psoftg1.genremanagement.model.GenreFactory;
import pt.psoft.g1.psoftg1.genremanagement.model.GenreMongo;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BookMongoTest {

    private final String validIsbn = "9782826012092";
    private final String validTitle = "Encantos de contar";
    private final AuthorMongo validAuthor1 = AuthorFactory.createMongo("João Alberto", "O João Alberto nasceu em Chaves e foi pedreiro a maior parte da sua vida.", null);
    private final AuthorMongo validAuthor2 = AuthorFactory.createMongo("Maria José", "A Maria José nasceu em Viseu e só come laranjas às segundas feiras.", null);
    private final GenreMongo validGenre = GenreFactory.createMongo("Fantasia");
    private final List<AuthorMongo> authors = new ArrayList<>();

    @BeforeEach
    void setUp(){
        authors.clear();
    }
    @Test
    void ensureAuthorsNotNull(){
        authors.add(validAuthor1);
        assertThrows(IllegalArgumentException.class, () -> BookFactory.createMongo(validIsbn, validTitle, null, validGenre, null, null));
    }

    @Test
    void ensureAuthorsNotEmpty(){
        assertThrows(IllegalArgumentException.class, () -> BookFactory.createMongo(validIsbn, validTitle, null, validGenre, authors, null));
    }

    @Test
    void ensureBookCreatedWithMultipleAuthors() {
        authors.add(validAuthor1);
        authors.add(validAuthor2);
        assertDoesNotThrow(() -> BookFactory.createMongo(validIsbn, validTitle, "null", validGenre, authors, null));
    }

}
