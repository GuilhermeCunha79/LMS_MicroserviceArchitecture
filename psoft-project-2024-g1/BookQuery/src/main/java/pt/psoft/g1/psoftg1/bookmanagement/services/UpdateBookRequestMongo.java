package pt.psoft.g1.psoftg1.bookmanagement.services;

import jakarta.annotation.Nullable;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;
import pt.psoft.g1.psoftg1.authormanagement.model.AuthorMongo;
import pt.psoft.g1.psoftg1.genremanagement.model.GenreMongo;

import java.util.List;

@Getter
@Data
@NoArgsConstructor
public class UpdateBookRequestMongo {
    @Setter
    private String isbn;

    @Setter
    private String description;

    private String title;

    @Nullable
    @Setter
    private String photoURI;

    @Nullable
    @Getter
    @Setter
    private MultipartFile photo;


    @Setter
    private GenreMongo genreObj;

    private String genre;

    private List<Long> authors;

    private List<AuthorMongo> authorObjList;

    public UpdateBookRequestMongo(String isbn, String title, String genre, @NonNull List<Long> authors, String description) {
        this.isbn = isbn;
        this.genre = genre;
        this.title = title;
        this.description = description;
        this.authors = authors;
    }
}
