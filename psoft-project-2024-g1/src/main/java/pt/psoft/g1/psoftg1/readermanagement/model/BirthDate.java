package pt.psoft.g1.psoftg1.readermanagement.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.access.AccessDeniedException;

import java.time.LocalDate;


@Embeddable
@NoArgsConstructor
@PropertySource({"classpath:config/library.properties"})
public class BirthDate {
    @Getter
    @Column(nullable = false, updatable = false)
    @Temporal(TemporalType.DATE)
    LocalDate birthDate;

    @Transient
    private final String dateFormatRegexPattern = "\\d{4}-\\d{2}-\\d{2}";

    @Transient
    @Value("${minimumReaderAge}")
    private int minimumAge;

    public BirthDate(int year, int month, int day) {
        setBirthDate(year, month, day);
    }

    public BirthDate(String birthDate) {
        if(!birthDate.matches(dateFormatRegexPattern)) {
            throw new IllegalArgumentException("Provided birth date is not in a valid format. Use yyyy-MM-dd");
        }

        String[] dateParts = birthDate.split("-");

        int year = Integer.parseInt(dateParts[0]);
        int month = Integer.parseInt(dateParts[1]);
        int day = Integer.parseInt(dateParts[2]);

        setBirthDate(year, month, day);
    }

    private void setBirthDate(int year, int month, int day) {
        LocalDate minimumAgeDate = LocalDate.now().minusYears(minimumAge);
        LocalDate userDate = LocalDate.of(year, month, day);
        if(userDate.isAfter(minimumAgeDate)) {
            throw new AccessDeniedException("User must be, at least, " + minimumAge + "years old");
        }

        this.birthDate = userDate;
    }

    public String toString() {
        return String.format("%d-%d-%d", this.birthDate.getYear(), this.birthDate.getMonthValue(), this.birthDate.getDayOfMonth());
    }
}
