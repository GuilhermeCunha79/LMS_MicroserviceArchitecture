package pt.psoft.g1.psoftg1.authormanagement.services;

import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.shared.api.AuthorViewAMQP;

import java.util.Optional;

public interface AuthorService {

    Author create(CreateAuthorRequest resource);
    Author create(AuthorViewAMQP authorViewAMQP); // AMQP request
    Author partialUpdate(String authorNumber, UpdateAuthorRequest resource, long desiredVersion);
    Optional<Author> findByAuthorNumber(final String authorNumber);
    Optional<Author> removeAuthorPhoto(String authorNumber, long desiredVersion);
}
