package pt.psoft.g1.psoftg1;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import pt.psoft.g1.psoftg1.authormanagement.api.AuthorView;
import pt.psoft.g1.psoftg1.shared.model.Generator;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = "spring.profiles.active=relational, IDService1")
public class SmokeTeste {


    private RestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        this.restTemplate = new RestTemplate();
    }


    @Test
    public void testCreateAuthor() {
        String url = "http://172.17.0.1:83/bookcommanda/api/authors";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);


        String requestBody = """
                {
                    "name": "LdoldSss Jose",
                    "bio": "dLodsl"
                }""";

        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        try {
            // Act
            ResponseEntity<AuthorView> response = restTemplate.exchange(url, HttpMethod.POST, entity, AuthorView.class);

            // Assert
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        } catch (HttpClientErrorException.Unauthorized ex) {
            // Assert
            assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        }
    }


}
