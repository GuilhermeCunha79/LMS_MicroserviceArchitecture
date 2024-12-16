package pt.psoft.g1.psoftg1.lendingmanagement.model;

import org.springframework.stereotype.Component;

@Component
public class LendingFactory {

    public static Lending create(String isbn, String readerDetailsId, int seq, int lendingDuration, int fineValuePerDayInCents){
        return new Lending(isbn, readerDetailsId,seq, lendingDuration,fineValuePerDayInCents);
    }

    public static Lending create(String lendingNumber, String isbn, String readerDetailsId, int lendingDuration, int fineValuePerDayInCents,int status){
        return new Lending(lendingNumber, isbn, readerDetailsId, lendingDuration,fineValuePerDayInCents ,status);
    }

}
