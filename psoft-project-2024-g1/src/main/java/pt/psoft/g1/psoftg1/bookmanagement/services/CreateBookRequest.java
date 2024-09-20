package pt.psoft.g1.psoftg1.bookmanagement.services;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Getter
@Data
@NoArgsConstructor
@Schema(description = "A DTO for creating a Book")
public class CreateBookRequest {

    @Setter
    private String description;

    @NotBlank
    private String title;

    @NotBlank
    private String genre;

    @Nullable
    @Getter
    @Setter
    private MultipartFile photo;

    @Nullable
    @Getter
    @Setter
    private String photoURI;

    @NotNull
    private List<Long> authors;
}
