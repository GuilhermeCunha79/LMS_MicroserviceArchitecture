package pt.psoft.g1.psoftg1.authormanagement.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.authormanagement.repositories.AuthorRepository;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.bookmanagement.repositories.BookRepository;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;
import pt.psoft.g1.psoftg1.shared.repositories.PhotoRepository;
import pt.psoft.g1.psoftg1.authormanagement.api.AuthorLendingView;
import pt.psoft.g1.psoftg1.exceptions.NotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles({"relational","hexIdGeneration", "spring_auth"})
public class AuthorServiceImplIntegrationTest {

    @Autowired
    private AuthorService authorService;

    @MockBean
    private AuthorRepository authorRepository;

    @MockBean
    private BookRepository bookRepository;

    @MockBean
    private PhotoRepository photoRepository;

    private Author alex;

    private final Author validAuthor1 = new Author("João Alberto", "O João Alberto nasceu em Chaves e foi pedreiro a maior parte da sua vida.", null);
    private final Author validAuthor2 = new Author("Maria José", "A Maria José nasceu em Viseu e só come laranjas às segundas feiras.", null);
    private final Genre validGenre = new Genre("Fantasia");
    private List<Author> authorList;


    @BeforeEach
    public void setUp() {
        alex = new Author("Alex", "O Alex escreveu livros", null);
        alex.setAuthorNumber("1L");
        authorList = new ArrayList<>();
        authorList.add(alex);

        // Setting up mock repository responses
        Mockito.when(authorRepository.searchByNameNameStartsWith("Alex")).thenReturn(authorList);
        Mockito.when(authorRepository.findByAuthorNumber("1L")).thenReturn(Optional.of(alex));
        Mockito.when(authorRepository.save(alex)).thenReturn(alex);
    }

    @Test
    public void whenValidId_thenAuthorShouldBeFound() {
        Optional<Author> found = authorService.findByAuthorNumber("1L");
        found.ifPresent(author -> assertThat(author.getId()).isEqualTo("1L"));
    }

    @Test
    public void whenFindByName_thenAuthorsWithNameShouldBeReturned() {
        String name = "Alex";
        List<Author> foundAuthors = authorService.findByName(name);
        assertThat(foundAuthors).hasSize(1);
        assertThat(foundAuthors.get(0).getName()).isEqualTo("Alex");
    }


    @Test
    public void whenPartialUpdateNonExistingAuthor_thenThrowNotFoundException() {
        UpdateAuthorRequest updateRequest = new UpdateAuthorRequest();
        Mockito.when(authorRepository.findByAuthorNumber("99L")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> authorService.partialUpdate("99L", updateRequest, 1L))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Cannot update an object that does not yet exist");
    }

    @Test
    public void whenFindTopAuthorsByLendings_thenAuthorsListShouldBeReturned() {
        List<AuthorLendingView> topAuthors = List.of(new AuthorLendingView( "Alex", 15L));
        Mockito.when(authorRepository.findTopAuthorByLendings(Mockito.any(Pageable.class)))
                .thenReturn(new PageImpl<>(topAuthors));

        List<AuthorLendingView> foundAuthors = authorService.findTopAuthorByLendings();
        assertThat(foundAuthors).hasSize(1);
        assertThat(foundAuthors.get(0).getLendingCount()).isEqualTo(15);
    }

    @Test
    public void whenFindBooksByAuthorNumber_thenBooksListShouldBeReturned() {
        List<Book> books = List.of(new Book("9782826012092", "Sample Book","description",validGenre,authorList,null));
        Mockito.when(bookRepository.findBooksByAuthorNumber("1L")).thenReturn(books);

        List<Book> foundBooks = authorService.findBooksByAuthorNumber("1L");
        assertThat(foundBooks).hasSize(1);
        assertThat(foundBooks.get(0).getTitle().toString()).isEqualTo("Sample Book");
    }

    @Test
    public void whenFindCoAuthorsByAuthorNumber_thenCoAuthorsListShouldBeReturned() {
        Author coAuthor = new Author("CoAuthor", "CoAuthor bio", null);
        Mockito.when(authorRepository.findCoAuthorsByAuthorNumber("1L")).thenReturn(List.of(coAuthor));

        List<Author> coAuthors = authorService.findCoAuthorsByAuthorNumber("1L");
        assertThat(coAuthors).hasSize(1);
        assertThat(coAuthors.get(0).getName()).isEqualTo("CoAuthor");
    }


    @Test
    public void whenRemovePhotoFromNonExistentAuthor_thenThrowNotFoundException() {
        Mockito.when(authorRepository.findByAuthorNumber("99L")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> authorService.removeAuthorPhoto("99L", 1L))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Cannot find reader");
    }
}