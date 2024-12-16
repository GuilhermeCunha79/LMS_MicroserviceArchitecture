package pt.psoft.g1.psoftg1.authormanagement.api;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.bookmanagement.api.BookShortView;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.shared.api.MapperInterface;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mapper(componentModel = "spring")
public abstract class AuthorViewMapper extends MapperInterface {

    @Mapping(target = "_links", expression = "java(mapLinks(author))")
    public abstract AuthorView toAuthorView(Author author);

    public abstract List<AuthorView> toAuthorView(List<Author> authors);

    @Mapping(target = "_links", source = "author", qualifiedByName = "mapAuthorLinks")
    @Mapping(target = "books", source = "books", qualifiedByName = "toBookShortViewList")
    public abstract CoAuthorView toCoAuthorView(Author author, List<Book> books);

    @Named(value = "toBookShortView")
    @Mapping(target = "_links", source = ".", qualifiedByName = "mapBookShortLink")
    public abstract BookShortView toBookShortView(Book book);

    @Named(value = "toBookShortViewList")
    @IterableMapping(qualifiedByName = "toBookShortView")
    public abstract List<BookShortView> toBookShortView(List<Book> books);

    public abstract AuthorCoAuthorBooksView toAuthorCoAuthorBooksView(Author author, List<CoAuthorView> coauthors);

    @Named(value = "mapAuthorLinks")
    public Map<String, Object> mapLinks(final Author author){
        String authorUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/authors/")
                .path(author.getId().toString())
                .toUriString();

        String booksByAuthorUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/authors/")
                .path(author.getId().toString())
                .path("/books")
                .toUriString();

        Map<String, Object> links = new HashMap<>();

        links.put("author", authorUri);
        links.put("photo", generatePhotoUrl(author));
        links.put("booksByAuthor", booksByAuthorUri);

        return links;
    }

    protected String generatePhotoUrl(Author author) {
        String authorNumber = author.getAuthorNumber();
        return ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/authors/{authorNumber}/photo").buildAndExpand(authorNumber).toUri().toString();
    }

    @Named(value = "mapBookShortLink")
    public String mapShortBookLink(final Book book) {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/books/")
                .path(book.getIsbn())
                .toUriString();
    }

}
