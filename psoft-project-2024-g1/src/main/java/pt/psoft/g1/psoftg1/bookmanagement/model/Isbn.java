package pt.psoft.g1.psoftg1.bookmanagement.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;

import java.io.Serializable;


@Embeddable
@EqualsAndHashCode
public class Isbn implements Serializable {
    @Size(min = 10, max = 13)
    @Column(name="ISBN", length = 16)

    String isbn;

    public Isbn(String isbn) {
        if (isValidIsbn(isbn)) {
            this.isbn = isbn;
        } else {
            throw new IllegalArgumentException("Invalid ISBN-13 format or check digit.");
        }
    }

    protected Isbn() {};

    private static boolean isValidIsbn(String isbn) {
        if(isbn == null)
            throw new IllegalArgumentException("Isbn cannot be null");
        return (isbn.length() == 10) ? isValidIsbn10(isbn) : isValidIsbn13(isbn);
    }

    private static boolean isValidIsbn10(String isbn) {
        if (!isbn.matches("\\d{9}[\\dX]")) {
            return false;
        }

        int sum = 0;
        for (int i = 0; i < 9; i++) {
            sum += (isbn.charAt(i) - '0') * (10 - i);
        }

        char lastChar = isbn.charAt(9);
        int lastDigit = (lastChar == 'X') ? 10 : lastChar - '0';
        sum += lastDigit;

        return sum % 11 == 0;
    }

    private static boolean isValidIsbn13(String isbn) {
        if (isbn == null || !isbn.matches("\\d{13}")) {
            return false;
        }

        int sum = 0;
        for (int i = 0; i < 12; i++) {
            int digit = Integer.parseInt(isbn.substring(i, i + 1));
            sum += (i % 2 == 0) ? digit : digit * 3;
        }

        int checksum = 10 - (sum % 10);
        if (checksum == 10) {
            checksum = 0;
        }

        return checksum == Integer.parseInt(isbn.substring(12));
    }

    public String toString() {
        return this.isbn;
    }
}
