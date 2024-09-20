package pt.psoft.g1.psoftg1.lendingmanagement.api;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Data
@Schema(description = "A Lending")
public class LendingView {

    @NotNull
    private String lendingNumber;

    @NotNull
    private String bookTitle;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate limitDate;

    private LocalDate returnedDate;

    private Integer daysUntilReturn;

    private Integer daysOverdue;

    private Integer fineValueInCents;

    @Setter
    @Getter
    private LendingLinksView _links;
}

/*>
A: a ambos deve ser facultado:
>- lending number
>- book title
>- lending date
>- return date
>- number of days till return date (if applicable)
>- number of days in overdue (if applicable)
>- fine amount (if applicable)
*/
