package pt.psoft.g1.psoftg1.readermanagement.model;

import org.springframework.stereotype.Component;
import pt.psoft.g1.psoftg1.usermanagement.Reader;

import java.util.List;

@Component
public class ReaderDetailsFactory {

    public static ReaderDetails create(int readerNumber, Reader reader, String birthDate, String phoneNumber, boolean gdpr, boolean marketing, boolean thirdParty, String photoURI, List<String> interestList) {
        return new ReaderDetails(readerNumber, reader, birthDate, phoneNumber,gdpr,marketing,thirdParty,photoURI, interestList);
    }

}
