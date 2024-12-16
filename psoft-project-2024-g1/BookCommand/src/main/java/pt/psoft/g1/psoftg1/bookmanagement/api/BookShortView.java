package pt.psoft.g1.psoftg1.bookmanagement.api;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
@Data
@Schema(description = "A Book, with details ommited")
public class BookShortView {
    @NotNull
    private String title;

    @NotNull
    private String isbn;

    @Setter
    @Getter
    private String _links;
}
