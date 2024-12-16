package pt.psoft.g1.psoftg1.lendingmanagement.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * The {@code LendingNumber} class handles the business logic of the identifier of a {@code Lending}.
 * <p>
 * It stores the year of the lending and a sequencial number, and a string combining these two.
 * @author  rmfranca*/
@Embeddable
public class LendingNumber implements Serializable {

    /**
     * Natural key of a {@code Lending}.
     * <p>
     * The string is constructed based on the values of {@code year} and {@code sequencial} (e.g.: 2024/23).
     */
    @Column(name = "LENDING_NUMBER", length = 32)
    @NotNull
    @NotBlank
    @Size(min = 6, max = 32)
    private String lendingNumber;


    /**
     * Constructs a new {@code LendingNumber} object based on a year and a given sequential number.
     * @param   year        Year component of the {@code LendingNumber}
     * @param   sequential  Sequential component of the {@code LendingNumber}
     * */
    public LendingNumber(int year, int sequential) {
        if(year < 1970 || LocalDate.now().getYear() < year)
            throw new IllegalArgumentException("Invalid year component");
        if(sequential < 0)
            throw new IllegalArgumentException("Sequencial component cannot be negative");
        this.lendingNumber = year + "/" + sequential;
    }

    /**
     * Constructs a new {@code LendingNumber} object based on a string.
     * <p>
     * Initialization may fail if the format is not as expected.
     * @param lendingNumber String containing the lending number.
     * */
    public LendingNumber(String lendingNumber){
        if(lendingNumber == null)
            throw new IllegalArgumentException("Lending number cannot be null");

        int year, sequential;
        try {
            year        = Integer.parseInt(lendingNumber, 0, 4, 10);
            sequential  = Integer.parseInt(lendingNumber, 5, lendingNumber.length(), 10);
            if(lendingNumber.charAt(4) != '/')
                throw new IllegalArgumentException("Lending number has wrong format. It should be \"{year}/{sequential}\"");
        }catch (NumberFormatException | IndexOutOfBoundsException e){
            throw new IllegalArgumentException("Lending number has wrong format. It should be \"{year}/{sequential}\"");
        }
        this.lendingNumber = year + "/" + sequential;
    }

    /**
     * Constructs a new {@code LendingNumber} object based on a given sequential number.
     * <p>
     * The {@code sequential} value should be retrieved from the count of lendings made in the current year.
     * The {@code year} value is automatically set with {@code LocalDate.now().getYear()}.
     * @param sequential Sequential component of the {@code LendingNumber}
     * */
    public  LendingNumber(int sequential) {
        if(sequential < 0)
            throw new IllegalArgumentException("Sequencial component cannot be negative");
        this.lendingNumber = LocalDate.now().getYear() + "/" + sequential;
    }

    /**Protected empty constructor for ORM only.*/
    public LendingNumber() {}

    public String toString() {
        return this.lendingNumber;
    }

}
