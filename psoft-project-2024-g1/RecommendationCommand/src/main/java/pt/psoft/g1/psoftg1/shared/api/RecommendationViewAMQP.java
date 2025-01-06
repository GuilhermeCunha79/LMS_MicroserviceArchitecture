package pt.psoft.g1.psoftg1.shared.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Setter;

@Data
@Schema(description = "A Recommendation form AMQP communication")
@JsonIgnoreProperties(ignoreUnknown = true)
public class RecommendationViewAMQP {


    @Setter
    private Long recommendationNumber;

    @Setter
    private String lendingNumber;

    @Setter
    private String readerDetailsId;

    @Setter
    private String isbn; //verificar se é necessario

    @Setter
    private String commentary;

    @Setter
    private int status;
}
