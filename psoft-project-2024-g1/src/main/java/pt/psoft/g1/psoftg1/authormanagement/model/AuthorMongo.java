package pt.psoft.g1.psoftg1.authormanagement.model;

import jakarta.persistence.Embedded;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.StaleObjectStateException;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.FieldType;
import pt.psoft.g1.psoftg1.authormanagement.services.UpdateAuthorRequest;
import pt.psoft.g1.psoftg1.exceptions.ConflictException;
import pt.psoft.g1.psoftg1.shared.model.EntityWithPhoto;
import pt.psoft.g1.psoftg1.shared.model.Generator;
import pt.psoft.g1.psoftg1.shared.model.Name;

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

    public void applyPatch(final long desiredVersion, final UpdateAuthorRequest request) {
        if (this.version != desiredVersion) {
            throw new StaleObjectStateException("Object was already modified by another user", this.authorNumber);
        }
        if (request.getName() != null) {
            setName(request.getName());
        }
        if (request.getBio() != null) {
            setBio(request.getBio());
        }
        if (request.getPhotoURI() != null) {
            setPhotoInternal(request.getPhotoURI());
        }
    }

    public void removePhoto(long desiredVersion) {
        if (desiredVersion != this.version) {
            throw new ConflictException("Provided version does not match latest version of this object");
        }
        setPhotoInternal(null);
    }

    public String getName() {
        return this.name.toString();
    }

    public String getBio() {
        return this.bio.toString();
    }
}
