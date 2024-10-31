package pt.psoft.g1.psoftg1.shared.model;

import com.mongodb.lang.Nullable;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.nio.file.Path;

@Getter
@Document(collection = "photoMongo")
@NoArgsConstructor
public class PhotoMongo {
    @Id
    private Long id;

    @Nullable
    @Field
    private String photoFile;


    public PhotoMongo(Path photoPath) {
        this.id = Generator.generateLongID();
        this.photoFile = photoPath.toString();
    }


}