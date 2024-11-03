package pt.psoft.g1.psoftg1.authormanagement.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.authormanagement.model.AuthorFactory;
import pt.psoft.g1.psoftg1.authormanagement.services.AuthorService;
import pt.psoft.g1.psoftg1.bookmanagement.api.BookView;
import pt.psoft.g1.psoftg1.bookmanagement.api.BookViewMapper;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;
import pt.psoft.g1.psoftg1.shared.services.ConcurrencyService;
import pt.psoft.g1.psoftg1.shared.services.FileStorageService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(AuthorController.class)
@AutoConfigureMockMvc
public class AuthorControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthorService authorService;

    @MockBean
    private ConcurrencyService concurrencyService;

    @MockBean
    private AuthorViewMapper authorViewMapper;

    @MockBean
    private FileStorageService fileStorageService;

    @MockBean
    private BookViewMapper bookViewMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAuthorization() throws Exception {
        // Arrange
        Long authorNumber = 1L;

        // Act and Assert
        mockMvc.perform(get("/api/authors/{authorNumber}/coauthors", authorNumber)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());


        mockMvc.perform(get("/api/authors/{authorNumber}/photo", authorNumber)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(get("/api/authors/top5")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(get("/api/authors/{authorNumber}/books", authorNumber)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(get("/api/authors")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser(username = "user", roles = {"ADMIN"})
    @Test
    public void testFindByAuthorNumber_Success() throws Exception {
        // Arrange
        String authorNumber = "1";
        Author author = AuthorFactory.create("John Doe", "A famous author", "photo.jpg");
        AuthorView authorView = new AuthorView();
        authorView.setName("John Doe");
        authorView.setBio("A famous author");
        authorView.setPhoto("photo.jpg");

        // Mock the service and mapper
        given(authorService.findByAuthorNumber(authorNumber)).willReturn(Optional.of(author));
        given(authorViewMapper.toAuthorView(author)).willReturn(authorView);

        // Act
        MvcResult result = mockMvc.perform(get("/api/authors/{authorNumber}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // Extract response body
        String responseBody = result.getResponse().getContentAsString();

        // Assertions
        assertNotNull(responseBody);
        assertTrue(responseBody.contains("John Doe"));
        assertTrue(responseBody.contains("A famous author"));
        assertTrue(responseBody.contains("photo.jpg"));
    }


    @WithMockUser(username = "user", roles = {"ADMIN"})
    @Test
    public void testFindByAuthorNumber_NotFound() throws Exception {
        // Arrange
        String authorNumber = "1L";
        given(authorService.findByAuthorNumber(authorNumber)).willReturn(Optional.empty());

        // Act and Assert
        mockMvc.perform(get("/api/author/{authorNumber}", authorNumber)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @WithMockUser(username = "user", roles = {"ADMIN"})
    @Test
    public void testFindByName_Success() throws Exception {
        // Arrange
        String authorName = "John";
        AuthorView authorView = new AuthorView();
        authorView.setName("John Doe");
        authorView.setBio("A famous author");
        authorView.setPhoto("photo.jpg");

        List<AuthorView> views = new ArrayList<>();
        views.add(authorView);

        List<Author> authors = Collections.singletonList(new Author("John Doe", "A famous author", "photo.jpg"));

        given(authorService.findByName(authorName)).willReturn(authors);
        given(authorViewMapper.toAuthorView(authors)).willReturn(views);

        // Act and Assert
        mockMvc.perform(get("/api/authors")
                        .param("name", authorName)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items[0].name").value("John Doe"))
                .andExpect(jsonPath("$.items[0].bio").value("A famous author"))
                .andExpect(jsonPath("$.items[0].photo").value("photo.jpg"))
                .andExpect(jsonPath("$.items.length()").value(1));

    }

    @WithMockUser(username = "user", roles = {"ADMIN"})
    @Test
    public void testFindByName_NotFound() throws Exception {
        // Arrange
        String authorName = "NonExistentAuthor";
        given(authorService.findByName(authorName)).willReturn(Collections.emptyList());

        // Act and Assert
        mockMvc.perform(get("/api/authors")
                        .param("name", authorName)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // Expect OK but empty list
                .andExpect(content().json("{\"items\":[]}"));
    }

    @WithMockUser(username = "user", roles = {"ADMIN"})
    @Test
    public void testGetBooksByAuthorNumber_Success() throws Exception {
        // Arrange
        String authorNumber = "1L";
        Author author = AuthorFactory.create("John Doe", "A famous author", "photo.jpg");
        List<Author> authors = new ArrayList<>();
        authors.add(author);
        Book book = new Book("9781234567897","title","description", new Genre("genre"), authors, null);

        List<Book> books = Collections.singletonList(book);

        BookView view = new BookView();
        view.setIsbn("9781234567897");
        view.setTitle("title");
        view.setDescription("description");


        given(authorService.findByAuthorNumber(authorNumber)).willReturn(Optional.of(author));
        given(authorService.findBooksByAuthorNumber(authorNumber)).willReturn(books);
        given(bookViewMapper.toBookView(books)).willReturn(Collections.singletonList(view));

        // Act and Assert
        mockMvc.perform(get("/api/authors/{authorNumber}/books", authorNumber)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items[0].isbn").value("9781234567897"))
                .andExpect(jsonPath("$.items[0].title").value("title"))
                .andExpect(jsonPath("$.items[0].description").value("description"));
    }

    @WithMockUser(username = "user", roles = {"ADMIN"})
    @Test
    public void testGetBooksByAuthorNumber_AuthorNotFound() throws Exception {
        // Arrange
        String authorNumber = "1L";
        given(authorService.findByAuthorNumber(authorNumber)).willReturn(Optional.empty());

        // Act and Assert
        mockMvc.perform(get("/api/authors/{authorNumber}/books", authorNumber)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @WithMockUser(username = "user", roles = {"ADMIN"})
    @Test
    public void testGetTop5Authors_Success() throws Exception {
        // Arrange
        long count = 10;
        AuthorLendingView view = new AuthorLendingView();
        view.setAuthorName("John");
        view.setLendingCount(count);
        given(authorService.findTopAuthorByLendings()).willReturn(Collections.singletonList(view));

        // Act and Assert
        mockMvc.perform(get("/api/authors/top5")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items[0].authorName").value("John"))
                .andExpect(jsonPath("$.items[0].lendingCount").value(count));

    }

    @WithMockUser(username = "user", roles = {"ADMIN"})
    @Test
    public void testGetTop5Authors_NotFound() throws Exception {
        // Arrange
        given(authorService.findTopAuthorByLendings()).willReturn(Collections.emptyList());

        // Act and Assert
        mockMvc.perform(get("/api/authors/top5")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @WithMockUser(username = "user", roles = {"ADMIN"})
    @Test
    public void testGetAuthorPhoto_NotFound() throws Exception {
        // Arrange
        String authorNumber = "1L";
        given(authorService.findByAuthorNumber(authorNumber)).willReturn(Optional.empty());

        // Act and Assert
        mockMvc.perform(get("/api/author/{authorNumber}/photo", authorNumber)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @WithMockUser(username = "user", roles = {"ADMIN"})
    @Test
    public void testGetAuthorWithCoAuthors_NotFound() throws Exception {
        // Arrange
        String authorNumber = "1L";
        given(authorService.findByAuthorNumber(authorNumber)).willReturn(Optional.empty());

        // Act and Assert
        mockMvc.perform(get("/api/author/{authorNumber}/coauthors", authorNumber)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

}
