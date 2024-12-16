package pt.psoft.g1.psoftg1.genremanagement.model;

import org.springframework.stereotype.Component;

@Component
public class GenreFactory {
    public static Genre create(String genre){
        return new Genre(genre);
    }

    public static GenreMongo createMongo(String genre){
        return new GenreMongo(genre);
    }
}
