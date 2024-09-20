package pt.psoft.g1.psoftg1.readermanagement.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.nio.file.AccessDeniedException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class BirthDateTest {

    @Test
    void ensureBirthDateCanBeCreatedWithValidDate() {
        assertDoesNotThrow(() -> new BirthDate(2000, 1, 1));
    }

    @Test
    void ensureBirthDateCanBeCreatedWithValidStringDate() {
        assertDoesNotThrow(() -> new BirthDate("2000-01-01"));
    }

    @Test
    void ensureExceptionIsThrownForInvalidStringDateFormat() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new BirthDate("01-01-2000"));
        assertEquals("Provided birth date is not in a valid format. Use yyyy-MM-dd", exception.getMessage());
    }

    @Test
    void ensureCorrectStringRepresentation() {
        BirthDate birthDate = new BirthDate(2000, 1, 1);
        assertEquals("2000-1-1", birthDate.toString());
    }
}
