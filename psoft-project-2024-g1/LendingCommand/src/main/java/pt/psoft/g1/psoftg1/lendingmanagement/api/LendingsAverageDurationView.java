package pt.psoft.g1.psoftg1.lendingmanagement.api;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "The average lending duration.")
public class LendingsAverageDurationView {
    @NotNull
    private Double lendingsAverageDuration;
}
