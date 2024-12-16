package pt.psoft.g1.psoftg1.shared.repositories;

import org.springframework.data.repository.query.Param;
import pt.psoft.g1.psoftg1.shared.model.Photo;
import java.util.Optional;


public interface PhotoRepository {
    //Optional<Photo> findById(long id);

    //Photo save(Photo photo);
    void deleteByPhotoFile(String photoFile);
}
