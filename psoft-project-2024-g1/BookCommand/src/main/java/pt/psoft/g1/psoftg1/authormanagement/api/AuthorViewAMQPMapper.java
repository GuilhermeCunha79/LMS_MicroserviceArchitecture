package pt.psoft.g1.psoftg1.authormanagement.api;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.bookmanagement.api.BookViewAMQP;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.shared.api.MapperInterface;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public abstract class AuthorViewAMQPMapper extends MapperInterface {

    @Mapping(target = "authorNumber", source = "authorNumber")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "bio", source = "bio")
    @Mapping(target = "photo", source = "photo")
    @Mapping(target = "version", source = "version")

    public abstract AuthorViewAMQP toAuthorViewAMQP(Author author);

    public abstract List<AuthorViewAMQP> toAuthorViewAMQP(List<Author> authors);

}

