package pt.psoft.g1.psoftg1.usermanagement.services;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import pt.psoft.g1.psoftg1.usermanagement.model.User;

import java.util.Optional;

@Component("facebookIam")
public class FacebookIAM implements UserIAM {
    @Override
    public Optional<User> authentication(String accessToken) {
        /* String url = String.format("https://graph.facebook.com/me?access_token=%s", accessToken);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<UserReq> response = restTemplate.getForEntity(url, FacebookUser.class);

        if (response.getStatusCode() != HttpStatus.OK) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // Extrai as informações do utilizador a partir da resposta do Facebook
        FacebookUser facebookUser = response.getBody();
        String userId = facebookUser.getId(); // ID único do Facebook para o utilizador
        String email = facebookUser.getEmail();

        // Busca ou cria um utilizador na base de dados usando o userId e email
        User user = userService.loginOrCreateFacebook(email, userId);

        // Define o escopo e as permissões do utilizador
        String scope = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));

        // Gera um token JWT interno para autenticação na aplicação
        Instant now = Instant.now();
        long expiry = 3600L; // Tempo de expiração em segundos (1 hora)

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("example.io")
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiry))
                .subject(String.format("%s,%s", user.getId(), user.getUsername()))
                .claim("roles", scope)
                .build();

        String token = this.jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

        // Retorna a resposta com o token JWT interno e as informações do utilizador
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, token)
                .body(userViewMapper.toUserView(user));

    } catch (Exception ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }*/
        return Optional.empty();
    }
}
