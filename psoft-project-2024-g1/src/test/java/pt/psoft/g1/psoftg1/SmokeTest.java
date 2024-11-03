package pt.psoft.g1.psoftg1;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import pt.psoft.g1.psoftg1.authormanagement.api.AuthorView;

import static org.assertj.core.api.Assertions.assertThat;

/*
@SpringBootTest
public class SmokeTest {

    @Autowired
    private RestTemplateBuilder restTemplateBuilder;

    private RestTemplate restTemplate;

    private final String BASE_URL = "https://vs-gate.dei.isep.ipp.pt:31362/api/authors";

    @BeforeEach
    public void setUp() {
        // Inicializa o RestTemplate com as configurações padrão
        this.restTemplate = restTemplateBuilder.build();
    }

    @Test
    public void testFindByAuthorNumber_Success() {
        // Arrange
        String authorNumber = "1";
        String url = BASE_URL + "/" + authorNumber;

        // Act
        AuthorView authorView = restTemplate.getForObject(url, AuthorView.class);

        // Assertions
        assertThat(authorView).isNotNull();
        assertThat(authorView.getName()).isEqualTo("John Doe");
        assertThat(authorView.getBio()).isEqualTo("A famous author");
        assertThat(authorView.getPhoto()).isEqualTo("photo.jpg");
    }

    @Test
    public void testFindByName_Success() {
        // Arrange
        String authorName = "John";
        String url = BASE_URL + "?name=" + authorName;

        // Act
        AuthorView[] authors = restTemplate.getForObject(url, AuthorView[].class);

        // Assertions
        assertThat(authors).isNotEmpty();
        assertThat(authors[0].getName()).isEqualTo("John Doe");
        assertThat(authors[0].getBio()).isEqualTo("A famous author");
        assertThat(authors[0].getPhoto()).isEqualTo("photo.jpg");
    }
}*/
