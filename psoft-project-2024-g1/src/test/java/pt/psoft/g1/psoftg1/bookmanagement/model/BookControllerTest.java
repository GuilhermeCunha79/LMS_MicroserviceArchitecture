package pt.psoft.g1.psoftg1.bookmanagement.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.bookmanagement.api.BookController;
import pt.psoft.g1.psoftg1.bookmanagement.api.BookView;
import pt.psoft.g1.psoftg1.bookmanagement.api.BookViewMapper;
import pt.psoft.g1.psoftg1.bookmanagement.repositories.BookRepository;
import pt.psoft.g1.psoftg1.bookmanagement.services.BookService;
import pt.psoft.g1.psoftg1.bookmanagement.services.CreateBookRequest;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;
import pt.psoft.g1.psoftg1.lendingmanagement.services.LendingService;
import pt.psoft.g1.psoftg1.readermanagement.services.ReaderService;
import pt.psoft.g1.psoftg1.shared.services.ConcurrencyService;
import pt.psoft.g1.psoftg1.shared.services.FileStorageService;
import pt.psoft.g1.psoftg1.usermanagement.services.UserService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;



@ExtendWith(SpringExtension.class)
@WebMvcTest(BookController.class)
@AutoConfigureMockMvc
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @MockBean
    private BookViewMapper bookViewMapper;

    @MockBean
    private BookRepository bookRepository;

    @MockBean
    private LendingService lendingService;

    @MockBean
    private ConcurrencyService concurrencyService;

    @MockBean
    private FileStorageService fileStorageService;

    @MockBean
    private UserService userService;

    @MockBean
    private ReaderService readerService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /*@WithMockUser(username = "user", roles = {"ADMIN"})
    @Test
    public void testCreateBook_Success() throws Exception {
        // Arrange
        String isbn = "9781234567897";
        List<Author> authors = new ArrayList<>();
        authors.add(new Author("Author1", "Biography1", null));
        Book book = BookFactory.create(isbn, "New Book Title", "New Book Description", new Genre("infantil"), authors , "photo.jpg");

        CreateBookRequest createBookRequest = new CreateBookRequest();
        createBookRequest.setTitle("New Book Title");
        createBookRequest.setDescription("New Book Description");
        createBookRequest.setGenre("infantil");
        createBookRequest.setAuthors(Collections.singletonList(1L));
        createBookRequest.setPhoto(null); // Assuming photo is optional

        given(bookService.create(createBookRequest, isbn)).willReturn(book);
        given(bookViewMapper.toBookView(book)).willReturn(new BookView());

        // Act and Assert
        mockMvc.perform(put("/api/books/{isbn}", isbn)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"New Book Title\",\"description\":\"New Book Description\"}"))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/books/" + isbn))
                .andExpect(jsonPath("$.title").value("New Book Title"))
                .andExpect(jsonPath("$.description").value("New Book Description"));
    }
    */

    @WithMockUser(username = "user", roles = {"ADMIN"})
    @Test
    public void testFindByIsbn_Success() throws Exception {
        // Arrange
        String isbn = "9781234567897";
        List<Author> authors = new ArrayList<>();
        authors.add(new Author("Author1", "Biography1", null)); // Adiciona autor sem erro
        Book book = BookFactory.create(isbn, "New Book Title", "New Book Description", new Genre("infantil"), authors , "photo.jpg");
        book.setVersion(1L);
        given(bookService.findByIsbn(isbn)).willReturn(book);
        given(bookViewMapper.toBookView(book)).willReturn(new BookView());

        // Act
        MvcResult result = mockMvc.perform(get("/api/books/{isbn}", isbn)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // Assertions
        String responseBody = result.getResponse().getContentAsString();
        assertNotNull(responseBody);
    }
/*


    @WithMockUser(username = "user", roles = {"ADMIN"})
    @Test
    public void testDeleteBookPhoto_NotFound() throws Exception {
        // Arrange
        String isbn = "9781234567897";
        List<Author> authors = new ArrayList<>(); // Inicializa a lista mutável
        authors.add(new Author("Author1", "Biography1", null)); // Adiciona autor sem erro
        Book book = BookFactory.create(isbn, "New Book Title", "New Book Description", new Genre("infantil"), authors , "photo.jpg");
        given(bookService.findByIsbn(isbn)).willReturn(book);

        // Act and Assert
        mockMvc.perform(delete("/api/books/{isbn}/photo", isbn)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

     */

    @Test
    void testFindByTitle_Success() {
        // Arrange
        String isbn = "9781234567897";
        String title = "Java Basics";
        List<Author> authors = new ArrayList<>(); // Inicializa a lista mutável
        authors.add(new Author("Author1", "Biography1", null));
        Book book = BookFactory.create(isbn, title, "New Book Description", new Genre("infantil"), authors , "photo.jpg");
        given(bookRepository.findByTitle(title)).willReturn(List.of(book)); // Simula o retorno do repositório

        // Act
        List<Book> foundBooks = bookRepository.findByTitle(title);

        // Assert
        assertThat(foundBooks).isNotEmpty(); // Verifica que a lista não está vazia
        assertThat(foundBooks.get(0).getTitle()).isEqualTo(book.getTitle()); // Verifica o título
    }


}