package pt.psoft.g1.psoftg1.auth.api;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import pt.psoft.g1.psoftg1.usermanagement.services.UserService;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


//@Profile("firebase")
@Component
public class FirebaseProvider implements AuthProvider {

    private static final String SIGN_IN_BASE_URL = "https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword?key=";

    private final UserService userService;

    @Autowired
    public FirebaseProvider(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Authentication authenticate(AuthRequest request) {
        try {
            JSONParser parser = new JSONParser((JSONParser.MODE_JSON_SIMPLE));

            JSONObject response = (JSONObject) parser.parse(login(request));

            String idToken = response.getAsString("idToken");

            System.out.println(idToken);

            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken);
            System.out.println(decodedToken.getUid());
            String email = decodedToken.getEmail();

            UserDetails userDetails = userService.loadUserByUsername(email);
            System.out.println(userDetails.getUsername());

            return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        } catch (FirebaseAuthException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String login(AuthRequest request) {
        String apiKey = "AIzaSyB9v5zOK9coiqk_EiTutEPk_rNazBgeKqg";
        String url = SIGN_IN_BASE_URL + apiKey;

        // Criar o corpo do pedido
        Map<String, String> body = new HashMap<>();
        body.put("email", request.getUsername());
        body.put("password", request.getPassword());
        body.put("returnSecureToken", "true");

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(body, headers);

        // Enviar o pedido POST para a API do Firebase
        ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody(); // Retorna o corpo da resposta que contém o token
        } else {
            throw new RuntimeException("Failed to login: " + response.getStatusCode());
        }
    }
}
