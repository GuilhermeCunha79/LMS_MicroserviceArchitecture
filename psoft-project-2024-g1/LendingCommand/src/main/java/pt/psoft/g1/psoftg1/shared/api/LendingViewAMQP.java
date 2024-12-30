package pt.psoft.g1.psoftg1.shared.api;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Schema(description = "A Book form AMQP communication")
public class LendingViewAMQP {

    @NotNull
    @Getter
    @Setter
    private String lendingNumber;

    @NotNull
    @Getter
    @Setter
    private String isbn;

    @NotNull
    @Getter
    @Setter
    private int status;

    @NotNull
    @Getter
    @Setter
    private String readerDetailsId;

    @Getter
    @Setter
    private String returnedDate;

    @Getter
    @Setter
    private String commentary;

    private Long version;

    private String recommendationNumber;

}
