package pt.psoft.g1.psoftg1.readermanagement.model;

import org.junit.Test;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class ReaderNumberTest {

    @Test
    public void testConstructorWithYearAndNumber() {
        int year = 2024;
        int number = 123;
        ReaderNumber readerNumber = new ReaderNumber(year, number);

        assertThat(readerNumber.toString()).isEqualTo("2024/123");
    }

    @Test
    public void testConstructorWithNumber() {
        int number = 456;
        int currentYear = LocalDate.now().getYear();
        ReaderNumber readerNumber = new ReaderNumber(number);

        assertThat(readerNumber.toString()).isEqualTo(currentYear + "/456");
    }

    @Test
    public void testConstructorWithString() {
        String number = "2024/789";
        ReaderNumber readerNumber = new ReaderNumber(number);

        assertThat(readerNumber.toString()).isEqualTo(LocalDate.now().getYear() + "/789");
    }

    @Test
    public void testSetReaderNumber() {
        ReaderNumber readerNumber = new ReaderNumber(123);
        readerNumber.setReaderNumber("321");

        assertThat(readerNumber.toString()).isEqualTo(LocalDate.now().getYear() + "/321");
    }
}
