package pt.psoft.g1.psoftg1.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;


/**
 * Based on https://www.baeldung.com/spring-5-webclient
 */
@Configuration
@EnableConfigurationProperties
@RequiredArgsConstructor
public class ApiNinjasConfig {
    private final String VERSION = "v1/";

    private final String HEADER = "X-API-KEY";

    @Value("${my.ninjas-key}")
    private String key;


    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .baseUrl("https://api.api-ninjas.com/" + VERSION)
                .defaultHeader(HEADER, key)
                .build();
    }
}
