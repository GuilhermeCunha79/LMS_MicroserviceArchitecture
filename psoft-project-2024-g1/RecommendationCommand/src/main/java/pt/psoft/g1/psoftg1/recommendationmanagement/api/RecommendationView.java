package pt.psoft.g1.psoftg1.recommendationmanagement.api;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Schema(description = "A Recommendation")
public class RecommendationView {


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

}


