package pt.psoft.g1.psoftg1.shared.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NameTest {
    @Test
    void ensureNameMustNotBeNull() {
        assertThrows(IllegalArgumentException.class, () -> new Name(null));
    }

    @Test
    void ensureNameMustNotBeBlank() {
        assertThrows(IllegalArgumentException.class, () -> new Name(""));
    }

    @Test
    void ensureNameMustOnlyBeAlphanumeric() {
        assertThrows(IllegalArgumentException.class, () -> new Name("Ricardo!"));
    }


    /**
     * Text from <a href="https://www.lipsum.com/">Lorem Ipsum</a> generator.
     */
    @Test
    void ensureNameMustNotBeOversize() {
        assertThrows(IllegalArgumentException.class, () -> new Name("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Ut fermentum venenatis augue, a congue turpis eleifend ut. Etiam fringilla ex nulla, id quis."));
    }

    @Test
    void ensureNameIsSet() {
        final var name = new Name("Some name");
        assertEquals("Some name", name.toString());
    }

    @Test
    void ensureNameIsChanged() {
        final var name = new Name("Some name");
        name.setName("Some other name");
        assertEquals("Some other name", name.toString());
    }
}
