package pt.psoft.g1.psoftg1.authormanagement.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.authormanagement.model.AuthorFactory;
import pt.psoft.g1.psoftg1.authormanagement.repositories.AuthorRepository;

import java.util.Optional;

import static org.junit.Assert.*;
@SpringBootTest
@Transactional
public class AuthorRepositoryIntegrationTest {

    /*
    @Autowired
    private AuthorRepository authorRepository;

    @Test
    public void whenSaveAuthor_thenAuthorIsSaved() {
        // Arrange
        Author author = AuthorFactory.create("John Doe", "A famous author", "photo.jpg");

        // Act
        Author savedAuthor = authorRepository.save(author);

        // Assert
        assertNotNull(savedAuthor.getAuthorNumber());
        assertEquals("Author Name", savedAuthor.getName());
    }

    @Test
    public void whenFindById_thenReturnAuthor() {
        // Arrange
        Author author = new Author("Author Name", "Biography", "photo.jpg");
        Author savedAuthor = authorRepository.save(author);

        // Act
        Optional<Author> retrievedAuthor = authorRepository.findByAuthorNumber(savedAuthor.getAuthorNumber());

        // Assert
        assertTrue(retrievedAuthor.isPresent());
        assertEquals("Author Name", retrievedAuthor.get().getName());
    }

     */
}
