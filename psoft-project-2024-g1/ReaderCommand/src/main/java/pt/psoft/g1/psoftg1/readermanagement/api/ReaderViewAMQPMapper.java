package pt.psoft.g1.psoftg1.readermanagement.api;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import pt.psoft.g1.psoftg1.readermanagement.model.ReaderDetails;
import pt.psoft.g1.psoftg1.shared.api.MapperInterface;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class ReaderViewAMQPMapper extends MapperInterface {

    @Named(value = "toReaderViewAMQP")
    @Mapping(target = "fullName", source = "reader.name.name")
    @Mapping(target = "email", source = "reader.username")
    @Mapping(target = "birthDate", source = "birthDate.birthDate")
    @Mapping(target = "phoneNumber", source = "phoneNumber")
    @Mapping(target = "gdprConsent", source = "gdprConsent")
    @Mapping(target = "readerNumber", source = "readerNumber")
    @Mapping(target = "interestList", source = "interestList")
    public abstract ReaderDetailsViewAMQP toReaderViewAMQP(ReaderDetails lending);

    @Mapping(target = ".", qualifiedByName = "toReaderViewAMQP")
    public abstract List<ReaderView> toReaderViewAMQP(Iterable<ReaderDetails> readerList);

}
