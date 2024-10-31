package pt.psoft.g1.psoftg1.bookmanagement.model;

import com.mongodb.lang.Nullable;
import jakarta.persistence.Embedded;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.StaleObjectStateException;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.authormanagement.model.AuthorMongo;

import pt.psoft.g1.psoftg1.bookmanagement.services.UpdateBookRequestMongo;

import pt.psoft.g1.psoftg1.genremanagement.model.GenreMongo;
import pt.psoft.g1.psoftg1.shared.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Document(collection = "book")
@Data
public class  BookMongo extends EntityWithPhoto {

    @Id
    @Field(targetType = FieldType.INT64, name = "pk")
    Long pk;

    @Embedded
    @Version
    private Long version;

    @Field(name = "isbn")
    private String isbn;

    @Field(name = "title")
    @NotNull
    private String title;

    @Field(name = "description")
    private String description;

    @Field(name = "genre")
    private GenreMongo genre;

    @Field(name = "authors")
    List<AuthorMongo> authors = new ArrayList<>();

    @Field(name = "photoUrl")
    private String photoo;

    public BookMongo(String isbn, String title, String description, GenreMongo genre, List<AuthorMongo> authors,
                      @Nullable Photo photo) {
        this.pk = Generator.generateLongID();
        setTitle(title);
        setIsbn(isbn);
        setDescription(description);
        setGenre(genre);
        setAuthors(authors);
        if(photo!=null){
            setPhotoInternal(photo.getPhotoFile());
        }else{
            setPhotoInternal(null);
        }
    }

    public void applyPatch(final Long desiredVersion, UpdateBookRequestMongo request) {
        if (!Objects.equals(this.version, desiredVersion)) {
            throw new StaleObjectStateException("Object was already modified by another user", this.pk);
        }

        if (request.getTitle() != null) {
            setTitle(request.getTitle());
        }
        if (request.getDescription() != null) {
            setDescription(request.getDescription());
        }
        if (request.getGenreObj() != null) {
            setGenre(request.getGenreObj());
        }
        if (request.getAuthorObjList() != null) {
            setAuthors(request.getAuthorObjList());
        }
        if (request.getPhotoURI() != null) {
            setPhotoInternal(request.getPhotoURI());
        }
    }


    private void setIsbn(String title) {
        this.isbn = title;
    }

    private void setTitle(String title) {
        this.title = title;
    }

    private void setDescription(String description) {
        this.description = description;
    }

    private void setGenre(GenreMongo genre) {
        this.genre = genre;
    }

    private void setAuthors(List<AuthorMongo> authors) {
        if (authors == null || authors.isEmpty()) {
            throw new IllegalArgumentException("Author list cannot be null or empty");
        }
        this.authors = authors;
    }
}