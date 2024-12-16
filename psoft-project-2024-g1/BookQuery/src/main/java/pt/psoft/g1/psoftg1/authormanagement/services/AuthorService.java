package pt.psoft.g1.psoftg1.authormanagement.services;

import pt.psoft.g1.psoftg1.authormanagement.api.AuthorLendingView;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;

import java.util.List;
import java.util.Optional;

public interface AuthorService {

    Iterable<Author> findAll();

    Optional<Author> findByAuthorNumber(String authorNumber);

    List<Author> findByName(String name);

    Author create(CreateAuthorRequest resource);

    Author partialUpdate(String authorNumber, UpdateAuthorRequest resource, long desiredVersion);

    List<AuthorLendingView> findTopAuthorByLendings();

    List<Book> findBooksByAuthorNumber(String authorNumber);

    List<Author> findCoAuthorsByAuthorNumber(String authorNumber);

    Optional<Author> removeAuthorPhoto(String authorNumber, long desiredVersion);
}
