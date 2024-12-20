package pt.psoft.g1.psoftg1.lendingmanagement.api;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "A Book form AMQP communication")
public class LendingViewAMQP {

    @NotNull
    private String lendingNumber;

    @NotNull
    private String isbn;

    @NotNull
    private int status;

    @NotNull
    private String readerDetailsId;

    private String returnedDate;

    private String commentary;

    @NotNull
    private Long version;
}
