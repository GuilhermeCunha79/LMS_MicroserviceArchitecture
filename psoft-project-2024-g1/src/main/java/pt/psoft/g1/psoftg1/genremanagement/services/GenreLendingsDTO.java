package pt.psoft.g1.psoftg1.genremanagement.services;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;

import java.util.Locale;

@Data
@AllArgsConstructor
public class GenreLendingsDTO {
    @NotNull
    private String genre;
    private Number value;

    public GenreLendingsDTO(String genre, Double value){
        this.genre = genre;
        if(value == null) {
            this.value = 0;
        }else{
            this.value = formatValue(value);
        }
    }
    public GenreLendingsDTO(String genre, Long value){
        this.genre = genre;
        this.value = value;
    }

    public GenreLendingsDTO(Genre genre, Double value){
        this.genre = genre.toString();
        this.value = formatValue(value);
    }

    public GenreLendingsDTO(Genre genre, Long value){
        this.genre = genre.toString();
        this.value = value;
    }

    private Double formatValue(Double value) {
        return Double.valueOf(String.format(Locale.US, "%.1f", value));
    }
}
