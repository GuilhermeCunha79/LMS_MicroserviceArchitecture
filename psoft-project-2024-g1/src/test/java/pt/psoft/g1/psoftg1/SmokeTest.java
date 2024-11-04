package pt.psoft.g1.psoftg1;


import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.socket.ConnectionSocketFactory;
import org.apache.hc.client5.http.socket.PlainConnectionSocketFactory;
import org.apache.hc.client5.http.ssl.TrustAllStrategy;
import org.apache.hc.core5.http.URIScheme;
import org.apache.hc.core5.http.config.Registry;
import org.apache.hc.core5.http.config.RegistryBuilder;
import org.apache.hc.core5.ssl.SSLContextBuilder;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.client.RestTemplateBuilder;

import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import pt.psoft.g1.psoftg1.authormanagement.api.AuthorView;

import org.springframework.http.*;


import java.security.cert.X509Certificate;
import java.util.Map;

import org.apache.http.HttpStatus;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import org.apache.hc.core5.ssl.SSLContextBuilder;

import javax.net.ssl.SSLContext;
import java.security.cert.X509Certificate;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
public class SmokeTest {

    @Autowired
    private RestTemplateBuilder restTemplateBuilder;

    private RestTemplate restTemplate;

    private final String BASE_URL = "https://vs-gate.dei.isep.ipp.pt:31362/api/";

    @BeforeEach
    public void setUp() throws Exception {
        // Configuração do SSL
        SSLContext sslContext = SSLContextBuilder.create()
                .loadTrustMaterial((X509Certificate[] certificateChain, String authType) -> true) // certificados
                .build();

        Registry<ConnectionSocketFactory> socketRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register(URIScheme.HTTPS.getId(), new SSLConnectionSocketFactory(sslContext))
                .register(URIScheme.HTTP.getId(), new PlainConnectionSocketFactory())
                .build();

        HttpClient httpClient = HttpClientBuilder.create()
                .setConnectionManager(new PoolingHttpClientConnectionManager(socketRegistry))
                .setConnectionManagerShared(true)
                .build();

        ClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
        // inicialização do RestTemplate
        this.restTemplate = restTemplateBuilder
                .requestFactory(() -> new HttpComponentsClientHttpRequestFactory(httpClient))
                .build();
    }


    @Test
    public void testFindByAuthorNumber_Success() {
        // Arrange
        String authorNumber = "1";
        String url = BASE_URL + "authors" + "/" + authorNumber;

        // cabeçalhos
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + "eyJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJleGFtcGxlLmlvIiwic3ViIjoiMyxwZWRyb0BnbWFpbC5jb20iLCJleHAiOjE3MzA3OTQ2ODQsImlhdCI6MTczMDc1ODY4NCwicm9sZXMiOiJSRUFERVIifQ.RjiWWHPpA7blOVW-2pIPstTi8lFni_HQZBQomUYbmHQkqNueHR6mvkk9pUbJ9FPy1NXyY3ow13cojrfBt_eC7u8McDdE2JyBXpEGAu4wCc2uXJM2FK4fkj5okjly_pOIT7yx3prllOojU9_XzRtUk_oCCoREOPMPB68f5FjJzpDiL9DILvEUaHo02M_ZqcLl61UMni-rXSerBPonoQt11jiqMmXRMx3ecQrnTxj2mcnNV4IJqlycNX4gEwyYzYk5DXm_qsJvB2NDzXvFswMn_Gy9aPk0QT4qX-35a5WZAIi6frFPB_UYqoNJe3zAI8LZ5QHIaArl7mWbGF6QtPCh9k_xPOYjBYVxTT-PnkEa47KqVKNGHzWOiII_Wkk2d_fc3OrN-zKB_J2g73TAFOPWr_09WCPXeYahJ9nG1-pxp0qldTuIDzrkUHQLJEK4f7OayAO14YMrXKe9lMs8NoPxOdj9-KBa-2VvlTrqP0LEknSfkd165cMBTYUPdTnuRYCwHAe6YHW45fJ67ooe4sEvn9-g8wHP50KwuTGxswfvze0Cmw0uYtjFvuDspeC5vThBsNS_nsYo8JELrESRGhxbSsRgz_nbCWli_nlhCr5AYzvUQklxqckrzzyLZmekb9g_vyRDBHV8p2nwoOap01WTNodrE8_4o2qQNh7VTe7K9og");

        // entidade da requisiçao
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // Act
        ResponseEntity<AuthorView> response = restTemplate.exchange(url, HttpMethod.GET, entity, AuthorView.class);
        AuthorView authorView = response.getBody();

        // Assertions
        assertThat(authorView).isNotNull();
        assertThat(authorView.getName()).isEqualTo("Manuel Antonio Pina");
    }
}
