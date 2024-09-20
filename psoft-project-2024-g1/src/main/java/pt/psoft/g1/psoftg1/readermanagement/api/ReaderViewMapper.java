package pt.psoft.g1.psoftg1.readermanagement.api;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;
import pt.psoft.g1.psoftg1.readermanagement.model.ReaderDetails;
import pt.psoft.g1.psoftg1.readermanagement.services.ReaderBookCountDTO;
import pt.psoft.g1.psoftg1.shared.api.MapperInterface;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public abstract class ReaderViewMapper extends MapperInterface {

    @Named(value = "toReaderView")
    @Mapping(target = "fullName", source = "reader.name.name")
    @Mapping(target = "email", source = "reader.username")
    @Mapping(target = "birthDate", source = "birthDate.birthDate")
    @Mapping(target = "phoneNumber", source = "phoneNumber")
    @Mapping(target = "gdprConsent", source = "gdprConsent")
    @Mapping(target = "readerNumber", source = "readerNumber")
    @Mapping(target = "photo", expression = "java(generatePhotoUrl(readerDetails))")
    @Mapping(target = "interestList", expression = "java(mapInterestList(readerDetails.getInterestList()))")
    public abstract ReaderView toReaderView(ReaderDetails readerDetails);

    @Named(value = "toReaderQuoteView")
    @Mapping(target = "fullName", source = "reader.name.name")
    @Mapping(target = "email", source = "reader.username")
    @Mapping(target = "birthDate", source = "birthDate.birthDate")
    @Mapping(target = "phoneNumber", source = "phoneNumber")
    @Mapping(target = "gdprConsent", source = "gdprConsent")
    @Mapping(target = "readerNumber", source = "readerNumber")
    @Mapping(target = "photo", expression = "java(generatePhotoUrl(readerDetails))")
    @Mapping(target = "interestList", expression = "java(mapInterestList(readerDetails.getInterestList()))")
    public abstract ReaderQuoteView toReaderQuoteView(ReaderDetails readerDetails);

    @Mapping(target = ".", qualifiedByName = "toReaderView")
    public abstract List<ReaderView> toReaderView(Iterable<ReaderDetails> readerList);

   @Mapping(target = "readerView", source = "readerDetails")
    public abstract ReaderCountView toReaderCountView(ReaderBookCountDTO readerBookCountDTO);

    public abstract List<ReaderCountView> toReaderCountViewList(List<ReaderBookCountDTO> readerBookCountDTOList);


    protected String generatePhotoUrl(ReaderDetails readerDetails) {
        String readerNumber = readerDetails.getReaderNumber();
        String[] readerNumberSplit = readerNumber.split("/");
        int year = Integer.parseInt(readerNumberSplit[0]);
        int seq = Integer.parseInt(readerNumberSplit[1]);
        return ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/readers/{year}/{seq}/photo").buildAndExpand(year,seq).toUri().toString();
    }

    protected List<String> mapInterestList(List<Genre> interestList) {
        List<String> stringInterestList = new ArrayList<>();

        if(interestList == null || interestList.isEmpty()) {
            return stringInterestList;
        }

        for(Genre genre : interestList) {
            stringInterestList.add(genre.getGenre());
        }

        return stringInterestList;
    }
}
