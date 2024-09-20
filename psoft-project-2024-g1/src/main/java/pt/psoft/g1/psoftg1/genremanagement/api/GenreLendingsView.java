package pt.psoft.g1.psoftg1.genremanagement.api;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "A Genre and its average number of associated lendings.")
public class GenreLendingsView {
    @NotNull
    private String genre;
    private Number value;
}