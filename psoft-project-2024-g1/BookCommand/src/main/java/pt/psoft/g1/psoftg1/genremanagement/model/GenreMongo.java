package pt.psoft.g1.psoftg1.genremanagement.model;

import jakarta.persistence.Id;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import pt.psoft.g1.psoftg1.shared.model.Generator;


@Document(collection = "genre")
@Data
public class GenreMongo {

    @Id
    @Field(targetType = FieldType.INT64, name = "pk")
    @Setter
    @Getter
    private Long id;

    @Field("genre")
    private String genre;

    public GenreMongo(String genre) {
        this.id= Generator.generateLongID();
        setGenre(genre);
    }

    private void setGenre(String genre) {
        if (genre == null)
            throw new IllegalArgumentException("Genre cannot be null");
        if (genre.isBlank())
            throw new IllegalArgumentException("Genre cannot be blank");
        this.genre = genre;
    }

    @Override
    public String toString() {
        return genre;
    }
}
