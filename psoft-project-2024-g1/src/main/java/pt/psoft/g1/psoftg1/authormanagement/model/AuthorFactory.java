package pt.psoft.g1.psoftg1.authormanagement.model;

import org.springframework.stereotype.Component;

@Component
public class AuthorFactory {

    public static Author create(String name, String bio, String photoURI) {
        return new Author(name, bio, photoURI);
    }

    public static AuthorMongo createMongo(String name, String bio, String photoURI) {
        return new AuthorMongo(name, bio, photoURI);
    }
}
