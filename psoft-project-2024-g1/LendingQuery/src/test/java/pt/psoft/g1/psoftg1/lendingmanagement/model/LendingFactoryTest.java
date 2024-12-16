package pt.psoft.g1.psoftg1.lendingmanagement.model;
  /*
import org.junit.Test;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.authormanagement.model.AuthorFactory;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.bookmanagement.model.BookFactory;
import pt.psoft.g1.psoftg1.bookmanagement.model.BookFactoryTest;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;
import pt.psoft.g1.psoftg1.genremanagement.model.GenreFactory;
import pt.psoft.g1.psoftg1.readermanagement.model.ReaderDetails;
import pt.psoft.g1.psoftg1.usermanagement.model.Reader;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class LendingFactoryTest {

    @Test
    public void testCreateLending() {
        Genre genre = GenreFactory.create("Infantil");
        List<Author> authors = new ArrayList<>();
        authors.add(AuthorFactory.create("Luis","Luis","Luis"));
        Reader reader=new Reader("Guilhas","pasasA1AS2!");
        Book book = BookFactory.create("9782826012092", "O Inspetor Max", "Descrição do livro", genre, authors, "Luis");
        ReaderDetails readerDetails = new ReaderDetails(1, reader, "2000-01-01", "919191919", true, true, true, null, null);
        int seq = 1;
        String seq1="2024/1";
        int lendingDuration = 15;
        LocalDate date = LocalDate.now();
        int fineValuePerDayInCents = 100;

        Lending lending = LendingFactory.create(book, readerDetails, seq, lendingDuration, fineValuePerDayInCents);

        assertThat(lending).isNotNull();
        assertThat(lending.getBook()).isEqualTo(book);
        assertThat(lending.getReaderDetails()).isEqualTo(readerDetails);
        assertThat(lending.getLendingNumber()).isEqualTo(seq1);
        assertThat(lending.getLimitDate()).isEqualTo(date.plusDays(lendingDuration));
        assertThat(lending.getFineValuePerDayInCents()).isEqualTo(fineValuePerDayInCents);
    }

    @Test
    public void testCreateLendingMongo() {
        Genre genre = GenreFactory.create("Infantil");
        List<Author> authors = new ArrayList<>();
        authors.add(AuthorFactory.create("Luis", "Luis", "Luis"));
        Reader reader = new Reader("Guilhas", "pasasA1AS2!");
        Book book = BookFactory.create("9782826012092", "O Inspetor Max", "Descrição do livro", genre, authors, "Luis");
        ReaderDetails readerDetails = new ReaderDetails(1, reader, "2000-01-01", "919191919", true, true, true, null, null);
        int seq = 1;
        String seq1 = "2024/1";
        int lendingDuration = 15;
        LocalDate date = LocalDate.now();
        int fineValuePerDayInCents = 100;

        LendingMongo lendingMongo = LendingFactory.createMongo(book, readerDetails, seq, lendingDuration, fineValuePerDayInCents);

        assertThat(lendingMongo).isNotNull();
        assertThat(lendingMongo.getBook()).isEqualTo(book);
        assertThat(lendingMongo.getReaderDetails()).isEqualTo(readerDetails);
        assertThat(lendingMongo.getLendingNumber()).isEqualTo(seq1);
        assertThat(lendingMongo.getLimitDate()).isEqualTo(date.plusDays(lendingDuration));
        assertThat(lendingMongo.getFineValuePerDayInCents()).isEqualTo(fineValuePerDayInCents);
    }
}*/
