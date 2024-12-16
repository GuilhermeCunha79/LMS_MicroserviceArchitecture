package pt.psoft.g1.psoftg1.shared.repositories;


public interface PhotoRepository {
    //Optional<Photo> findById(long id);

    //Photo save(Photo photo);
    void deleteByPhotoFile(String photoFile);
}
