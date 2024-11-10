package pt.psoft.g1.psoftg1;

import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.socket.ConnectionSocketFactory;
import org.apache.hc.client5.http.socket.PlainConnectionSocketFactory;
import org.apache.hc.core5.http.URIScheme;
import org.apache.hc.core5.http.config.Registry;
import org.apache.hc.core5.http.config.RegistryBuilder;
import org.apache.hc.core5.ssl.SSLContextBuilder;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.client.RestTemplateBuilder;

import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import pt.psoft.g1.psoftg1.authormanagement.api.AuthorView;

import org.springframework.http.*;
import pt.psoft.g1.psoftg1.usermanagement.api.UserView;


import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
public class SmokeTeste {

    @Autowired
    private RestTemplateBuilder restTemplateBuilder;

    private RestTemplate restTemplate;
    private UserView userView;

    private final String BASE_URL = "https://vs-gate.dei.isep.ipp.pt:30575/api/";
    private String authToken;

    @BeforeEach
    public void setUp() throws Exception {
        // Configuração do SSL
        SSLContext sslContext = SSLContextBuilder.create()
                .loadTrustMaterial((X509Certificate[] certificateChain, String authType) -> true)
                .build();

        Registry<ConnectionSocketFactory> socketRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register(URIScheme.HTTPS.getId(), new SSLConnectionSocketFactory(sslContext))
                .register(URIScheme.HTTP.getId(), new PlainConnectionSocketFactory())
                .build();

        HttpClient httpClient = HttpClientBuilder.create()
                .setConnectionManager(new PoolingHttpClientConnectionManager(socketRegistry))
                .setConnectionManagerShared(true)
                .build();

        new HttpComponentsClientHttpRequestFactory(httpClient);
        this.restTemplate = restTemplateBuilder
                .requestFactory(() -> new HttpComponentsClientHttpRequestFactory(httpClient))
                .build();

        String url = BASE_URL + "public/login";


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String loginRequestJson = """
                {
                    "username": "maria@gmail.com",
                    "password": "Mariaroberta!123"
                }
                """;

        HttpEntity<String> entity = new HttpEntity<>(loginRequestJson, headers);

        // Act
        ResponseEntity<UserView> response = restTemplate.exchange(url, HttpMethod.POST, entity, UserView.class);
        userView = response.getBody();

        this.authToken = response.getHeaders().getFirst("Authorization");

        // Assertions
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(userView).isNotNull();
    }

    @Test
    public void testFindByAuthorNumber_Success() {

        assertThat(this.authToken).isNotNull();

        String authorNumber = "1";
        String url = BASE_URL + "authors/" + authorNumber;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + this.authToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        // Act
        ResponseEntity<AuthorView> response = restTemplate.exchange(url, HttpMethod.GET, entity, AuthorView.class);
        AuthorView authorView = response.getBody();

        // Assert
        assertThat(authorView).isNotNull();
        assertThat(authorView.getName()).isEqualTo("Manuel Antonio Pina");
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
