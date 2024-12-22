package pt.psoft.g1.psoftg1.authormanagement.model;

import org.hibernate.StaleObjectStateException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import pt.psoft.g1.psoftg1.exceptions.ConflictException;
import pt.psoft.g1.psoftg1.shared.model.EntityWithPhoto;
import pt.psoft.g1.psoftg1.shared.model.Photo;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class AuthorMongoTest {
    private AuthorMongo author;
    private final String validName = "João Alberto";
    private final String validBio = "O João Alberto nasceu em Chaves e foi pedreiro a maior parte da sua vida.";

    private static class EntityWithPhotoImpl extends EntityWithPhoto { }

    @BeforeEach
    void setup() {
        // Configuração inicial do teste, criando uma instância de AuthorMongo com dados válidos.
        author = new AuthorMongo("John Doe", "Biography of John", "photoURI");
    }

    @Test
    void testConstructor() {
        // Teste de caixa preta: verifica se o construtor inicializa corretamente os campos.
        assertEquals("John Doe", author.getName());
        assertEquals("Biography of John", author.getBio());
        assertEquals("photoURI", author.getPhoto().getPhotoFile());
    }

    @Test
    void removePhotoVersionMismatch() {
        // Teste de caixa branca: verifica se a versão correta é usada ao remover uma foto.
        Author author = new Author("Valid Name", "Valid Bio", "photoURI");
        long wrongVersion = 999L;

        assertThrows(ConflictException.class, () -> {
            // Verifica se a remoção de foto com versão errada lança uma exceção.
            author.removePhoto(wrongVersion);
        });
    }

    @Test
    void ensureNameNotNull() {
        // Teste de caixa preta: assegura que um nome nulo gera uma exceção no construtor.
        assertThrows(IllegalArgumentException.class, () -> new Author(null, validBio, null));
    }

    @Test
    void ensureBioNotNull() {
        // Teste de caixa preta: assegura que uma biografia nula gera uma exceção no construtor.
        assertThrows(IllegalArgumentException.class, () -> new Author(validName, null, null));
    }
    @Test
    void testCreateAuthorWithoutPhoto() {
        // Teste de caixa preta: verifica a criação de um autor sem foto.
        Author author = new Author(validName, validBio, null);
        assertNotNull(author);
        assertNull(author.getPhoto());
    }

    @Test
    void testGetAuthorNumber() {
        // Teste de caixa branca: verifica a lógica do método getAuthorNumber.
        author.setAuthorNumber(String.valueOf(42L));
        assertEquals(String.valueOf(42), author.getAuthorNumber());
    }

    @Test
    void testSetName() {
        // Teste de caixa branca: assegura que o método setName atualiza corretamente o nome.
        author.setName("New Author Name");
        assertEquals("New Author Name", author.getName());
    }

    @Test
    void testSetBio() {
        // Teste de caixa branca: assegura que o método setBio atualiza corretamente a biografia.
        author.setBio("New Author Bio");
        assertEquals("New Author Bio", author.getBio());
    }

    @Test
    void testEntityWithPhotoSetPhotoInternalWithValidURI() {
        // Teste de caixa branca: verifica a lógica de setPhoto na classe interna EntityWithPhotoImpl.
        EntityWithPhoto entity = new EntityWithPhotoImpl();
        String validPhotoURI = "photoTest.jpg";
        entity.setPhoto(validPhotoURI);
        assertNotNull(entity.getPhoto());
    }

    @Test
    void ensurePhotoCanBeNull_AkaOptional() {
        // Teste de caixa preta: verifica se a foto pode ser nula sem causar exceções.
        Author author = new Author(validName, validBio, null);
        assertNull(author.getPhoto());
    }

    @Test
    void ensureValidPhoto() {
        // Teste de caixa preta: verifica se um autor pode ser criado com uma foto válida.
        Author author = new Author(validName, validBio, "photoTest.jpg");
        Photo photo = author.getPhoto();
        assertNotNull(photo);
        assertEquals("photoTest.jpg", photo.getPhotoFile());
    }
}