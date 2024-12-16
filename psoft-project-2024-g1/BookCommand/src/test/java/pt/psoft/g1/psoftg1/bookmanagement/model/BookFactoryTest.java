package pt.psoft.g1.psoftg1.bookmanagement.model;

import org.junit.Test;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.authormanagement.model.AuthorFactory;
import pt.psoft.g1.psoftg1.authormanagement.model.AuthorMongo;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;
import pt.psoft.g1.psoftg1.genremanagement.model.GenreFactory;
import pt.psoft.g1.psoftg1.genremanagement.model.GenreMongo;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class BookFactoryTest {
    @Test
    public void testCreateBook() {
        String isbn = "9782826012092";
        String title = "O Inspetor Max";
        String description = "Descrição do livro";
        Genre genre = GenreFactory.create("Infantil");
        List<Author> authors = new ArrayList<>();
        authors.add(AuthorFactory.create("Luis", "Autor de livros", "photo.jpg"));

        String photoURI = "photo.jpg";

        Book book = BookFactory.create(isbn, title, description, genre, authors, photoURI);

        assertThat(book).isNotNull();
        assertThat(book.getIsbn()).isEqualTo(isbn);
      //  assertThat(book.getTitle().title.trim()).isEqualTo(title.trim());
        assertThat(book.getDescription()).isEqualTo(description);
        assertThat(book.getGenre()).isEqualTo(genre);
        assertThat(book.getAuthors()).isEqualTo(authors);
        assert book.getPhoto() != null;
        assertThat(book.getPhoto().getPhotoFile()).isEqualTo(photoURI);
    }


    @Test
    public void testCreateBookMongo() {
        String isbn = "9782826012092";
        String title = "O Inspetor Max";
        String description = "Descrição do livro";
        GenreMongo genreMongo = GenreFactory.createMongo("Infantil");
        List<AuthorMongo> authorsMongo = new ArrayList<>();
        authorsMongo.add(AuthorFactory.createMongo("Luis", "Autor de livros", "photojpg"));

        String photoURI = null;

        BookMongo bookMongo = BookFactory.createMongo(isbn, title, description, genreMongo, authorsMongo, photoURI);

        assertThat(bookMongo).isNotNull();
        assertThat(bookMongo.getIsbn()).isEqualTo(isbn);
        assertThat(bookMongo.getTitle().trim()).isEqualTo(title.trim());
        assertThat(bookMongo.getDescription()).isEqualTo(description);
        assertThat(bookMongo.getGenre()).isEqualTo(genreMongo);
        assertThat(bookMongo.getAuthors()).isEqualTo(authorsMongo);
        assertThat(bookMongo.getPhoto()).isEqualTo(null);
    }

}
