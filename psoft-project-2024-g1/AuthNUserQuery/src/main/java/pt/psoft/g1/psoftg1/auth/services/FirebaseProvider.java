package pt.psoft.g1.psoftg1.auth.services;


import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import pt.psoft.g1.psoftg1.usermanagement.services.UserService;


import java.io.FileInputStream;
import java.io.IOException;

@Profile("firebase")
@RequiredArgsConstructor
@Service
public class FirebaseProvider implements AuthProvider {

    private final UserService userService;

    @Override
    public Authentication authenticate(String request) {
        try {
            initializeFirebase();

            UserRecord decodedToken = FirebaseAuth.getInstance().getUser(request);
            String email = decodedToken.getEmail();

            if (email == null) {
                throw new AuthenticationException("Email não encontrado no token");
            }

            // Carrega os detalhes do user com base no email
            UserDetails userDetails = userService.loadUserByUsername(email);

            if (userDetails == null) {
                throw new AuthenticationException("Utilizador não encontrado: " + email);
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

    public static class AuthenticationException extends RuntimeException {
        public AuthenticationException(String message) {
            super(message);
        }

        public AuthenticationException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
