package pt.psoft.g1.psoftg1.authormanagement.services;

import pt.psoft.g1.psoftg1.authormanagement.api.AuthorLendingView;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.shared.api.AuthorViewAMQP;

import java.util.List;
import java.util.Optional;

public interface AuthorService {

    Author create(AuthorViewAMQP authorViewAMQP); // AMQP request
    Iterable<Author> findAll();

    Optional<Author> findByAuthorNumber(String authorNumber);

    List<Author> findByName(String name);

    List<AuthorLendingView> findTopAuthorByLendings();

    List<Book> findBooksByAuthorNumber(String authorNumber);

    List<Author> findCoAuthorsByAuthorNumber(String authorNumber);

}
