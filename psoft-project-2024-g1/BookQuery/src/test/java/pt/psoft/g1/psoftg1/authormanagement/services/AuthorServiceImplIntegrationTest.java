package pt.psoft.g1.psoftg1.authormanagement.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.authormanagement.repositories.AuthorRepository;
import pt.psoft.g1.psoftg1.authormanagement.api.AuthorLendingView;
import pt.psoft.g1.psoftg1.exceptions.NotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;
/*
@ExtendWith(MockitoExtension.class)
public class AuthorServiceImplIntegrationTest {

    @InjectMocks
    private AuthorServiceImpl authorService;

    @Mock
    private AuthorRepository authorRepository;

    @BeforeEach
    public void setUp() {
        Author alex = new Author("Alex", "O Alex escreveu livros", null);
        alex.setAuthorNumber("1L");
        List<Author> authorList = new ArrayList<>();
        authorList.add(alex);

        // Configuração dos mocks com lenient()
        lenient().when(authorRepository.searchByNameNameStartsWith("Alex")).thenReturn(authorList);
        lenient().when(authorRepository.findByAuthorNumber("1L")).thenReturn(Optional.of(alex));
        lenient().when(authorRepository.save(alex)).thenReturn(alex);
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
        when(authorRepository.findByAuthorNumber("99L")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> authorService.partialUpdate("99L", updateRequest, 1L))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Cannot update an object that does not yet exist");
    }

    @Test
    public void whenFindCoAuthorsByAuthorNumber_thenCoAuthorsListShouldBeReturned() {
        Author coAuthor = new Author("CoAuthor", "CoAuthor bio", null);
        when(authorRepository.findCoAuthorsByAuthorNumber("1L")).thenReturn(List.of(coAuthor));

        List<Author> coAuthors = authorService.findCoAuthorsByAuthorNumber("1L");
        assertThat(coAuthors).hasSize(1);
        assertThat(coAuthors.get(0).getName()).isEqualTo("CoAuthor");
    }

    @Test
    public void whenRemovePhotoFromNonExistentAuthor_thenThrowNotFoundException() {
        when(authorRepository.findByAuthorNumber("99L")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> authorService.removeAuthorPhoto("99L", 1L))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Cannot find reader");
    }
}
*/