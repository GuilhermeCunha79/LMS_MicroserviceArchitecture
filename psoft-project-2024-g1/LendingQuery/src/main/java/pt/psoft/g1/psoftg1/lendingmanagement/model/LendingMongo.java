package pt.psoft.g1.psoftg1.lendingmanagement.model;

import jakarta.persistence.Id;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.StaleObjectStateException;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import pt.psoft.g1.psoftg1.shared.model.Generator;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.Optional;

@Document(collection = "lending")
public class LendingMongo {

    @Id
    @Getter
    private String pk;

    @Field("lending_number")
    @Setter
    private LendingNumber lendingNumber;

    @NotNull
    @Getter
    @Setter
    @Field("isbn")
    private String isbn;

    @NotNull
    @Getter
    @Setter
    @Field("reader_details")
    private String readerDetailsId;

    @NotNull
    @Getter
    @Setter
    @Field("start_date")
    private LocalDate startDate;

    @NotNull
    @Getter
    @Field("limit_date")
    @Setter
    private LocalDate limitDate;

    @Getter
    @Field("returned_date")
    @Setter
    private LocalDate returnedDate;

    @Version
    @Getter
    private Long version; // Usar Long para versionamento

    @Size(min = 0, max = 1024)
    @Field("commentary")
    @Getter
    private String commentary = null;

    @Field("days_until_return")
    @Setter
    private Integer daysUntilReturn;

    @Field("days_overdue")
    @Setter
    private Integer daysOverdue;

    @Getter
    @Setter
    private int fineValuePerDayInCents;

    @Getter
    @Setter
    private int status = LendingStatus.LENDING_WAITING_VALIDATION;

    public LendingMongo(String isbn, String readerDetails, int seq, int lendingDuration, int fineValuePerDayInCents) {
        this.pk = String.valueOf(Generator.generateLongID());
        try {
            this.isbn = Objects.requireNonNull(isbn);
            this.readerDetailsId = Objects.requireNonNull(readerDetails);
        } catch (NullPointerException e) {
            throw new IllegalArgumentException("Null objects passed to lending");
        }
        this.lendingNumber = new LendingNumber(seq);
        this.startDate = LocalDate.now();
        this.limitDate = LocalDate.now().plusDays(lendingDuration);
        this.returnedDate = null;
        this.fineValuePerDayInCents = fineValuePerDayInCents;
        setDaysUntilReturn();
        setDaysOverdue();
        this.status = LendingStatus.LENDING_WAITING_VALIDATION;
    }

    public LendingMongo(String lendingNumber, String isbn, String readerDetails, int lendingDuration, int fineValuePerDayInCents, int status) {
        this.pk = String.valueOf(Generator.generateLongID());
        try {
            this.isbn = Objects.requireNonNull(isbn);
            this.readerDetailsId = Objects.requireNonNull(readerDetails);
        } catch (NullPointerException e) {
            throw new IllegalArgumentException("Null objects passed to lending");
        }
        this.lendingNumber = new LendingNumber(lendingNumber);
        this.startDate = LocalDate.now();
        this.limitDate = LocalDate.now().plusDays(lendingDuration);
        this.returnedDate = null;
        this.fineValuePerDayInCents = fineValuePerDayInCents;
        setDaysUntilReturn();
        setDaysOverdue();
        this.status = status;
    }




    public void setReturned(final Long desiredVersion, final String commentary) {
        if (this.returnedDate != null)
            throw new IllegalArgumentException("book has already been returned!");

        // check current version
        if (!this.version.equals(desiredVersion))
            throw new StaleObjectStateException("Object was already modified by another user", this.pk);

        if (commentary != null)
            this.commentary = commentary;

        this.returnedDate = LocalDate.now();
    }

    public void setCommentary(final String commentary) {
        if (commentary != null)
            this.commentary = commentary;
    }

    public int getDaysDelayed() {
        if (this.returnedDate != null) {
            return Math.max((int) ChronoUnit.DAYS.between(this.limitDate, this.returnedDate), 0);
        } else {
            return Math.max((int) ChronoUnit.DAYS.between(this.limitDate, LocalDate.now()), 0);
        }
    }

    public void setValidated(int status) {
        this.status = status;
    }


    public Optional<Integer> getDaysUntilReturn() {
        setDaysUntilReturn();
        return Optional.ofNullable(daysUntilReturn);
    }

    public Optional<Integer> getDaysOverdue() {
        setDaysOverdue();
        return Optional.ofNullable(daysOverdue);
    }

    public Optional<Integer> getFineValueInCents() {
        Optional<Integer> fineValueInCents = Optional.empty();
        int days = getDaysDelayed();
        if (days > 0) {
            fineValueInCents = Optional.of(fineValuePerDayInCents * days);
        }
        return fineValueInCents;
    }

    public String getLendingNumber() {
        return this.lendingNumber.toString();
    }

    // Método para ajustar dias até a devolução
    private void setDaysUntilReturn() {
        int daysUntilReturn = (int) ChronoUnit.DAYS.between(LocalDate.now(), this.limitDate);
        if (this.returnedDate != null || daysUntilReturn < 0) {
            this.daysUntilReturn = null;
        } else {
            this.daysUntilReturn = daysUntilReturn;
        }
    }

    // Método para ajustar dias em atraso
    private void setDaysOverdue() {
        int days = getDaysDelayed();
        if (days > 0) {
            this.daysOverdue = days;
        } else {
            this.daysOverdue = null;
        }
    }

    /** Factory method for creating new Lending objects for bootstrapping. */
    public static LendingMongo newBootstrappingLending(String book,
                                                       String readerDetails,
                                                  int year,
                                                  int seq,
                                                  LocalDate startDate,
                                                  LocalDate returnedDate,
                                                  int lendingDuration,
                                                  int fineValuePerDayInCents) {
        LendingMongo lending = new LendingMongo();
        lending.isbn = Objects.requireNonNull(book);
        lending.readerDetailsId = Objects.requireNonNull(readerDetails);
        lending.lendingNumber = new LendingNumber(year, seq);
        lending.startDate = startDate;
        lending.limitDate = startDate.plusDays(lendingDuration);
        lending.fineValuePerDayInCents = fineValuePerDayInCents;
        lending.returnedDate = returnedDate;
        return lending;
    }

    public LendingMongo() {}
}