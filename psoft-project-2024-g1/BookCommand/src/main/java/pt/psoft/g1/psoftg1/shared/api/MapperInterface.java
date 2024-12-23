package pt.psoft.g1.psoftg1.shared.api;

import org.mapstruct.Named;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public abstract class MapperInterface {

    public <T> String map(final T value) {
        if (value == null)
            return null;
        return value.toString();}

    public <T extends Number> Number map(final T value) {
        if(value instanceof Double)
            return value.doubleValue();
        if(value instanceof Integer)
            return value.intValue();
        if(value instanceof Long)
            return value.longValue();
        else throw new NumberFormatException("Invalid number format");
    }

    @Named(value = "bookLink")
    protected Map<String, String> mapBookLink(Book book){
        Map<String, String> bookLink = new HashMap<>();
        String bookUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/books/")
                .path(book.getIsbn())
                .toUriString();
        bookLink.put("href", bookUri);
        return bookLink;
    }

    @Named(value = "authorLink")
    protected Map<String, String> mapAuthorLink(Author author){
        Map<String, String> authorLink = new HashMap<>();
        String authorUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/authors/")
                .path(author.getAuthorNumber().toString())
                .toUriString();
        authorLink.put("href", authorUri);
        return authorLink;
    }

    public <T> T mapOpt(final Optional<T> i) {return i.orElse(null);}
}
