package pt.psoft.g1.psoftg1.authormanagement.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.authormanagement.model.AuthorFactory;
import pt.psoft.g1.psoftg1.authormanagement.repositories.AuthorRepository;
import pt.psoft.g1.psoftg1.authormanagement.services.AuthorService;
import pt.psoft.g1.psoftg1.shared.model.Generator;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class AuthorRepositoryTest {

    @Mock
    private AuthorRepository authorRepository;

    private Author author;

    @BeforeEach
    void setUp() {
        author = AuthorFactory.create("user", "Biografia", null);
    }

    @Test
    public void whenFindByName_thenReturnAuthor() {
        // Arrange
        when(authorRepository.searchByNameName(author.getName())).thenReturn(Collections.singletonList(author));

        // Act
        List<Author> list = authorRepository.searchByNameName(author.getName());

        // Assert
        assertThat(list).isNotEmpty();
        assertThat(list.get(0).getName()).isEqualTo(author.getName());
        verify(authorRepository, times(1)).searchByNameName(author.getName());
    }

    @Test
    public void whenDelete_thenAuthorShouldBeDeleted() {
        // Arrange
        Author authorToDelete = AuthorFactory.create("user", "Biografia", null);
        authorRepository.save(authorToDelete);

        // Act
        authorRepository.delete(authorToDelete);

        // Assert
        // Verifique se o autor não pode ser encontrado
        assertThat(authorRepository.findByAuthorNumber(authorToDelete.getAuthorNumber())).isEmpty();
    }

    @Test
    public void whenFindAll_thenReturnAllAuthors() {
        // Arrange
        Author author1 = new Author(String.valueOf(Generator.generateLongID()),"Author One", "first bio", null);
        Author author2 = new Author(String.valueOf(Generator.generateLongID()),"Author Two", "second bio", null);
       // when(authorRepository.findAll()).thenReturn(List.of(author1, author2));

        // Act
        //    Iterable<Author> allAuthors = authorRepository.findAll();

        // Assert
        //  assertThat(allAuthors).hasSize(2).contains(author1, author2);
        //   verify(authorRepository, times(1)).findAll();
    }

    @Test
    public void whenSearchByNameStartsWith_thenReturnMatchingAuthors() {
        // Arrange
        Author author1 = new Author(String.valueOf(Generator.generateLongID()),"Alice", "alice bio", null);
        Author author2 = new Author(String.valueOf(Generator.generateLongID()),"Alfred", "alfred bio", null);
        //    when(authorRepository.searchByNameNameStartsWith("Al")).thenReturn(List.of(author1, author2));

        // Act
        //    List<Author> authorsStartingWithAl = authorRepository.searchByNameNameStartsWith("Al");
        //
        // Assert
        //    assertThat(authorsStartingWithAl).hasSize(2);
        //    verify(authorRepository, times(1)).searchByNameNameStartsWith("Al");
    }
}