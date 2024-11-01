package pt.psoft.g1.psoftg1.authormanagement.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.request.WebRequest;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.authormanagement.services.AuthorService;
import pt.psoft.g1.psoftg1.authormanagement.services.CreateAuthorRequest;
import pt.psoft.g1.psoftg1.authormanagement.services.UpdateAuthorRequest;
import pt.psoft.g1.psoftg1.bookmanagement.api.BookViewMapper;
import pt.psoft.g1.psoftg1.exceptions.NotFoundException;
import pt.psoft.g1.psoftg1.shared.services.ConcurrencyService;
import pt.psoft.g1.psoftg1.shared.services.FileStorageService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/*
@SpringBootTest
@AutoConfigureMockMvc
class AuthorControllerTest {

    @Autowired
    private MockMvc mockMvc;


    private AuthorController authorController;



    @BeforeEach
    void setup() {
        FileStorageService fileStorageService = mock(FileStorageService.class);
        authorController = new AuthorController(authorService, mock(AuthorViewMapper.class),
                mock(ConcurrencyService.class), fileStorageService, mock(BookViewMapper.class));
    }

    @Test
    @WithMockUser(username = "ADMIN", roles = {"ADMIN"})
    void whenCreateAuthor_thenReturnsCreatedStatus() throws Exception {
        // Arrange

        CreateAuthorRequest request = new CreateAuthorRequest();
        Author authorDouble = mock(Author.class);

        when(authorService.create(any(CreateAuthorRequest.class))).thenReturn(authorDouble);

        AuthorController controller = new AuthorController(authorService, mock(AuthorViewMapper.class),
                mock(ConcurrencyService.class), mock(FileStorageService.class), mock(BookViewMapper.class));

        // Act
        ResponseEntity<?> response = controller.create(request);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }







    @Test
    void whenUpdateNonExistentAuthor_thenThrowsNotFoundException() {
        // Arrange
        Long nonExistentAuthorId = 999L;
        UpdateAuthorRequest request = mock(UpdateAuthorRequest.class);

        // Adicionar um valor para o If-Match no teste para evitar o erro de 400
        WebRequest webRequest = mock(WebRequest.class);
        when(webRequest.getHeader("If-Match")).thenReturn("*");

        // Simular que o autor não existe
        when(authorService.partialUpdate(eq(nonExistentAuthorId.toString()), eq(request), anyLong()))
                .thenThrow(new NotFoundException("Cannot update an object that does not yet exist"));

        // Act + Assert
        NotFoundException exception = assertThrows(NotFoundException.class, () ->
                authorController.partialUpdate(nonExistentAuthorId, webRequest, request)
        );

        // Assert
        assertEquals("Cannot update an object that does not yet exist", exception.getMessage());
    }

    @Test
    void whenFindExistingAuthor_thenReturnsAuthor() {
        // Arrange
        Long authorId = 1L;
        Author author = mock(Author.class);
        when(authorService.findByAuthorNumber(String.valueOf(authorId))).thenReturn(Optional.of(author));

        // Act
        ResponseEntity<?> response = authorController.findByAuthorNumber(authorId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void whenGetNonExistentPhoto_thenReturnsOkWithoutBody() {
        // Arrange
        Long authorId = 1L;
        Author author = mock(Author.class);
        when(authorService.findByAuthorNumber(String.valueOf(authorId))).thenReturn(Optional.of(author));
        when(author.getPhoto()).thenReturn(null);

        // Act
        ResponseEntity<byte[]> response = authorController.getSpecificAuthorPhoto(authorId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());
    }
}*/