package pt.psoft.g1.psoftg1.bookmanagement.services;

import org.mapstruct.Mapper;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.bookmanagement.model.BookMongo;
import pt.psoft.g1.psoftg1.shared.api.MapperInterface;

@Mapper(componentModel = "spring")
public abstract class BookMapper extends MapperInterface {

    public abstract Book toBook(BookMongo authorMongo);

    public abstract BookMongo toBookMongo(Book author);
}
