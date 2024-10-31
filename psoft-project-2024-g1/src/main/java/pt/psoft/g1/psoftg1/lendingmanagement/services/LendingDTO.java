package pt.psoft.g1.psoftg1.lendingmanagement.services;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pt.psoft.g1.psoftg1.lendingmanagement.model.LendingNumber;
import pt.psoft.g1.psoftg1.shared.model.Name;

import java.time.LocalDate;
import java.util.Optional;

@Getter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LendingDTO {

    private String lendingNumber;
    private String bookTitle;
    private String readerName;
    private LocalDate startDate;
    private LocalDate limitDate;
    private LocalDate returnedDate;
    private String commentary;
    private Integer daysUntilReturn;
    private Integer daysOverdue;
    private int fineValuePerDayInCents;

}
