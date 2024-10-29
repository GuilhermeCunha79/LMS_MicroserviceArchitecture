package pt.psoft.g1.psoftg1.bookmanagement.infrastructure.repositories.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;

import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.bookmanagement.repositories.BookRepository;
import pt.psoft.g1.psoftg1.bookmanagement.services.BookCountDTO;
import pt.psoft.g1.psoftg1.bookmanagement.services.BookMapper;
import pt.psoft.g1.psoftg1.bookmanagement.services.SearchBooksQuery;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;
import pt.psoft.g1.psoftg1.lendingmanagement.model.Lending;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component("bookMongo")
@Primary
public class NoSQLBookRepository implements BookRepository {
    private final MongoTemplate mongoTemplate;
    private final BookMapper bookMapper;

    @Autowired
    public NoSQLBookRepository(MongoTemplate mongoTemplate, BookMapper bookMapper) {
        this.mongoTemplate = mongoTemplate;
        this.bookMapper = bookMapper;
    }

    @Override
    public Optional<Book> findByIsbn(String isbn) {
        Query query = new Query(Criteria.where("isbn").is(isbn));
        Book book = mongoTemplate.findOne(query, Book.class);
        return Optional.ofNullable(book);
    }

    @Override
    public Page<BookCountDTO> findTop5BooksLent(LocalDate oneYearAgo, Pageable pageable) {
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("lendings.startDate").gt(oneYearAgo)),
                Aggregation.group("book").count().as("lendingCount"),
                Aggregation.sort(Sort.by(Sort.Direction.DESC, "lendingCount")),
                Aggregation.limit(5)
        );

        AggregationResults<BookCountDTO> results = mongoTemplate.aggregate(aggregation, Lending.class, BookCountDTO.class);
        return new PageImpl<>(results.getMappedResults(), pageable, results.getMappedResults().size());
    }

    @Override
    public List<Book> findByGenre(String genre) {
        Query query = new Query(Criteria.where("genre.genre").regex(genre, "i"));
        List<Book> bookList = mongoTemplate.find(query, Book.class);
        return bookList;

    }

    @Override
    public Book save(Book book) {
        mongoTemplate.save(book);
        return book;
    }

    @Override
    public void delete(Book book) {
        mongoTemplate.remove(book);
    }

    @Override
    public List<Book> findXBooksByYGenre(int x, int y) {
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.group("genre.pk").count().as("bookCount"),
                Aggregation.sort(Sort.by(Sort.Direction.DESC, "bookCount")),
                Aggregation.limit(y),
                Aggregation.lookup("books", "_id", "genre.pk", "books"),
                Aggregation.unwind("books"),
                Aggregation.limit(x)
        );

        AggregationResults<Book> results = mongoTemplate.aggregate(aggregation, Genre.class, Book.class);

        return results.getMappedResults();
    }

    @Override
    public List<Book> findByTitle(String title) {
        Query query = new Query(Criteria.where("title.title").regex(title, "i"));
        List<Book> bookList = mongoTemplate.find(query, Book.class);
        return bookList;
    }

    @Override
    public List<Book> findByAuthorName(String authorName) {
        Query query = new Query(Criteria.where("authors.name").regex(authorName, "i"));
        List<Book> bookList = mongoTemplate.find(query, Book.class);
        return bookList;
    }

    @Override
    public List<Book> findBooksByAuthorNumber(Long authorNumber) {
        Query query = new Query(Criteria.where("authors.authorNumber").is(authorNumber));
        List<Book> bookList = mongoTemplate.find(query, Book.class);
        return bookList;
    }

    @Override
    public List<Book> findXBooksByGenre(String genre, int x) {
        Query query = new Query();
        query.addCriteria(Criteria.where("genre.genre").regex(genre, "i"));
        query.limit(x);
        return mongoTemplate.find(query, Book.class);
    }

    @Override
    public List<Book> findTopXBooksFromMostLentGenreByReader(String readerNumber, int x) {

        String mostLentGenre = mongoTemplate.aggregate(
                Aggregation.newAggregation(
                        Aggregation.match(Criteria.where("readerDetails.readerNumber").is(readerNumber)),
                        Aggregation.lookup("lending", "book", "book", "lendingInfo"),
                        Aggregation.unwind("lendingInfo"),
                        Aggregation.group("lendingInfo.genre").count().as("count"),
                        Aggregation.sort(Sort.by(Sort.Order.desc("count"))),
                        Aggregation.limit(1)
                ),
                "lending",
                String.class
        ).getMappedResults().stream().findFirst().orElse(null);

        if (mostLentGenre == null) {
            return Collections.emptyList();
        }

        return mongoTemplate.find(
                Query.query(Criteria.where("genre").is(mostLentGenre)).with(Sort.by(Sort.Order.desc("count"))).limit(x),
                Book.class
        );
    }



    @Override
    public List<Book> searchBooks(pt.psoft.g1.psoftg1.shared.services.Page page, SearchBooksQuery query) {
        Criteria criteria = new Criteria();
        if (query.getTitle() != null && !query.getTitle().isEmpty()) {
            criteria.and("title.title").regex(query.getTitle(), "i");
        }
        if (query.getGenre() != null && !query.getGenre().isEmpty()) {
            criteria.and("genre.genre").regex(query.getGenre(), "i");
        }
        if (query.getAuthorName() != null && !query.getAuthorName().isEmpty()) {
            criteria.and("authors.name").regex(query.getAuthorName(), "i");
        }

        Query mongoQuery = new Query(criteria);
        mongoQuery.with(Sort.by(Sort.Order.asc("title")));
        mongoQuery.skip((page.getNumber() - 1) * page.getLimit()).limit(page.getLimit());

        List<Book> bookList = mongoTemplate.find(mongoQuery, Book.class);

        return bookList;
    }
}
