package pt.psoft.g1.psoftg1.authormanagement.model;

import org.springframework.stereotype.Component;

@Component
public class AuthorFactory {

    public Author create(String name, String bio, String photoURI) {
        return new Author(name, bio, photoURI);
    }
}
