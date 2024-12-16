package pt.psoft.g1.psoftg1.authormanagement.publishers;

import pt.psoft.g1.psoftg1.authormanagement.model.Author;

public interface AuthorEventsPublisher {

    void sendAuthorCreated(Author author);

    void sendAuthorUpdated(Author author, Long currentVersion);

}
