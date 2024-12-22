package pt.psoft.g1.psoftg1.authormanagement.model;

import org.hibernate.StaleObjectStateException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import pt.psoft.g1.psoftg1.exceptions.ConflictException;
import pt.psoft.g1.psoftg1.shared.model.Photo;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AuthorTest {
    private Author author;
    private final String validName = "João Alberto";
    private final String validBio = "O João Alberto nasceu em Chaves e foi pedreiro a maior parte da sua vida.";

    @BeforeEach
    void setup() {
        author = AuthorFactory.create("John Doe", "Biography of John", "photoURI");
    }

    // Testes de Caixa Branca (White Box Tests)
    @Test
    void testConstructor() {
        assertEquals("John Doe", author.getName());
        assertEquals("Biography of John", author.getBio());
        assertEquals("photoURI", author.getPhoto().getPhotoFile());
    }

    @Test
    void ensureNameCannotBeNull() {
        assertThrows(IllegalArgumentException.class, () -> AuthorFactory.create(null, validBio, null));
    }

    @Test
    void ensureBioCannotBeNull() {
        assertThrows(IllegalArgumentException.class, () -> AuthorFactory.create(validName, null, null));
    }

    @Test
    void testRemovePhotoWithValidVersion() {
        long currentVersion = author.getVersion();
        author.removePhoto(currentVersion);
        assertNull(author.getPhoto());
    }

    @Test
    void testRemovePhotoWithStaleVersionThrowsException() {
        assertThrows(ConflictException.class, () -> author.removePhoto(999));
    }

    @Test
    void testGetVersion() {
        assertEquals(Long.valueOf(0), author.getVersion());
    }

    // Testes de Caixa Preta (Black Box Tests)

    @Test
    void testCreateAuthorWithoutPhoto() {
        Author author = AuthorFactory.create(validName, validBio, null);
        assertNotNull(author);
        assertNull(author.getPhoto());
    }

    @Test
    void testGetAuthorNumber() {
        author.setAuthorNumber(String.valueOf(42L));
        assertEquals(String.valueOf(42L), author.getAuthorNumber());
    }

    @Test
    void testSetName() {
        author.setName("New Author Name");
        assertEquals("New Author Name", author.getName());
    }

    @Test
    void testSetBio() {
        author.setBio("New Author Bio");
        assertEquals("New Author Bio", author.getBio());
    }

    @Test
    void ensureValidPhoto() {
        Author author = AuthorFactory.create(validName, validBio, "photoTest.jpg");
        Photo photo = author.getPhoto();
        assertNotNull(photo);
        assertEquals("photoTest.jpg", photo.getPhotoFile());
    }

    @Test
    void ensurePhotoCanBeNull_AkaOptional() {
        Author author = AuthorFactory.create(validName, validBio, null);
        assertNull(author.getPhoto());
    }

    //Testes de Mutação

    @Test
    void testRemovePhotoWithStaleVersionThrowsExceptionMutation() {
        assertThrows(ConflictException.class, () -> author.removePhoto(999));
    }

    @Test
    void whenVersionIsStale_removePhotoThrowsConflictException() {
        long incorrectVersion = author.getVersion() + 1;

        assertThrows(ConflictException.class, () -> author.removePhoto(incorrectVersion));
    }

    @Test
    void whenSetName_withNullName_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> author.setName(null));
    }



}
