package pt.psoft.g1.psoftg1.usermanagement.services;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;
import pt.psoft.g1.psoftg1.usermanagement.model.User;
import pt.psoft.g1.psoftg1.usermanagement.repositories.UserRepository;

import java.time.Instant;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Component("googleIam")
public class GoogleIAM implements UserIAM{

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;
    public final UserRepository userRepo;
    private final JwtEncoder jwtEncoder;

    public GoogleIAM(@Value("${user.repository.type}") String userRepositoryType, ApplicationContext context, JwtEncoder jwtEncoder){
        this.jwtEncoder = jwtEncoder;
        this.userRepo=context.getBean(userRepositoryType, UserRepository.class);
    }

    //CHATGPT HELP
    public Optional<User> authentication(String accessToken) {
        try {
            // Configura o verificador de tokens Google com o client ID
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), GsonFactory.getDefaultInstance())
                    .setAudience(Collections.singletonList(clientId)) // Substitua pelo seu CLIENT_ID do Google
                    .build();

            // Extrai as informações do utilizador a partir do payload
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(accessToken);
            String email = decodedToken.getEmail();

            // Busca ou cria um utilizador na base de dados usando o userId e email
            if(userRepo.findByUsername(email).isPresent() && Objects.equals(userRepo.findByUsername(email).get().getPassword(), accessToken)){
                return Optional.of(userRepo.findByUsername(email).get());
            }

            User user = new User(email,accessToken);

            userRepo.save(user);

            // Define o escopo e as permissões do utilizador
            String scope = user.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.joining(" "));

            // Gera um token JWT interno para autenticação na aplicação
            Instant now = Instant.now();
            long expiry = 3600L; // Tempo de expiração em segundos (1 hora)

            JwtClaimsSet claims = JwtClaimsSet.builder()
                    .issuer("example.io") // Pode ser substituído pelo seu domínio ou nome de aplicação
                    .issuedAt(now)
                    .expiresAt(now.plusSeconds(expiry))
                    .subject(String.format("%s,%s", user.getId(), user.getUsername()))
                    .claim("roles", scope)
                    .build();

            String token = this.jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

            return Optional.of(user); // Retorna o utilizador dentro de um Optional

        } catch (Exception ex) {
            // Log do erro pode ser feito aqui se necessário
            return Optional.empty(); // Retorna um Optional vazio em caso de erro
        }
    }


}
