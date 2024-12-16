package pt.psoft.g1.psoftg1.bookmanagement.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.authormanagement.repositories.AuthorRepository;
import pt.psoft.g1.psoftg1.bookmanagement.api.BookViewAMQP;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.bookmanagement.publishers.BookEventsPublisher;
import pt.psoft.g1.psoftg1.bookmanagement.repositories.BookRepository;
import pt.psoft.g1.psoftg1.exceptions.ConflictException;
import pt.psoft.g1.psoftg1.exceptions.NotFoundException;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;
import pt.psoft.g1.psoftg1.genremanagement.repositories.GenreRepository;
import pt.psoft.g1.psoftg1.shared.model.LendingStatus;
import pt.psoft.g1.psoftg1.shared.api.LendingViewAMQP;
import pt.psoft.g1.psoftg1.shared.repositories.PhotoRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final GenreRepository genreRepository;
    private final AuthorRepository authorRepository;
    private final PhotoRepository photoRepository;
    private final BookEventsPublisher bookEventsPublisher;


    @Value("${suggestionsLimitPerGenre}")
    private long suggestionsLimitPerGenre;

    @Override
    public Book create(CreateBookRequest request, String isbn) {

        if (bookRepository.findByIsbn(isbn).isPresent()) {
            throw new ConflictException("Book with ISBN " + isbn + " already exists");
        }

        List<Long> authorNumbers = request.getAuthors();
        List<Author> authors = new ArrayList<>();
        for (Long authorNumber : authorNumbers) {

            Optional<Author> temp = authorRepository.findByAuthorNumber(String.valueOf(authorNumber));
            if (temp.isEmpty()) {
                continue;
            }

            Author author = temp.get();
            authors.add(author);
        }

        MultipartFile photo = request.getPhoto();
        String photoURI = request.getPhotoURI();
        if (photo == null && photoURI != null || photo != null && photoURI == null) {
            request.setPhoto(null);
            request.setPhotoURI(null);
        }

        final var genre = genreRepository.findByString(request.getGenre())
                .orElseThrow(() -> new NotFoundException("Genre not found"));

        Book newBook = new Book(isbn, request.getTitle(), request.getDescription(), genre, authors, photoURI);

        bookEventsPublisher.sendBookCreated(newBook);

        return bookRepository.save(newBook);
    }

    @Override
    public Book create(BookViewAMQP bookViewAMQP) {

        final String isbn = bookViewAMQP.getIsbn();
        final String description = bookViewAMQP.getDescription();
        final String title = bookViewAMQP.getTitle();
        final String photoURI = null;
        final String genre = bookViewAMQP.getGenre();
        final List<String> authorIds = bookViewAMQP.getAuthorIds();

        return create(isbn, title, description, photoURI, genre, authorIds);
    }

    private Book create(String isbn,
                        String title,
                        String description,
                        String photoURI,
                        String genreName,
                        List<String> authorIds) {

        if (bookRepository.findByIsbn(isbn).isPresent()) {
            throw new ConflictException("Book with ISBN " + isbn + " already exists");
        }

        List<Author> authors = getAuthors(authorIds);

        final Genre genre = genreRepository.findByString(String.valueOf(genreName))
                .orElseThrow(() -> new NotFoundException("Genre not found"));

        Book newBook = new Book(isbn, title, description, genre, authors, photoURI);

        return bookRepository.save(newBook);
    }

    @Override
    public boolean verifyIfIsbnExists(LendingViewAMQP lendingViewAMQP) {
        boolean exists = bookRepository.findByIsbn(lendingViewAMQP.getIsbn()).isPresent();
        lendingViewAMQP.setStatus(exists ? LendingStatus.LENDING_VALIDATED_BOOKS : LendingStatus.LENDING_INVALIDATED);
        bookEventsPublisher.sendBookValidated(lendingViewAMQP);
        return exists;
    }



    @Override
    public Book update(UpdateBookRequest request, Long currentVersion) {

        var book = findByIsbn(request.getIsbn());
        if (request.getAuthors() != null) {
            List<String> authorNumbers = request.getAuthors();
            List<Author> authors = new ArrayList<>();
            for (String authorNumber : authorNumbers) {
                Optional<Author> temp = authorRepository.findByAuthorNumber(authorNumber);
                if (temp.isEmpty()) {
                    continue;
                }
                Author author = temp.get();
                authors.add(author);
            }

            request.setAuthorObjList(authors);
        }

        MultipartFile photo = request.getPhoto();
        String photoURI = request.getPhotoURI();
        if (photo == null && photoURI != null || photo != null && photoURI == null) {
            request.setPhoto(null);
            request.setPhotoURI(null);
        }

        if (request.getGenre() != null) {
            Optional<Genre> genre = genreRepository.findByString(request.getGenre());
            if (genre.isEmpty()) {
                throw new NotFoundException("Genre not found");
            }
            request.setGenreObj(genre.get());
        }
        Book updatedBook = update(book, currentVersion, request.getTitle(), request.getDescription(), photoURI, request.getGenreObj().getGenre(), request.getAuthors());

        if (updatedBook != null) {
            bookEventsPublisher.sendBookUpdated(updatedBook, currentVersion);
        }

        return book;
    }

    @Override
    public Book update(BookViewAMQP bookViewAMQP) {

        final Long version = bookViewAMQP.getVersion();
        final String isbn = bookViewAMQP.getIsbn();
        final String description = bookViewAMQP.getDescription();
        final String title = bookViewAMQP.getTitle();
        final String photoURI = null;
        final String genre = bookViewAMQP.getGenre();
        final List<String> authorIds = bookViewAMQP.getAuthorIds();

        var book = findByIsbn(isbn);

        return update(book, version, title, description, photoURI, genre, authorIds);
    }


    private Book update(Book book,
                        Long currentVersion,
                        String title,
                        String description,
                        String photoURI,
                        String genreId,
                        List<String> authorsId) {

        Genre genreObj = null;
        if (genreId != null) {
            Optional<Genre> genre = genreRepository.findByString(genreId);
            if (genre.isEmpty()) {
                throw new NotFoundException("Genre not found");
            }
            genreObj = genre.get();
        }

        List<Author> authors = new ArrayList<>();
        if (authorsId != null) {
            for (String authorNumber : authorsId) {
                Optional<Author> temp = authorRepository.findByAuthorNumber(authorNumber);
                if (temp.isEmpty()) {
                    continue;
                }
                Author author = temp.get();
                authors.add(author);
            }
        } else
            authors = null;

        book.applyPatch(currentVersion, title, description, photoURI, genreObj, authors);

        return bookRepository.save(book);
    }


    @Override
    public Book removeBookPhoto(String isbn, long desiredVersion) {
        Book book = this.findByIsbn(isbn);
        String photoFile;
        try {
            photoFile = book.getPhoto().getPhotoFile();
        } catch (NullPointerException e) {
            throw new NotFoundException("Book did not have a photo assigned to it.");
        }

        book.removePhoto(desiredVersion);

        var deletedBook = bookRepository.save(book);
        if (deletedBook != null) {
            photoRepository.deleteByPhotoFile(photoFile);

            bookEventsPublisher.sendBookDeleted(deletedBook, desiredVersion);
        }
        return deletedBook;
    }

    public Book findByIsbn(String isbn) {
        return this.bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> new NotFoundException(Book.class, isbn));
    }

    private List<Author> getAuthors(List<String> authorNumbers) {

        List<Author> authors = new ArrayList<>();
        for (String authorNumber : authorNumbers) {

            Optional<Author> temp = authorRepository.findByAuthorNumber(authorNumber);
            if (temp.isEmpty()) {
                continue;
            }

            Author author = temp.get();
            authors.add(author);
        }

        return authors;
    }
}
