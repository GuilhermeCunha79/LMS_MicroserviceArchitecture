package pt.psoft.g1.psoftg1.bookmanagement.infrastructure.repositories.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.bookmanagement.model.BookMongo;
import pt.psoft.g1.psoftg1.bookmanagement.repositories.BookRepository;
import pt.psoft.g1.psoftg1.bookmanagement.services.BookMapper;

import java.util.Optional;

@Repository
@Profile("mongo")
@RequiredArgsConstructor
public class NoSQLBookRepository implements BookRepository {
    private final MongoTemplate mongoTemplate;
    private final BookMapper bookMapper;

    @Override
    public Book save(Book book) {
        mongoTemplate.save(bookMapper.toBookMongo(book));
        return book;
    }

    @Override
    public void delete(Book book) {
        mongoTemplate.remove(bookMapper.toBookMongo(book));
    }

    @Override
    public Optional<Book> findByIsbn(String isbn) {
        Query query = new Query(Criteria.where("isbn").is(isbn));
        Book book = bookMapper.toBook(mongoTemplate.findOne(query, BookMongo.class));
        return Optional.ofNullable(book);
    }
}
