package pt.psoft.g1.psoftg1.authormanagement.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import pt.psoft.g1.psoftg1.shared.model.StringUtilsCustom;

@Embeddable
public class Bio {

    @Column(nullable = false, length = 4096)
    @NotNull
    @Size(min = 1, max = 4096)
    @Getter
    private String bio;

    public Bio(String bio) {
        setBio(bio);
    }

    protected Bio() {
    }

    public void setBio(String bio) {
        if(bio == null)
            throw new IllegalArgumentException("Bio cannot be null");
        if(bio.isBlank())
            throw new IllegalArgumentException("Bio cannot be blank");
        if(bio.length() > 4096)
            throw new IllegalArgumentException("Bio has a maximum of 4096 characters");
        this.bio = StringUtilsCustom.sanitizeHtml(bio);
    }

    @Override
    public String toString() {
        return bio;
    }
}

