package pt.psoft.g1.psoftg1.bookmanagement.services;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.authormanagement.model.AuthorMongo;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.bookmanagement.model.BookMongo;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;
import pt.psoft.g1.psoftg1.genremanagement.model.GenreMongo;
import pt.psoft.g1.psoftg1.shared.api.MapperInterface;

@Mapper(componentModel = "spring")
public abstract class BookMapper extends MapperInterface {

    public abstract Book toBook(BookMongo bookMongo);

    @Mapping(target = "version", ignore = true)
    public abstract BookMongo toBookMongo(Book book);

    protected abstract Author mapAuthorById(Long authorId);

    protected abstract AuthorMongo mapAuthorMongoById(Long authorId);

    protected abstract Genre mapGenre(String genre);

    protected abstract GenreMongo mapGenreMongo(String genre);



}