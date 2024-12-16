package pt.psoft.g1.psoftg1.shared.model;

import org.junit.jupiter.api.Test;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class PhotoTest {
    @Test
    void ensurePathMustNotBeNull() {
        assertThrows(NullPointerException.class, () -> new Photo(null));
    }

    @Test
    void ensurePathIsValidToLocalFile() {
        Path fileStorageLocation = Paths.get("uploads-psoft-g1").toAbsolutePath().normalize();
        assertNotEquals(null, fileStorageLocation.toString());

        Photo photo = new Photo(Paths.get("photoTest.jpg"));
        assertEquals(photo.getPhotoFile(), "photoTest.jpg");
    }
}
