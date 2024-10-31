package pt.psoft.g1.psoftg1.readermanagement.model;

import org.springframework.stereotype.Component;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;
import pt.psoft.g1.psoftg1.usermanagement.model.Reader;

import java.util.List;

@Component
public class ReaderDetailsFactory {

    public static ReaderDetails create(int readerNumber, Reader reader, String birthDate, String phoneNumber, boolean gdpr, boolean marketing, boolean thirdParty, String photoURI, List<Genre> interestList) {
        return new ReaderDetails(readerNumber, reader, birthDate, phoneNumber,gdpr,marketing,thirdParty,photoURI, interestList);
    }

}
