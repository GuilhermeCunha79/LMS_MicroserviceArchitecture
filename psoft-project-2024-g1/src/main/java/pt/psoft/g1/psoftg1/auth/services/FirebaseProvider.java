package pt.psoft.g1.psoftg1.auth.services;


import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import com.google.firebase.auth.FirebaseToken;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import pt.psoft.g1.psoftg1.auth.api.AuthRequest;
import pt.psoft.g1.psoftg1.usermanagement.services.UserService;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Component("firebase")
public class FirebaseProvider implements AuthProvider {

    @Value("${firebase.sign_in_base_url}")
    private String SIGN_IN_BASE_URL;
    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String clientSecret;

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private String redirectUrii;

    private final UserService userService;

    @Autowired
    public FirebaseProvider(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Authentication authenticate(String request) {
        try {
            initializeFirebase();

            // Troca o código de autorização pelo token de ID
           // String idToken = exchangeCodeForToken(request);
            // Verifica o token de ID com o Firebase
            UserRecord decodedToken = FirebaseAuth.getInstance().getUser(request);
            String email = decodedToken.getEmail();

            if (email == null) {
                throw new AuthenticationException("Email não encontrado no token");
            }

            // Carrega os detalhes do usuário com base no email
            UserDetails userDetails = userService.loadUserByUsername(email);

            if (userDetails == null) {
                throw new AuthenticationException("Usuário não encontrado: " + email);
            }

            // Retorna o token de autenticação
            return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        } catch (FirebaseAuthException e) {
            throw new AuthenticationException("Erro na verificação do token do Firebase", e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void initializeFirebase() throws IOException {
        if (FirebaseApp.getApps().isEmpty()) {
            FileInputStream serviceAccount =
                    new FileInputStream("C:\\Users\\Guilherme Cunha\\IdeaProjects\\arqsoft-25-1201506-1211439\\psoft-project-2024-g1\\src\\main\\resources\\servicesAccount.json");

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            FirebaseApp.initializeApp(options);
        }
    }

    private String exchangeCodeForToken(String code) {
        String tokenEndpoint = "https://oauth2.googleapis.com/token";

        // Monta a solicitação para o endpoint de token
        String params = String.format(
                "client_id=%s&client_secret=%s&code=%s&grant_type=authorization_code&redirect_uri=%s",
                clientId, clientSecret, code, redirectUrii
        );

        // Realiza a chamada POST para o endpoint de token
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<String> requestEntity = new HttpEntity<>(params, headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                tokenEndpoint, HttpMethod.POST, requestEntity, Map.class
        );

        // Extrai o token de ID da resposta
        return (String) Objects.requireNonNull(response.getBody()).get("id_token");
    }

    private String login(AuthRequest request) {
        RestTemplate restTemplate = new RestTemplate();

        // Defina os cabeçalhos para a requisição
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Corpo da requisição com email e senha
        Map<String, String> body = new HashMap<>();
        body.put("email", request.getUsername());
        body.put("password", request.getPassword());
        body.put("returnSecureToken", "true");

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                SIGN_IN_BASE_URL, HttpMethod.POST, entity, String.class);

        return response.getBody();
    }

    // Exceção personalizada para autenticação
    public static class AuthenticationException extends RuntimeException {
        public AuthenticationException(String message) {
            super(message);
        }

        public AuthenticationException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
