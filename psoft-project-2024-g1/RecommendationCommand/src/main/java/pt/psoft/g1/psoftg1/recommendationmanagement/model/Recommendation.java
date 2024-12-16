package pt.psoft.g1.psoftg1.recommendationmanagement.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import pt.psoft.g1.psoftg1.shared.model.Commentary;
import pt.psoft.g1.psoftg1.shared.model.Generator;

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
    private String isbn; //verificar se é necessario

    @Setter
    private Commentary commentary;

    public Recommendation(String lendingNumber, String readerNumber, String isbn, Commentary commentary) {
        this.recommendationNumber = String.valueOf(Generator.generateLongID());
        this.lendingNumber = lendingNumber;
        this.readerDetailsId = readerNumber;
        this.isbn = isbn;
        this.commentary = commentary;
    }

    protected Recommendation() {
        // for ORM only
    }
}


