package pt.psoft.g1.psoftg1.genremanagement.api;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "A Genre with book count")
public class GenreBookCountView {
    @NotNull
    private GenreView genreView;

    private Long bookCount;
}