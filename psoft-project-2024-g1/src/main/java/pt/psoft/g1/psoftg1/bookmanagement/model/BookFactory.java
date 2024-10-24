package pt.psoft.g1.psoftg1.bookmanagement.model;

import org.springframework.stereotype.Component;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;

import java.util.List;

@Component
public class BookFactory {

    public Book create(String isbn, String title, String description, Genre genre, List<Author> authors, String photoURI){
        return new Book(isbn, title, description, genre, authors,photoURI);
    }
}
