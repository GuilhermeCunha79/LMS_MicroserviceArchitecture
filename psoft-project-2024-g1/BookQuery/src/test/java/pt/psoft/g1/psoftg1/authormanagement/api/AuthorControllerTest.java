package pt.psoft.g1.psoftg1.authormanagement.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.authormanagement.model.AuthorFactory;
import pt.psoft.g1.psoftg1.authormanagement.services.AuthorService;
import pt.psoft.g1.psoftg1.authormanagement.services.CreateAuthorRequest;
import pt.psoft.g1.psoftg1.shared.model.generateID.GenerateIDService;
import pt.psoft.g1.psoftg1.shared.services.FileStorageService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthorControllerTest {

    @Mock
    private AuthorService authorService;

    @Mock
    private GenerateIDService authorIDService;

    @Mock
    private FileStorageService fileStorageService;

    @Mock
    private AuthorViewMapper authorViewMapper;

    @InjectMocks
    private AuthorController authorController;

    private CreateAuthorRequest createAuthorRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Configura um MockHttpServletRequest e vincula-o ao contexto de requisição
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        // Configura o CreateAuthorRequest com dados válidos
        createAuthorRequest = new CreateAuthorRequest();
        createAuthorRequest.setPhoto(null); // Sem foto para simplificar
        createAuthorRequest.setPhotoURI(null); // Garante que o URI é nulo
        createAuthorRequest.setName("Test Author");
    }

    @Test
    void testCreateAuthorWith20CharacterID() {
        String hexAuthorID = "dP7MYDnnfacPUjDbV1c1";
        when(authorIDService.generateID()).thenReturn(hexAuthorID);

        String name = "Author Name";
        String bio = "Author Bio";
        String photoURI = "photo-uri";

        // Cria um autor usando o AuthorFactory e configura o ID
        Author author = AuthorFactory.create(name, bio, photoURI);
        author.setAuthorNumber(hexAuthorID);

        // Mapeia o autor criado para um AuthorView (mockado)
        AuthorView authorView = new AuthorView();
        when(authorViewMapper.toAuthorView(any(Author.class))).thenReturn(authorView);

        // Mock da chamada de criação de autor no serviço
        when(authorService.create(createAuthorRequest)).thenReturn(author);

        // Chama o método de criação do controlador
        ResponseEntity<AuthorView> response = authorController.create(createAuthorRequest);

        // Verificações
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(authorView, response.getBody());
        assertNotNull(author.getAuthorNumber());
        assertTrue(author.getAuthorNumber().matches("^[a-zA-Z0-9]{20}$"),
                "O ID do negócio deve ser uma string alfanumérica com 20 caracteres");


        // Verifica as interações
        verify(authorService).create(createAuthorRequest);
        verify(authorViewMapper).toAuthorView(author);
    }








    @Test
    void testCreateAuthorWith20TCharacterID() {
        // Define um ID de autor simulado que deve ser gerado
        String hexAuthorID = "dP7MYDnnfacPUjDbV1c1"; // 20 caracteres alfanuméricos
        when(authorIDService.generateID()).thenReturn(hexAuthorID);

        // Dados para criar um novo autor
        String name = "Author Name";
        String bio = "Author Bio";
        String photoURI = "photo-uri";

        // Cria o objeto de solicitação para criação do autor
        CreateAuthorRequest createAuthorRequest = new CreateAuthorRequest(name, bio, null, photoURI);

        // Cria uma instância de Author com o AuthorFactory
        Author author = AuthorFactory.create(name, bio, photoURI);
        author.setAuthorNumber(hexAuthorID);  // Define o ID simulado no autor

        // Prepara o mapeamento do autor para AuthorView
        AuthorView authorView = new AuthorView();
        when(authorViewMapper.toAuthorView(any(Author.class))).thenReturn(authorView);

        // Configura o mock do serviço de autor para retornar o autor criado
        when(authorService.create(createAuthorRequest)).thenReturn(author);

        // Executa a chamada ao controlador para criar o autor
        ResponseEntity<AuthorView> response = authorController.create(createAuthorRequest);

        // Verifica se o status da resposta é CREATED (201)
        assertEquals(HttpStatus.CREATED, response.getStatusCode(), "A resposta deve ter o status 201 CREATED");

        // Verifica se o corpo da resposta é o AuthorView que foi mapeado
        assertEquals(authorView, response.getBody(), "O corpo da resposta deve ser o AuthorView mapeado");

        // Verifica se o ID do autor não é nulo e que segue o formato correto
        assertNotNull(author.getAuthorNumber(), "O número do autor não deve ser nulo");
        assertTrue(author.getAuthorNumber().matches("^[a-zA-Z0-9]{20}$"),
                "O ID do negócio deve ser uma string alfanumérica com 20 caracteres");

        // Verifica que as interações com os mocks ocorreram como esperado
        verify(authorService).create(createAuthorRequest);
        verify(authorViewMapper).toAuthorView(author);
    }
}