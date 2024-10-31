package pt.psoft.g1.psoftg1.lendingmanagement.model;

import org.springframework.stereotype.Component;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.readermanagement.model.ReaderDetails;

@Component
public class LendingFactory {

    public static Lending create(Book book, ReaderDetails readerDetails, int seq, int lendingDuration, int fineValuePerDayInCents){
        return new Lending(book, readerDetails,seq, lendingDuration,fineValuePerDayInCents);
    }

    public static LendingMongo createMongo(Book book, ReaderDetails readerDetails, int seq, int lendingDuration, int fineValuePerDayInCents){
        return new LendingMongo(book, readerDetails,seq, lendingDuration,fineValuePerDayInCents);
    }
}
