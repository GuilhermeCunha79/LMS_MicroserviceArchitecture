package pt.psoft.g1.psoftg1.usermanagement.model;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import pt.psoft.g1.psoftg1.auth.api.AuthRequest;
import pt.psoft.g1.psoftg1.auth.services.AuthService;
import pt.psoft.g1.psoftg1.authormanagement.api.AuthorView;
import pt.psoft.g1.psoftg1.authormanagement.api.AuthorViewMapper;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.authormanagement.services.AuthorService;
import pt.psoft.g1.psoftg1.authormanagement.services.CreateAuthorRequest;
import pt.psoft.g1.psoftg1.genremanagement.model.GenreFactory;
import pt.psoft.g1.psoftg1.testutils.JsonHelper;
import pt.psoft.g1.psoftg1.testutils.UserTestDataFactory;
import pt.psoft.g1.psoftg1.usermanagement.api.UserView;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Collections;

import java.util.Collections;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
public class SmokeTest {
/*
    @Mock
    private AuthorService authorService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserTestDataFactory userTestDataFactory;

    public ApplicationContext context;

    @Mock
    private AuthService authService;

    @Mock
    private AuthorViewMapper authorViewMapper;

    private final String password = "Test12345_";

    public SmokeTest(ApplicationContext context){
        this.context=context;
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLoginProvider() throws Exception {
        // Configurar dados de teste
        String code = "cfg1GqpyFFcnYVd9W3g5nzMiA0V2";
        User user = new User("GUILHAS","AmAD23!FDSaks");

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(() -> "ROLE_USER");

        // Mocking da autenticação
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(user);
       // when(authentication.getAuthorities()).thenReturn(authorities);

        // Mocking do serviço de autenticação
        when(authService.authentication(code)).thenReturn(authentication);

        // Executando o teste
        mockMvc.perform(get("/login/oauth2/code/google") // Substitua pela URL do seu endpoint
                        .param("code", code)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(header().string(org.springframework.http.HttpHeaders.AUTHORIZATION, notNullValue())) // Verifica se o token não é nulo
                .andExpect(jsonPath("$.username").value("testuser")); // Verifique se o nome de usuário está correto
    }


    @Test
    void testCreateAuthor() throws Exception {
        // Configurar dados de teste
        CreateAuthorRequest createRequest = new CreateAuthorRequest();
        createRequest.setName("Author Name");
        createRequest.setBio("Bio do ireneu José");
        // Adicione outros campos conforme necessário

        Author author = new Author("uthor Name", "Bio","mariaaaM22!");

        AuthorView authorView = new AuthorView();
        authorView.setAuthorNumber("author.getId().toString()");
        authorView.setBio("author.getId()");
        authorView.setName(author.getName());

        // Mocking do serviço
        when(authorService.create(any(CreateAuthorRequest.class))).thenReturn(author);
        when(authorViewMapper.toAuthorView(author)).thenReturn(authorView);

        // Executando o teste
        mockMvc.perform(post("http://localhost:8081/api/authors") // Substitua pela URL do seu endpoint
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Irsineu José\", \"bio\": \"Bio do ireneu José\"}")
                        .characterEncoding("utf-8"))
                .andExpect(status().isCreated());
    }


    @Test
    public void testLoginSuccess() throws Exception {
        final UserView userView = userTestDataFactory
                .createUser("maria1@gmail.com", "Martaaaa", "Test User");

        final AuthRequest request = new AuthRequest(userView.getUsername(), password);

        final MvcResult createResult = this.mockMvc
                .perform(post("/api/admin/users").contentType(MediaType.APPLICATION_JSON)
                        .content(JsonHelper.toJson(objectMapper, request)))
                .andExpect(status().isUnauthorized()).andExpect(header().doesNotExist(HttpHeaders.AUTHORIZATION))
                .andReturn();

        final UserView authUserView = JsonHelper.fromJson(objectMapper, createResult.getResponse().getContentAsString(),
                UserView.class);

        assertNotNull(authUserView.getId(), "User ids must match!");
    }*/
}
