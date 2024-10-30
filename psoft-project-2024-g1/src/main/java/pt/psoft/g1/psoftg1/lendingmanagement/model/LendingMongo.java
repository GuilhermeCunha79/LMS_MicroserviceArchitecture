package pt.psoft.g1.psoftg1.lendingmanagement.model;

import jakarta.persistence.Id;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.StaleObjectStateException;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.readermanagement.model.ReaderDetails;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.Optional;

@Document(collection = "lending")
public class LendingMongo {

    @Id
    @Getter
    private String id; // Usando String como ID para MongoDB

    @Field("lending_number")
    private LendingNumber lendingNumber;

    @NotNull
    @Getter
    @Field("book")
    private Book book;

    @NotNull
    @Getter
    @Field("reader_details")
    private ReaderDetails readerDetails;

    @NotNull
    @Getter
    @Field("start_date")
    private LocalDate startDate;

    @NotNull
    @Getter
    @Field("limit_date")
    private LocalDate limitDate;

    @Getter
    @Field("returned_date")
    private LocalDate returnedDate;

    @Version
    @Getter
    private Long version; // Usar Long para versionamento

    @Size(min = 0, max = 1024)
    @Field("commentary")
    private String commentary = null;

    @Field("days_until_return")
    private Integer daysUntilReturn;

    @Field("days_overdue")
    private Integer daysOverdue;

    @Getter
    private int fineValuePerDayInCents;

    public LendingMongo(Book book, ReaderDetails readerDetails, int seq, int lendingDuration, int fineValuePerDayInCents) {
        this.book = Objects.requireNonNull(book);
        this.readerDetails = Objects.requireNonNull(readerDetails);
        this.lendingNumber = new LendingNumber(seq);
        this.startDate = LocalDate.now();
        this.limitDate = LocalDate.now().plusDays(lendingDuration);
        this.returnedDate = null;
        this.fineValuePerDayInCents = fineValuePerDayInCents;
        setDaysUntilReturn();
        setDaysOverdue();
    }
    protected LendingMongo() {}


    public void setReturned(final Long desiredVersion, final String commentary) {
        if (this.returnedDate != null)
            throw new IllegalArgumentException("book has already been returned!");

        // check current version
        if (!this.version.equals(desiredVersion))
            throw new StaleObjectStateException("Object was already modified by another user", this.id);

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
    public static LendingMongo newBootstrappingLending(Book book,
                                                  ReaderDetails readerDetails,
                                                  int year,
                                                  int seq,
                                                  LocalDate startDate,
                                                  LocalDate returnedDate,
                                                  int lendingDuration,
                                                  int fineValuePerDayInCents) {
        LendingMongo lending = new LendingMongo();
        lending.book = Objects.requireNonNull(book);
        lending.readerDetails = Objects.requireNonNull(readerDetails);
        lending.lendingNumber = new LendingNumber(year, seq);
        lending.startDate = startDate;
        lending.limitDate = startDate.plusDays(lendingDuration);
        lending.fineValuePerDayInCents = fineValuePerDayInCents;
        lending.returnedDate = returnedDate;
        return lending;
    }
}