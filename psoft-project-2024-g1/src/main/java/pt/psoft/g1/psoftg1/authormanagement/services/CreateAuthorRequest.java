package pt.psoft.g1.psoftg1.authormanagement.services;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "A DTO for creating a Author")
public class CreateAuthorRequest {
    @Size(min = 1, max = 150)
    private String name;

    @Size(min = 1, max = 4096)
    private String bio;

    @Nullable
    @Getter
    @Setter
    private MultipartFile photo;

    @Nullable
    @Getter
    @Setter
    private String photoURI;
}
