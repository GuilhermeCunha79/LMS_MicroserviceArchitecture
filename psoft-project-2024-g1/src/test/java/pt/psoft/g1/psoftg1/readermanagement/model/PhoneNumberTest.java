package pt.psoft.g1.psoftg1.readermanagement.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PhoneNumberTest {
    @Test
    void ensureValidMobilePhoneNumberIsAccepted() {
        assertDoesNotThrow(() -> new PhoneNumber("912345678"));
    }

    @Test
    void ensureValidFixedPhoneNumberIsAccepted() {
        assertDoesNotThrow(() -> new PhoneNumber("212345678"));
    }

    @Test
    void ensureInvalidPhoneNumberThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new PhoneNumber("12345678")); // Too short
        assertThrows(IllegalArgumentException.class, () -> new PhoneNumber("00123456789")); // Too long
        assertThrows(IllegalArgumentException.class, () -> new PhoneNumber("abcdefghij")); // Non-numeric
        assertThrows(IllegalArgumentException.class, () -> new PhoneNumber("512345678")); // Invalid start digit
        assertThrows(IllegalArgumentException.class, () -> new PhoneNumber("91234567")); // Too short by one digit
        assertThrows(IllegalArgumentException.class, () -> new PhoneNumber("21234567")); // Too short by one digit
    }

    @Test
    void ensureCorrectStringRepresentation() {
        PhoneNumber phoneNumber = new PhoneNumber("912345678");
        assertEquals("912345678", phoneNumber.toString());

        PhoneNumber anotherPhoneNumber = new PhoneNumber("212345678");
        assertEquals("212345678", anotherPhoneNumber.toString());
    }
}
