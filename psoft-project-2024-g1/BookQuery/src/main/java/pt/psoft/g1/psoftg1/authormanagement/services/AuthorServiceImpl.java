package pt.psoft.g1.psoftg1.authormanagement.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pt.psoft.g1.psoftg1.authormanagement.api.AuthorLendingView;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.authormanagement.repositories.AuthorRepository;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.bookmanagement.repositories.BookRepository;
import pt.psoft.g1.psoftg1.exceptions.ConflictException;
import pt.psoft.g1.psoftg1.shared.api.AuthorViewAMQP;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;

    @Override
    public Author create(AuthorViewAMQP authorViewAMQP) {

        final String authorNumber = authorViewAMQP.getAuthorNumber();
        final String name = authorViewAMQP.getName();
        final String bio = authorViewAMQP.getBio();
        final String photo = authorViewAMQP.getPhoto();
        // Chama o método create com os dados extraídos
        return create(authorNumber, name, bio, photo);
    }

    private Author create(String authorNumber,
                          String name,
                          String bio,
                          String photoURI) {

        if (authorRepository.findByAuthorNumber(authorNumber).isPresent()) {
            throw new ConflictException("Author with AuthorNumber " + authorNumber + " already exists");
        }

        Author newAuthor = new Author(authorNumber, name, bio, photoURI);
        return authorRepository.save(newAuthor);
    }


    @Override
    public Iterable<Author> findAll() {
        return authorRepository.findAll();
    }

    @Override
    public Optional<Author> findByAuthorNumber(final String authorNumber) {
        return authorRepository.findByAuthorNumber(authorNumber);
    }

    @Override
    public List<Author> findByName(String name) {
        return authorRepository.searchByNameNameStartsWith(name);
    }

    @Override
    public List<AuthorLendingView> findTopAuthorByLendings() {
        Pageable pageableRules = PageRequest.of(0, 5);
        return authorRepository.findTopAuthorByLendings(pageableRules).getContent();
    }

    @Override
    public List<Book> findBooksByAuthorNumber(String authorNumber) {
        return bookRepository.findBooksByAuthorNumber(authorNumber);
    }

    @Override
    public List<Author> findCoAuthorsByAuthorNumber(String authorNumber) {
        return authorRepository.findCoAuthorsByAuthorNumber(authorNumber);
    }
}

