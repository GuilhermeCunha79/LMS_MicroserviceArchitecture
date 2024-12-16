package pt.psoft.g1.psoftg1.bookmanagement.model;


import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.authormanagement.model.AuthorFactory;
import pt.psoft.g1.psoftg1.bookmanagement.services.UpdateBookRequest;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;
import pt.psoft.g1.psoftg1.genremanagement.model.GenreFactory;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookTest {
    private final String validIsbn = "9782826012092";
    private final String validTitle = "Encantos de contar";
    private final Author validAuthor1 = AuthorFactory.create("João Alberto", "O João Alberto nasceu em Chaves e foi pedreiro a maior parte da sua vida.", null);
    private final Author validAuthor2 = AuthorFactory.create("Maria José", "A Maria José nasceu em Viseu e só come laranjas às segundas feiras.", null);
    private final Genre validGenre = GenreFactory.create("Fantasia");
    private final ArrayList<Author> authors = new ArrayList<>();

    @BeforeEach
    void setUp(){
        authors.clear();
    }

    @Test
    void ensureIsbnNotNull(){
        authors.add(validAuthor1);
        assertThrows(IllegalArgumentException.class, () -> BookFactory.create(null, validTitle, null, validGenre, authors, null));
    }
/*

    //white box unit
    @Test
    @Transactional
    void verifyPatchApplicationBehavior_WhenNoChangesMadeToValidParameters() {
        try (MockedConstruction<Title> mockedTitle = mockConstruction(Title.class);
             MockedConstruction<Isbn> mockedIsbn = mockConstruction(Isbn.class);
             MockedConstruction<Description> mockedDescription = mockConstruction(Description.class)) {

            // Arrange
            String isbn = "1234567890123";
            String title = "Banana Selvagem";
            String description = "Texto aleatório sobre uma fruta";
            Genre genre = mock(Genre.class);
            Author author = AuthorFactory.create("Zé das Couves", "Zé é um famoso cozinheiro", null);
            List<Author> authors = Collections.singletonList(author);
            String photoUri = "imagem_aleatoria.png";

            UpdateBookRequest updateRequest = mock(UpdateBookRequest.class);
            when(updateRequest.getDescription()).thenReturn(null);
            when(updateRequest.getAuthorObjList()).thenReturn(null);
            when(updateRequest.getGenreObj()).thenReturn(null);
            when(updateRequest.getPhotoURI()).thenReturn(null);

            Book book = BookFactory.create(isbn, title, description, genre, authors, photoUri);

            // Act
            book.applyPatch(0L,updateRequest);

            // Assert: verify interactions
            verify(updateRequest, times(1)).getDescription();
            verify(updateRequest, times(1)).getGenreObj();
            verify(updateRequest, times(1)).getAuthorObjList();
            verify(updateRequest, times(1)).getPhotoURI();

            // Assert: ensure state remains unchanged
            assertEquals(isbn, book.getIsbn());
            assertEquals(title, book.getTitle());
            assertEquals(description, book.getDescription());
            assertEquals(authors, book.getAuthors());
            assertEquals(genre, book.getGenre());
            assertEquals(photoUri, book.getPhoto().getPhotoFile());
        }
    }



    @Test
    void verifyPatchApplicationBehavior_WhenChangingValidParameters() {
        try (MockedConstruction<Title> mockedTitle = mockConstruction(Title.class);
             MockedConstruction<Isbn> mockedIsbn = mockConstruction(Isbn.class);
             MockedConstruction<Description> mockedDescription = mockConstruction(Description.class)) {

            String isbn = "1234567890123";
            String title = "Banana Selvagem";
            String description = "Texto aleatório sobre uma fruta";
            Genre genre = mock(Genre.class);
            Author author = AuthorFactory.create("Zé das Couves", "Zé é um famoso cozinheiro", null);
            List<Author> authors = Collections.singletonList(author);
            String photoUri = "imagem_aleatoria.png";

            UpdateBookRequest updateRequest = mock(UpdateBookRequest.class);
            Book book = new Book(null, null, "", genre, authors, "null");

            when(mockedIsbn.constructed().get(0).toString()).thenReturn(isbn);
            when(mockedTitle.constructed().get(0).toString()).thenReturn(title);
            when(genre.toString()).thenReturn("Ficção");
            when(mockedDescription.constructed().get(0).toString()).thenReturn(description);
            when(updateRequest.getAuthorObjList()).thenReturn(null);

            verify(updateRequest).getTitle();
            verify(updateRequest).getDescription();
            verify(updateRequest).getGenreObj();
            verify(updateRequest).getAuthorObjList();
            verify(updateRequest).getPhotoURI();

            assertEquals(genre, book.getGenre());
            assertEquals(isbn, book.getIsbn());
            assertEquals(title, book.getTitle().toString());
            assertEquals(authors, book.getAuthors());
            assertEquals(description, book.getDescription());
        }
    }*/


    @Test
    void ensureTitleNotNull(){
        authors.add(validAuthor1);
        assertThrows(IllegalArgumentException.class, () -> BookFactory.create(validIsbn, null, null, validGenre, authors, null));
    }

    @Test
    void ensureGenreNotNull(){
        authors.add(validAuthor1);
        assertThrows(IllegalArgumentException.class, () -> BookFactory.create(validIsbn, validTitle, null,null, authors, null));
    }

    @Test
    void ensureAuthorsNotNull(){
        authors.add(validAuthor1);
        assertThrows(IllegalArgumentException.class, () -> BookFactory.create(validIsbn, validTitle, null, validGenre, null, null));
    }

    @Test
    void ensureAuthorsNotEmpty(){
        assertThrows(IllegalArgumentException.class, () -> BookFactory.create(validIsbn, validTitle, null, validGenre, authors, null));
    }

    @Test
    void ensureBookCreatedWithMultipleAuthors() {
        authors.add(validAuthor1);
        authors.add(validAuthor2);
        assertDoesNotThrow(() -> BookFactory.create(validIsbn, validTitle, null, validGenre, authors, null));
    }

}