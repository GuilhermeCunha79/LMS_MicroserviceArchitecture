package pt.psoft.g1.psoftg1.shared.model;

import jakarta.annotation.Nullable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.nio.file.InvalidPathException;
import java.nio.file.Path;

@Getter
public abstract class EntityWithPhotoMongo {
    @Nullable
    private PhotoMongo photo;

    // Método usado pelo mapper para definir a foto
    public void setPhoto(String photoUri) {
        this.setPhotoInternal(photoUri);
    }

    protected void setPhotoInternal(String photoURI) {
        if (photoURI == null) {
            this.photo = null;
        } else {
            // Criação do objeto Photo com base na URI da foto
            try {
                this.photo = new PhotoMongo(Path.of(photoURI));
            } catch (InvalidPathException e) {
                // Se falhar, define photo como null
                this.photo = null;
            }
        }
    }
}