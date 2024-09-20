package pt.psoft.g1.psoftg1.genremanagement.services;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class GenreLendingsPerMonthDTO {
    private int year;
    private int month;
    List<GenreLendingsDTO> values;

}
