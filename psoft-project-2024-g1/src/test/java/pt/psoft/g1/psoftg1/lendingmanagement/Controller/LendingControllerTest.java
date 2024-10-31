package pt.psoft.g1.psoftg1.lendingmanagement.Controller;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import org.springframework.web.context.request.WebRequest;
import pt.psoft.g1.psoftg1.lendingmanagement.api.LendingController;
import pt.psoft.g1.psoftg1.lendingmanagement.api.LendingView;
import pt.psoft.g1.psoftg1.lendingmanagement.api.LendingViewMapper;
import pt.psoft.g1.psoftg1.lendingmanagement.model.Lending;
import pt.psoft.g1.psoftg1.lendingmanagement.services.CreateLendingRequest;
import pt.psoft.g1.psoftg1.lendingmanagement.services.LendingService;
import pt.psoft.g1.psoftg1.lendingmanagement.services.SetLendingReturnedRequest;
import pt.psoft.g1.psoftg1.readermanagement.model.ReaderDetails;
import pt.psoft.g1.psoftg1.readermanagement.services.ReaderService;
import pt.psoft.g1.psoftg1.shared.services.ConcurrencyService;
import pt.psoft.g1.psoftg1.usermanagement.model.User;
import pt.psoft.g1.psoftg1.usermanagement.services.UserService;

import java.util.Optional;

/*
@SpringBootTest
@AutoConfigureMockMvc
public class LendingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private LendingService lendingService;

    @Mock
    private ConcurrencyService concurrencyService;

    @Mock
    private LendingViewMapper lendingViewMapper;

    @InjectMocks
    private LendingController lendingController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(lendingController).build();
    }

    @Test
    @WithMockUser(username = "ADMIN", roles = {"ADMIN"})
    void whenCreateLending_thenReturnsCreatedStatus() throws Exception {
        // Arrange
        CreateLendingRequest request = new CreateLendingRequest();
        Lending lendingDouble = mock(Lending.class);

        // Simulando o comportamento do serviço
        when(lendingService.create(any(CreateLendingRequest.class))).thenReturn(lendingDouble);

        // Cria uma instância do LendingController com mocks
        LendingController controller = new LendingController(
                lendingService,
                mock(ReaderService.class),
                mock(UserService.class),
                mock(ConcurrencyService.class),
                mock(LendingViewMapper.class)
        );

        // Act
        ResponseEntity<?> response = controller.create(request);

        // Assert
        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }




    @Test
    @WithMockUser(username = "USER", roles = {"USER"})
    void whenFindByLendingNumber_thenReturnsOkStatus() throws Exception {
        // Arrange
        String year = "2024";
        String seq = "1";
        String ln = year + "/" + seq;

        Lending lendingDouble = mock(Lending.class);
        LendingViewMapper lendingViewMapper = mock(LendingViewMapper.class);
        UserService userService = mock(UserService.class);
        ReaderService readerService = mock(ReaderService.class);
        Authentication authentication = mock(Authentication.class);
        User loggedUser = mock(User.class);

        // Configurar os mocks
        when(lendingService.findByLendingNumber(ln)).thenReturn(Optional.of(lendingDouble));
        when(lendingViewMapper.toLendingView(lendingDouble)).thenReturn(new LendingView());
        when(userService.getAuthenticatedUser(authentication)).thenReturn(loggedUser); // Garante que retorna um User válido
        when(loggedUser.getUsername()).thenReturn("USER");

        // Configurar o ReaderDetails caso o usuário seja um Reader
        ReaderDetails readerDetails = mock(ReaderDetails.class);
        when(readerService.findByUsername(loggedUser.getUsername())).thenReturn(Optional.of(readerDetails));
        when(readerDetails.getReaderNumber()).thenReturn("1234");
        when(lendingDouble.getReaderDetails()).thenReturn(readerDetails); // Simular associação entre Reader e Lending

        // Criar o controller com o LendingViewMapper e UserService mocks configurados
        LendingController controller = new LendingController(
                lendingService,
                readerService,
                userService,
                mock(ConcurrencyService.class),
                lendingViewMapper
        );

        // Act
        ResponseEntity<?> response = controller.findByLendingNumber(authentication, Integer.parseInt(year), Integer.parseInt(seq));

        // Assert
        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }







    @Test
    @WithMockUser(username = "USER", roles = {"USER"})
    void whenSetLendingReturned_thenReturnsOkStatus() throws Exception {
        // Arrange
        String year = "2024";
        String seq = "1";
        SetLendingReturnedRequest request = new SetLendingReturnedRequest();
        Lending lendingDouble = mock(Lending.class);

        // Configura o Lending para retornar um ReaderDetails válido
        ReaderDetails readerDetailsMock = mock(ReaderDetails.class);
        when(readerDetailsMock.getReaderNumber()).thenReturn("reader-123"); // Define o número do leitor
        when(lendingDouble.getReaderDetails()).thenReturn(readerDetailsMock); // Retorna o mock de ReaderDetails

        // Configura o comportamento dos mocks para Lending e LendingService
        when(lendingService.findByLendingNumber(any())).thenReturn(Optional.of(lendingDouble));
        when(lendingService.setReturned(any(), any(), anyLong())).thenReturn(lendingDouble);
        when(lendingViewMapper.toLendingView(lendingDouble)).thenReturn(new LendingView());

        // Configuração de mocks para Authentication e WebRequest
        Authentication authentication = mock(Authentication.class);
        WebRequest webRequest = mock(WebRequest.class);
        when(webRequest.getHeader(ConcurrencyService.IF_MATCH)).thenReturn("1"); // Simula o cabeçalho If-Match

        // Mock para o userService e criação de um User válido
        UserService userService = mock(UserService.class);
        User loggedUser = mock(User.class);
        when(userService.getAuthenticatedUser(authentication)).thenReturn(loggedUser);
        when(loggedUser.getUsername()).thenReturn("USER"); // Configura o username para evitar NullPointerException

        // Mock para o readerService e criação de um ReaderDetails válido
        ReaderService readerService = mock(ReaderService.class);
        when(readerService.findByUsername("USER")).thenReturn(Optional.of(readerDetailsMock));

        // Configura o LendingController com os mocks
        LendingController controller = new LendingController(
                lendingService,
                readerService,  // Passa o readerService configurado com o ReaderDetails mockado
                userService,
                mock(ConcurrencyService.class),
                lendingViewMapper
        );

        // Act
        ResponseEntity<?> response = controller.setLendingReturned(webRequest, authentication, Integer.parseInt(year), Integer.parseInt(seq), request);

        // Assert
        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}*/