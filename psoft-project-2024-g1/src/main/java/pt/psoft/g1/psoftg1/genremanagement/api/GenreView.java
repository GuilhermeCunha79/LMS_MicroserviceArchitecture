package pt.psoft.g1.psoftg1.genremanagement.api;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;


@Data
@Schema(description = "A Genre")
public class GenreView {
    @NotNull
    private String genre;
}