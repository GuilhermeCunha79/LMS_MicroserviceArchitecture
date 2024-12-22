package pt.psoft.g1.psoftg1.bookmanagement.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.authormanagement.repositories.AuthorRepository;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.bookmanagement.repositories.BookRepository;
import pt.psoft.g1.psoftg1.exceptions.ConflictException;
import pt.psoft.g1.psoftg1.exceptions.NotFoundException;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;
import pt.psoft.g1.psoftg1.genremanagement.repositories.GenreRepository;
import pt.psoft.g1.psoftg1.shared.api.BookViewAMQP;
import pt.psoft.g1.psoftg1.shared.services.Page;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

	private final BookRepository bookRepository;
	private final GenreRepository genreRepository;
	private final AuthorRepository authorRepository;

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

	private Book create( String isbn,
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

	private Book update( Book book,
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
		}
		else
			authors = null;

		book.applyPatch(currentVersion, title, description, photoURI, genreObj, authors);

        return bookRepository.save(book);
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


	@Override
	public Book save(Book book) {
		return this.bookRepository.save(book);
	}

	@Override
	public List<Book> findByGenre(String genre) {
		return this.bookRepository.findByGenre(genre);
	}

	public List<Book> findByTitle(String title) {
		return bookRepository.findByTitle(title);
	}

	@Override
	public List<Book> findByAuthorName(String authorName) {
		return bookRepository.findByAuthorName(authorName + "%");
	}

	public Book findByIsbn(String isbn) {
		return this.bookRepository.findByIsbn(isbn)
				.orElseThrow(() -> new NotFoundException(Book.class, isbn));
	}

	@Override
	public List<Book> searchBooks(Page page, SearchBooksQuery query) {
		if (page == null) {
			page = new Page(1, 10);
		}
		if (query == null) {
			query = new SearchBooksQuery("", "", "");
		}
		return bookRepository.searchBooks(page, query);
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
