package pt.psoft.g1.psoftg1.bookmanagement.infrastructure.repositories.impl;

import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.bookmanagement.model.BookMongo;
import pt.psoft.g1.psoftg1.bookmanagement.repositories.BookRepository;
import pt.psoft.g1.psoftg1.bookmanagement.services.BookCountDTO;
import pt.psoft.g1.psoftg1.bookmanagement.services.BookMapper;
import pt.psoft.g1.psoftg1.bookmanagement.services.SearchBooksQuery;
import pt.psoft.g1.psoftg1.genremanagement.model.GenreMongo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@Profile("mongo")
@RequiredArgsConstructor
public class NoSQLBookRepository implements BookRepository {
    private final MongoTemplate mongoTemplate;
    private final BookMapper bookMapper;


    @Override
    public Optional<Book> findByIsbn(String isbn) {
        Query query = new Query(Criteria.where("isbn").is(isbn));
        Book book = bookMapper.toBook(mongoTemplate.findOne(query, BookMongo.class));
        return Optional.ofNullable(book);
    }

    @Override
    public Book save(Book book) {
        mongoTemplate.save(bookMapper.toBookMongo(book));
        return book;
    }

    @Override
    public void delete(Book book) {
        mongoTemplate.remove(bookMapper.toBookMongo(book));
    }
}
