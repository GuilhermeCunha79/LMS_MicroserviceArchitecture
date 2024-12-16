package pt.psoft.g1.psoftg1.lendingmanagement.model;

import jakarta.persistence.Id;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import pt.psoft.g1.psoftg1.shared.model.Generator;

import java.util.Objects;

@Getter
@Document(collection = "fine")
public class FineMongo {

    @Id
    private Long id;

    @PositiveOrZero
    @Field("fine_value_per_day_in_cents")
    private int fineValuePerDayInCents;

    /** Fine value in Euro cents */
    @PositiveOrZero
    @Field("cents_value")
    private int centsValue;

    @Setter
    @DBRef(lazy = true)
    @Field("lending_id")
    private String lendingId;

    /**
     * Constructs a new {@code FineMongo} object. Sets the current value of the fine,
     * as well as the fine value per day at the time of creation.
     *
     * @param lending the {@code Lending} object which generates this fine.
     */
    public FineMongo(LendingMongo lending) {
        this.id= Generator.generateLongID();
        if (lending.getDaysDelayed() <= 0) {
            throw new IllegalArgumentException("Lending is not overdue");
        }
        this.fineValuePerDayInCents = lending.getFineValuePerDayInCents();
        this.centsValue = fineValuePerDayInCents * lending.getDaysDelayed();
        this.lendingId = Objects.requireNonNull(lending.getId());
    }

    /** Protected empty constructor for ORM only. */
    protected FineMongo() {}
}