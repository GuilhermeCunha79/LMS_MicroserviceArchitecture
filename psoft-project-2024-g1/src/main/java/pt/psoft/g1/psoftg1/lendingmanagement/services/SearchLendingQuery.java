package pt.psoft.g1.psoftg1.lendingmanagement.services;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchLendingQuery {
    @Setter
    String readerNumber;
    String isbn;
    Boolean returned;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    String startDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    String endDate;
}
