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
import pt.psoft.g1.psoftg1.shared.model.Generator;
import pt.psoft.g1.psoftg1.shared.model.Photo;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthorMongoTest {
    private AuthorMongo author;
    private final String validName = "João Alberto";
    private final String validBio = "O João Alberto nasceu em Chaves e foi pedreiro a maior parte da sua vida.";
    @Mock
    private final UpdateAuthorRequest request = new UpdateAuthorRequest(validName, validBio, null, null);

    private static class EntityWithPhotoImpl extends EntityWithPhoto { }

    @BeforeEach
    void setup() {
        // Configuração inicial do teste, criando uma instância de AuthorMongo com dados válidos.
        author = new AuthorMongo(String.valueOf(Generator.generateLongID()),"John Doe", "Biography of John", "photoURI");
    }

    @Test
    void testConstructor() {
        // Teste de caixa preta: verifica se o construtor inicializa corretamente os campos.
        assertEquals("John Doe", author.getName());
        assertEquals("Biography of John", author.getBio());
        assertEquals("photoURI", author.getPhoto().getPhotoFile());
    }

    @Test
    void applyPatchGetNameNull() {
        // Teste de caixa branca: verifica a lógica do método applyPatch ao passar nome nulo.
        final var subject = new Author(String.valueOf(Generator.generateLongID()),"Valid Name", validBio, null);
        UpdateAuthorRequest request = new UpdateAuthorRequest();
        request.setName(null);

        assertThrows(StaleObjectStateException.class, () -> subject.applyPatch(999, request));
    }

    @Test
    void applyPatchGetBioNull() {
        // Teste de caixa branca: verifica a lógica do método applyPatch ao passar bio nula.
        final var subject = new Author(String.valueOf(Generator.generateLongID()),"Valid Name", validBio, null);
        UpdateAuthorRequest request = new UpdateAuthorRequest();
        request.setBio(null);

        assertThrows(StaleObjectStateException.class, () -> subject.applyPatch(999, request));
    }

    @Test
    void applyPatchGetPhotoUriNull() {
        // Teste de caixa branca: assegura que uma exceção é lançada se photoURI for nulo.
        final var subject = new Author(String.valueOf(Generator.generateLongID()),"Valid Name", validBio, null);
        UpdateAuthorRequest request = new UpdateAuthorRequest();
        request.setPhotoURI(null);

        assertThrows(StaleObjectStateException.class, () -> subject.applyPatch(999, request));
    }

    @Test
    void removePhotoVersionMismatch() {
        // Teste de caixa branca: verifica se a versão correta é usada ao remover uma foto.
        Author author = new Author(String.valueOf(Generator.generateLongID()),"Valid Name", "Valid Bio", "photoURI");
        long wrongVersion = 999L;

        assertThrows(ConflictException.class, () -> {
            // Verifica se a remoção de foto com versão errada lança uma exceção.
            author.removePhoto(wrongVersion);
        });
    }

    @Test
    void ensureNameNotNull() {
        // Teste de caixa preta: assegura que um nome nulo gera uma exceção no construtor.
        assertThrows(IllegalArgumentException.class, () -> new Author(String.valueOf(Generator.generateLongID()),null, validBio, null));
    }

    @Test
    void ensureBioNotNull() {
        // Teste de caixa preta: assegura que uma biografia nula gera uma exceção no construtor.
        assertThrows(IllegalArgumentException.class, () -> new Author(String.valueOf(Generator.generateLongID()),validName, null, null));
    }

    @Test
    void whenVersionIsStaleItIsNotPossibleToPatch() {
        // Teste de caixa branca: assegura que uma versão desatualizada gera uma exceção ao tentar aplicar um patch.
        final var subject = new Author(String.valueOf(Generator.generateLongID()),validName, validBio, null);
        assertThrows(StaleObjectStateException.class, () -> subject.applyPatch(999, request));
    }

    @Test
    void testCreateAuthorWithoutPhoto() {
        // Teste de caixa preta: verifica a criação de um autor sem foto.
        Author author = new Author(String.valueOf(Generator.generateLongID()),validName, validBio, null);
        assertNotNull(author);
        assertNull(author.getPhoto());
    }

    @Test
    void testCreateAuthorRequestWithPhoto() {
        // Teste de caixa preta: verifica a criação de um autor a partir de um CreateAuthorRequest com foto.
        CreateAuthorRequest request = new CreateAuthorRequest(validName, validBio, null, "photoTest.jpg");
        Author author = new Author(String.valueOf(Generator.generateLongID()),request.getName(), request.getBio(), "photoTest.jpg");
        assertNotNull(author);
        assertEquals(request.getPhotoURI(), author.getPhoto().getPhotoFile());
    }

    @Test
    void testCreateAuthorRequestWithoutPhoto() {
        // Teste de caixa preta: verifica a criação de um autor a partir de um CreateAuthorRequest sem foto.
        CreateAuthorRequest request = new CreateAuthorRequest(validName, validBio, null, null);
        Author author = new Author(String.valueOf(Generator.generateLongID()),request.getName(), request.getBio(), null);
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
        Author author = new Author(String.valueOf(Generator.generateLongID()),validName, validBio, null);
        assertNull(author.getPhoto());
    }

    @Test
    void ensureValidPhoto() {
        // Teste de caixa preta: verifica se um autor pode ser criado com uma foto válida.
        Author author = new Author(String.valueOf(Generator.generateLongID()),validName, validBio, "photoTest.jpg");
        Photo photo = author.getPhoto();
        assertNotNull(photo);
        assertEquals("photoTest.jpg", photo.getPhotoFile());
    }
}