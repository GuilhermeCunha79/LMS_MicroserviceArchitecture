package pt.psoft.g1.psoftg1.lendingmanagement.services;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "A DTO for setting a Lending as returned")
public class SetLendingReturnedRequest {
    @Size(max = 1024)
    private String commentary;

}
