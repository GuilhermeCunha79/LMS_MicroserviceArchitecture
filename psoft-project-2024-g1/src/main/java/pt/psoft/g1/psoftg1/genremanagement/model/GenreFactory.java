package pt.psoft.g1.psoftg1.genremanagement.model;

import org.springframework.stereotype.Component;

@Component
public class GenreFactory {
    public Genre create(String genre){
        return new Genre(genre);
    }
}
