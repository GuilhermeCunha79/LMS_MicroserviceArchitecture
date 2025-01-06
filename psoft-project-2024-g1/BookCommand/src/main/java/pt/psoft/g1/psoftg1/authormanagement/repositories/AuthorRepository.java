package pt.psoft.g1.psoftg1.authormanagement.repositories;

import pt.psoft.g1.psoftg1.authormanagement.model.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorRepository {

    Optional<Author> findByAuthorNumber(String authorNumber);

    Author save(Author author);

    List<Author> searchByNameName(String name);
    void delete(Author author);


}
