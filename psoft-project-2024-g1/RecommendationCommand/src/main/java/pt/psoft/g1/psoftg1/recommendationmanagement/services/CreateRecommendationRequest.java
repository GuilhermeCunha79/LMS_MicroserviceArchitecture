package pt.psoft.g1.psoftg1.recommendationmanagement.services;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Data
@NoArgsConstructor
public class CreateRecommendationRequest {
    @Setter
    private Long lendingNumber;

    @Setter
    private Long readerDetailsId;

    @Setter
    private Long isbn; //verificar se é necessario

    @Setter
    private String commentary;

}
