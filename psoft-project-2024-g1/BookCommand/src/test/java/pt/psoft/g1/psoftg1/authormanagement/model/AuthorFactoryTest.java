package pt.psoft.g1.psoftg1.authormanagement.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


public class AuthorFactoryTest {

    @BeforeEach
    public void setUp() {
        AuthorFactory authorFactory = new AuthorFactory();
    }

    @Test
    public void testCreate_ReturnsAuthor() {
        String name = "Author Name";
        String bio = "Author Bio";
        String photoURI = "photo-uri";

        Author author = AuthorFactory.create(name, bio, photoURI);

        Assertions.assertNotNull(author);
        Assertions.assertEquals(name, author.getName());
        Assertions.assertEquals(bio, author.getBio());
        assert author.getPhoto() != null;
        Assertions.assertEquals(photoURI, author.getPhoto().getPhotoFile());
    }

    @Test
    public void testCreateAuthorMongo() {
        String name = "Luis";
        String bio = "Autor de muitos livros infantis.";
        String photoURI = "photo-uri";

        AuthorMongo authorMongo = AuthorFactory.createMongo(name, bio, photoURI);

        assertThat(authorMongo).isNotNull();
        assertThat(authorMongo.getName()).isEqualTo(name);
        assertThat(authorMongo.getBio()).isEqualTo(bio);
        assert authorMongo.getPhoto() != null;
        Assertions.assertEquals(photoURI, authorMongo.getPhoto().getPhotoFile());
    }
}
