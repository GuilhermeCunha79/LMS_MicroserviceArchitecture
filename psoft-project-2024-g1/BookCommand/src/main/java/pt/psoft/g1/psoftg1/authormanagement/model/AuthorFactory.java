package pt.psoft.g1.psoftg1.authormanagement.model;

import org.springframework.stereotype.Component;
import pt.psoft.g1.psoftg1.shared.model.Generator;

@Component
public class AuthorFactory {

    public static Author create(String name, String bio, String photoURI) {
        return new Author(String.valueOf(Generator.generateLongID()), name, bio, photoURI);
    }


    public static AuthorMongo createMongo(String name, String bio, String photoURI) {
        return new AuthorMongo(String.valueOf(Generator.generateLongID()),name, bio, photoURI);
    }
}
