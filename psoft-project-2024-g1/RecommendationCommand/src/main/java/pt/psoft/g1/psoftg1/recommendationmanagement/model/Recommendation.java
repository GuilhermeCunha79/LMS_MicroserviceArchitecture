package pt.psoft.g1.psoftg1.recommendationmanagement.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import pt.psoft.g1.psoftg1.shared.model.Commentary;
import pt.psoft.g1.psoftg1.shared.model.Generator;
import pt.psoft.g1.psoftg1.shared.model.RecommendationStatus;

@Getter
@Entity
@Table(name = "Recommendation")
public class Recommendation {

    @Id
    private String recommendationNumber;

    @Version
    @Setter
    private Long version;

    @Setter
    private String lendingNumber;

    @Setter
    private String readerDetailsId;

    @Setter
    private String isbn;

    @Setter
    private Commentary commentary;

    @Getter
    @Setter
    private int status;

    public Recommendation(String lendingNumber, String readerNumber, String isbn, Commentary commentary) {
        this.recommendationNumber = String.valueOf(Generator.generateLongID());
        this.lendingNumber = lendingNumber;
        this.readerDetailsId = readerNumber;
        this.isbn = isbn;
        this.commentary = commentary;
        this.status = RecommendationStatus.RECOMMENDATION_WAITING_VALIDATION;
    }

    protected Recommendation() {
        // for ORM only
    }
}


