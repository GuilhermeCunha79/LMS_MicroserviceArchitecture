package pt.psoft.g1.psoftg1.shared.services;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * <p>
 * code based on
 * https://github.com/callicoder/spring-boot-file-upload-download-rest-api-example
 *
 *
 */
@ConfigurationProperties(prefix = "file")
@Component
@Data
public class FileStorageProperties {
    private String uploadDir;
    private long photoMaxSize;
}
