package pt.psoft.g1.psoftg1.authormanagement.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class AuthorFactoryTest {

    private AuthorFactory authorFactory;
    @BeforeEach
    public void setUp() {
        authorFactory = new AuthorFactory();
    }

    @Test
    public void testCreate_ReturnsAuthor() {
        String name = "Author Name";
        String bio = "Author Bio";
        String photoURI = "photo-uri";

        Author author = authorFactory.create(name, bio, photoURI);

        Assertions.assertNotNull(author);
        Assertions.assertEquals(name, author.getName());
        Assertions.assertEquals(bio, author.getBio());
        assert author.getPhoto() != null;
        Assertions.assertEquals(photoURI, author.getPhoto().getPhotoFile());
    }
}
