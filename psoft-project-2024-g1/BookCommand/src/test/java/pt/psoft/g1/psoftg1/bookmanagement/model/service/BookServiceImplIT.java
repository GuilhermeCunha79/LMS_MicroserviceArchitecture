package pt.psoft.g1.psoftg1.bookmanagement.model.service;

import lombok.RequiredArgsConstructor;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.authormanagement.model.AuthorFactory;
import pt.psoft.g1.psoftg1.authormanagement.repositories.AuthorRepository;
import pt.psoft.g1.psoftg1.bookmanagement.api.BookController;
import pt.psoft.g1.psoftg1.bookmanagement.api.BookEventRabbitmqReceiver;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.bookmanagement.repositories.BookRepository;
import pt.psoft.g1.psoftg1.bookmanagement.services.BookService;
import pt.psoft.g1.psoftg1.exceptions.ConflictException;
import pt.psoft.g1.psoftg1.exceptions.NotFoundException;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;
import pt.psoft.g1.psoftg1.genremanagement.model.GenreFactory;
import pt.psoft.g1.psoftg1.genremanagement.repositories.GenreRepository;
import pt.psoft.g1.psoftg1.shared.api.BookViewAMQP;


import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.NONE,
        classes = {BookRepository.class, BookService.class, GenreRepository.class, AuthorRepository.class}
)
@Transactional
@RequiredArgsConstructor
public class BookServiceImplIT {

    @MockBean
    public BookService bookService;


    @Autowired
    private BookRepository bookRepository;

    @Bean
    public GenreRepository genreRepository() {
        return Mockito.mock(GenreRepository.class);
    };

    @Bean
    public AuthorRepository authorRepository() {
        return Mockito.mock(AuthorRepository.class);
    };

    private Genre genre;
    private Author author1;
    private Author author2;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        genre = GenreFactory.create("Fiction");
        genreRepository().save(genre);

        author1 = AuthorFactory.create("Tiago miakel","Viveu e morreu","photo.jpg");
        author2 = AuthorFactory.create("Tiago miakell","Viveu e morrleu","photo.jlpg");
        authorRepository().save(author1);
        authorRepository().save(author2);
    }

    @Test
    public void testCreateBookSuccessfully() {
        // Arrange
        BookViewAMQP bookViewAMQP = new BookViewAMQP();
        bookViewAMQP.setIsbn("123456789");
        bookViewAMQP.setTitle("Test Book");
        bookViewAMQP.setDescription("Test Description");
        bookViewAMQP.setGenre("Fiction");
        bookViewAMQP.setAuthorIds(Arrays.asList(author1.getId().toString(), author2.getId().toString()));

        // Act
        Book createdBook = bookService.create(bookViewAMQP);

        // Assert
        assertEquals("123456789", createdBook.getIsbn());
        assertEquals("Test Book", createdBook.getTitle().getTitle());
        assertEquals("Test Description", createdBook.getDescription());
        assertEquals(genre, createdBook.getGenre());
        assertTrue(createdBook.getAuthors().contains(author1));
        assertTrue(createdBook.getAuthors().contains(author2));
    }

    @Test
    public void testCreateBookWithExistingIsbn() {
        // Arrange
        Book existingBook = new Book("9783161484100", "Existing Book", "Description", genre, List.of(author1), null);
        bookRepository.save(existingBook);

        BookViewAMQP bookViewAMQP = new BookViewAMQP();
        bookViewAMQP.setIsbn("123456789");
        bookViewAMQP.setTitle("Test Book");
        bookViewAMQP.setDescription("Test Description");
        bookViewAMQP.setGenre("Fiction");
        bookViewAMQP.setAuthorIds(Arrays.asList(author1.getId().toString(), author2.getId().toString()));

        // Act & Assert
        ConflictException thrown = assertThrows(ConflictException.class, () -> {
            bookService.create(bookViewAMQP);
        });

        assertEquals("Book with ISBN 123456789 already exists", thrown.getMessage());
    }

    @Test
    public void testCreateBookWithInvalidGenre() {
        // Arrange
        BookViewAMQP bookViewAMQP = new BookViewAMQP();
        bookViewAMQP.setIsbn("123456789");
        bookViewAMQP.setTitle("Test Book");
        bookViewAMQP.setDescription("Test Description");
        bookViewAMQP.setGenre("NonExistingGenre");
        bookViewAMQP.setAuthorIds(Arrays.asList(author1.getId().toString(), author2.getId().toString()));

        // Act & Assert
        NotFoundException thrown = assertThrows(NotFoundException.class, () -> {
            bookService.create(bookViewAMQP);
        });

        assertEquals("Genre not found", thrown.getMessage());
    }

}
