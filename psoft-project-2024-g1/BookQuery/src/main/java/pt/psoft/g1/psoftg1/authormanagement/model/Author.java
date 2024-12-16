package pt.psoft.g1.psoftg1.authormanagement.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.StaleObjectStateException;
import pt.psoft.g1.psoftg1.authormanagement.services.UpdateAuthorRequest;
import pt.psoft.g1.psoftg1.exceptions.ConflictException;
import pt.psoft.g1.psoftg1.shared.model.EntityWithPhoto;
import pt.psoft.g1.psoftg1.shared.model.Generator;
import pt.psoft.g1.psoftg1.shared.model.Name;


@Entity
public class Author extends EntityWithPhoto {
    @Id
    @Column(name = "AUTHOR_NUMBER")
    @Setter
    @Getter
    private String authorNumber;

    @Version
    private long version;

    @Embedded
    private Name name;

    @Embedded
    private Bio bio;

    public void setName(String name) {
        this.name = new Name(name);
    }

    public void setBio(String bio) {
        this.bio = new Bio(bio);
    }

    public Long getVersion() {
        return Long.parseLong(String.valueOf(0)); // Converte long para Long
    }

    public Long getId() {
        return 1L;
    }

    public Author(String name, String bio, String photoURI) {
        this.authorNumber= String.valueOf(Generator.generateLongID());
        setName(name);
        setBio(bio);
        setPhotoInternal(photoURI);
    }

    @PrePersist
    protected void onPrePersist() {
        this.authorNumber= String.valueOf(Generator.generateLongID());
    }

    protected Author() {
        // got ORM only
    }


    public void applyPatch(final long desiredVersion, final UpdateAuthorRequest request) {
        if (this.version != desiredVersion)
            throw new StaleObjectStateException("Object was already modified by another user", this.authorNumber);
        if (request.getName() != null)
            setName(request.getName());
        if (request.getBio() != null)
            setBio(request.getBio());
        if(request.getPhotoURI() != null)
            setPhotoInternal(request.getPhotoURI());
    }

    public void removePhoto(long desiredVersion) {
        if(desiredVersion != this.version) {
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

