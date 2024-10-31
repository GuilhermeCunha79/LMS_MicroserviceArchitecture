package pt.psoft.g1.psoftg1.bookmanagement.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.request.WebRequest;
import pt.psoft.g1.psoftg1.bookmanagement.api.BookController;
import pt.psoft.g1.psoftg1.bookmanagement.api.BookView;
import pt.psoft.g1.psoftg1.bookmanagement.api.BookViewMapper;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.bookmanagement.services.BookService;
import pt.psoft.g1.psoftg1.bookmanagement.services.CreateBookRequest;
import pt.psoft.g1.psoftg1.bookmanagement.services.UpdateBookRequest;
import pt.psoft.g1.psoftg1.exceptions.NotFoundException;
import pt.psoft.g1.psoftg1.lendingmanagement.services.LendingService;
import pt.psoft.g1.psoftg1.readermanagement.services.ReaderService;
import pt.psoft.g1.psoftg1.shared.services.ConcurrencyService;
import pt.psoft.g1.psoftg1.shared.services.FileStorageService;
import pt.psoft.g1.psoftg1.usermanagement.services.UserService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
/*
@SpringBootTest
@AutoConfigureMockMvc
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @MockBean
    private FileStorageService fileStorageService;

    @MockBean
    private ConcurrencyService concurrencyService;

    @MockBean
    private BookViewMapper bookViewMapper;

    private BookController bookController;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        FileStorageService fileStorageService = mock(FileStorageService.class);
        bookController = new BookController(
                bookService,
                mock(LendingService.class),
                concurrencyService,
                fileStorageService,
                mock(UserService.class),
                mock(ReaderService.class),
                bookViewMapper
        );
    }

    @Test
    @WithMockUser(username = "ADMIN", roles = {"ADMIN"})
    void whenCreateBook_thenReturnsCreatedStatus() throws Exception {
        // Arrange
        String isbn = "978-3-16-148410-0";
        CreateBookRequest request = new CreateBookRequest();
        Book bookDouble = mock(Book.class);

        when(bookService.create(any(CreateBookRequest.class), eq(isbn))).thenReturn(bookDouble);
        when(bookViewMapper.toBookView(bookDouble)).thenReturn(new BookView());

        // Act
        ResponseEntity<?> response = bookController.create(request, isbn);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    void whenUpdateNonExistentBook_thenThrowsNotFoundException() {
        String nonExistentIsbn = "999-9999999999";
        UpdateBookRequest request = mock(UpdateBookRequest.class);

        WebRequest webRequest = mock(WebRequest.class);
        when(webRequest.getHeader("If-Match")).thenReturn("*");

        // Mock do serviço para retornar null quando o livro não existe
        when(bookService.findByIsbn(nonExistentIsbn)).thenReturn(null); // Mock para encontrar livro
        when(bookService.update(eq(request), eq(nonExistentIsbn)))
                .thenThrow(new NotFoundException("Cannot update a book that does not exist"));

        // Verificação da exceção esperada
        NotFoundException exception = assertThrows(NotFoundException.class, () ->
                bookController.updateBook(nonExistentIsbn, webRequest, request)
        );

        // Verificação da mensagem da exceção
        assertEquals("Cannot update a book that does not exist", exception.getMessage());
    }

    @Test
    void whenFindExistingBook_thenReturnsBook() {
        // Arrange
        String isbn = "978-3-16-148410-0";
        Book book = mock(Book.class);

        when(bookService.findByIsbn(isbn)).thenReturn(book);

        // Act
        ResponseEntity<?> response = bookController.findByIsbn(isbn);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void whenGetNonExistentPhoto_thenReturnsOkWithoutBody() {
        // Arrange
        String isbn = "978-3-16-148410-0";
        Book book = mock(Book.class);

        when(bookService.findByIsbn(isbn)).thenReturn(book);
        when(book.getPhoto()).thenReturn(null);

        // Act
        ResponseEntity<byte[]> response = bookController.getSpecificBookPhoto(isbn);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());
    }
}*/