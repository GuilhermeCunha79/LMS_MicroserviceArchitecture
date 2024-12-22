package pt.psoft.g1.psoftg1.authormanagement.model;

import jakarta.persistence.Id;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.StaleObjectStateException;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import pt.psoft.g1.psoftg1.exceptions.ConflictException;
import pt.psoft.g1.psoftg1.shared.model.EntityWithPhoto;
import pt.psoft.g1.psoftg1.shared.model.Generator;

@Document(collection = "author")
@Data
@NoArgsConstructor
public class AuthorMongo extends EntityWithPhoto {

    @Id
    @Field(targetType = FieldType.INT64, name = "authorNumber")
    @Setter
    @Getter
    private String authorNumber;

    @Version
    public Long version;

    @Field(name = "name")
    public String name;

    @Field(name = "bio")
    public String bio;

    public void setName(String name) {
        this.name = name;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public AuthorMongo(String name, String bio, String photoURI) {
        this.authorNumber= String.valueOf(Generator.generateLongID());
        setName(name);
        setBio(bio);
        setPhotoInternal(photoURI);
    }

    public String getName() {
        return this.name.toString();
    }

    public String getBio() {
        return this.bio.toString();
    }
}
