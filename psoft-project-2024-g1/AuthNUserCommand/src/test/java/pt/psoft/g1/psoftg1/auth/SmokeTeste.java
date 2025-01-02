package pt.psoft.g1.psoftg1.auth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import pt.psoft.g1.psoftg1.usermanagement.api.UserView;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = "spring.profiles.active= relational, firebase")
public class SmokeTeste {

    private RestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        this.restTemplate = new RestTemplate();
    }

    @Test
    public void testUser_Authorized() {
        String BASE_URL = "http://localhost:8094/api/";
        String url = BASE_URL + "public/login";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String requestBody = """
                {
                    "username": "maria@gmail.com",
                    "password": "Mariaroberta!123"
                }""";

        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        // Act
        ResponseEntity<UserView> response = restTemplate.exchange(url, HttpMethod.POST, entity, UserView.class);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        UserView userView = response.getBody();
        assertThat(userView).isNotNull();
        assertThat(userView.getUsername()).isEqualTo("maria@gmail.com");
        assertThat(userView.getFullName()).isEqualTo("Maria Roberta");

    }

    @Test
    public void testUser_Unauthorized() {
        String BASE_URL = "http://localhost:8094/api/";
        String url = BASE_URL + "public/login";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String requestBody = """
                {
                    "username": "maria1@gmail.com",
                    "password": "Mariarobert1a!123"
                }""";

        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        try {
            // Act
            ResponseEntity<UserView> response = restTemplate.exchange(url, HttpMethod.POST, entity, UserView.class);

            // Assert
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        } catch (HttpClientErrorException.Unauthorized ex) {
            // Assert
            assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        }
    }

}


