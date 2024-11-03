import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pt.psoft.g1.psoftg1.authormanagement.api.AuthorView;
import pt.psoft.g1.psoftg1.authormanagement.services.CreateAuthorRequest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SmokeTest {
/*
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void contextLoads() {
    }

    @Test
    public void shouldCreateNewAuthor() {
        // Preparar um objeto CreateAuthorRequest
        CreateAuthorRequest createAuthorRequest = new CreateAuthorRequest();
        createAuthorRequest.setName("Artur Dias");
        createAuthorRequest.setBio("Nasceu e morreu.");
        // Adicione outros campos necessários, por exemplo, email, etc.

        // Criar um objeto HttpEntity com o corpo da requisição
        HttpEntity<CreateAuthorRequest> requestEntity = new HttpEntity<>(createAuthorRequest);

        // Enviar a requisição POST para criar um novo autor
        ResponseEntity<AuthorView> response = restTemplate.postForEntity("/api/authors", requestEntity, AuthorView.class);

        // Verificar o status da resposta
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        // Verificar o corpo da resposta (opcional)
        AuthorView createdAuthor = response.getBody();
        assertThat(createdAuthor).isNotNull();
        assertThat(createdAuthor.getName()).isEqualTo("Jane Doe"); // Supondo que o AuthorView tenha um método getName()
    }

 */
}
