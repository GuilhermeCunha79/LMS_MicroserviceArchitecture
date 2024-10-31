package pt.psoft.g1.psoftg1.authormanagement.model;

import org.hibernate.StaleObjectStateException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pt.psoft.g1.psoftg1.authormanagement.services.CreateAuthorRequest;
import pt.psoft.g1.psoftg1.authormanagement.services.UpdateAuthorRequest;
import pt.psoft.g1.psoftg1.exceptions.ConflictException;
import pt.psoft.g1.psoftg1.shared.model.EntityWithPhoto;
import pt.psoft.g1.psoftg1.shared.model.Photo;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthorTest {
    private Author author;
    private final String validName = "João Alberto";
    private final String validBio = "O João Alberto nasceu em Chaves e foi pedreiro a maior parte da sua vida.";
    @Mock
    private final UpdateAuthorRequest request = new UpdateAuthorRequest(validName, validBio, null, null);

    private static class EntityWithPhotoImpl extends EntityWithPhoto { }
    @BeforeEach
    void setup() {
        author = new Author("John Doe", "Biography of John", "photoURI");
       // author.setVersion(1L);
    }

    @Test
    void testConstructor() {
        assertEquals("John Doe", author.getName());
        assertEquals("Biography of John", author.getBio());
        assertEquals("photoURI", author.getPhoto().getPhotoFile());
    }

    @Test
    void testGetId() {
        assertEquals(1L, author.getId());
    }

    @Test
    void testGetVersion1L() {
        assertEquals(0L, author.getVersion());
    }

    @Test
    void applyPatchGetNameNull() {
        final var subject = new Author("Valid Name", validBio, null);
        UpdateAuthorRequest request = new UpdateAuthorRequest();
        request.setName(null);

        assertThrows(StaleObjectStateException.class, () -> subject.applyPatch(999, request));
    }

    @Test
    void applyPatchGetBioNull() {
        final var subject = new Author("Valid Name", validBio, null);
        UpdateAuthorRequest request = new UpdateAuthorRequest();
        request.setBio(null);

        assertThrows(StaleObjectStateException.class, () -> subject.applyPatch(999, request));
    }

    @Test
    void applyPatchGetPhotoUriNull() {
        final var subject = new Author("Valid Name", validBio, null);
        UpdateAuthorRequest request = new UpdateAuthorRequest();
        request.setPhotoURI(null);

        assertThrows(StaleObjectStateException.class, () -> subject.applyPatch(999, request));
    }

    @Test
    void removePhotoVersionMismatch() {
        Author author = new Author("Valid Name", "Valid Bio", "photoURI");
        long wrongVersion = 999L;


        assertThrows(ConflictException.class, () -> {

            author.removePhoto(wrongVersion);
        });
    }

    @Test
    void testGetVersion() {
        assertEquals(Long.valueOf(0), author.getVersion());
    }
    @Test
    void ensureNameNotNull(){
        assertThrows(IllegalArgumentException.class, () -> new Author(null,validBio, null));
    }

    @Test
    void ensureBioNotNull(){
        assertThrows(IllegalArgumentException.class, () -> new Author(validName,null, null));
    }

    @Test
    public void testApplyPatch_VersionMismatch_ThrowsException() {
        // Arrange
        long desiredVersion = 2L; // Mismatched version

        // Act & Assert
        assertThrows(StaleObjectStateException.class, () -> {
            author.applyPatch(desiredVersion, request);
        });
    }

    @Test
    void whenVersionIsStaleItIsNotPossibleToPatch() {
        final var subject = new Author(validName,validBio, null);

        assertThrows(StaleObjectStateException.class, () -> subject.applyPatch(999, request));
    }

    @Test
    void testCreateAuthorWithoutPhoto() {
        Author author = new Author(validName, validBio, null);
        assertNotNull(author);
        assertNull(author.getPhoto());
    }

    @Test
    void testCreateAuthorRequestWithPhoto() {
        CreateAuthorRequest request = new CreateAuthorRequest(validName, validBio, null, "photoTest.jpg");
        Author author = new Author(request.getName(), request.getBio(), "photoTest.jpg");
        assertNotNull(author);
        assertEquals(request.getPhotoURI(), author.getPhoto().getPhotoFile());
    }

    @Test
    void testCreateAuthorRequestWithoutPhoto() {
        CreateAuthorRequest request = new CreateAuthorRequest(validName, validBio, null, null);
        Author author = new Author(request.getName(), request.getBio(), null);
        assertNotNull(author);
        assertNull(author.getPhoto());
    }

    @Test
    void testGetAuthorNumber() {
        author.setAuthorNumber(42L);
        assertEquals(Long.valueOf(42), author.getAuthorNumber());
    }

    @Test
    void testGetId2() {
        assertEquals(Long.valueOf(1), author.getId());
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
    void testEntityWithPhotoSetPhotoInternalWithValidURI() {
        EntityWithPhoto entity = new EntityWithPhotoImpl();
        String validPhotoURI = "photoTest.jpg";
        entity.setPhoto(validPhotoURI);
        assertNotNull(entity.getPhoto());
    }

    @Test
    void ensurePhotoCanBeNull_AkaOptional() {
        Author author = new Author(validName, validBio, null);
        assertNull(author.getPhoto());
    }

    @Test
    void ensureValidPhoto() {
        Author author = new Author(validName, validBio, "photoTest.jpg");
        Photo photo = author.getPhoto();
        assertNotNull(photo);
        assertEquals("photoTest.jpg", photo.getPhotoFile());
    }


}

