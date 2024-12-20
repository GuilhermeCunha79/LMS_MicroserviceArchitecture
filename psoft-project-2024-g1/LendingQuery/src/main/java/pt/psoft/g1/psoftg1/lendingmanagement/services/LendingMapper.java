package pt.psoft.g1.psoftg1.lendingmanagement.services;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import pt.psoft.g1.psoftg1.lendingmanagement.model.Lending;
import pt.psoft.g1.psoftg1.lendingmanagement.model.LendingMongo;
import pt.psoft.g1.psoftg1.lendingmanagement.model.LendingNumber;

import java.util.Optional;

/**
 * Brief guide:
 * <a href="https://www.baeldung.com/mapstruct">https://www.baeldung.com/mapstruct</a>
 * */
@Mapper(componentModel = "spring")
public abstract class LendingMapper {

    @Mapping(target = "commentary", source = "request.commentary")
    public abstract void update(SetLendingReturnedRequest request, @MappingTarget Lending lending);

    @Mapping(target = "daysOverdue", expression = "java(mapDaysOverdue(lending.getDaysOverdue()))")
    public abstract LendingDTO toDTO(Lending lending, Long bookId);

    protected Integer mapDaysOverdue(Optional<Integer> daysOverdue) {
        return daysOverdue.orElse(null); // ou forneça um valor padrão, como daysOverdue.orElse(0)
    }

    @Mapping(target = "lendingNumber", source = "lendingNumber", qualifiedByName = "mapToLendingNumber")
    @Mapping(target = "readerDetailsId", source = "readerDetailsId")
    @Mapping(target = "isbn", source = "isbn")
    @Mapping(target = "status", source = "status", qualifiedByName = "mapToInt")
    @Mapping(target = "returnedDate", source = "returnedDate")
    @Mapping(target = "commentary", source = "commentary")
    public abstract Lending toLending(LendingMongo lendingMongo);

    @Mapping(target = "lendingNumber", source = "lendingNumber", qualifiedByName = "mapToLendingNumber")
    @Mapping(target = "readerDetailsId", source = "readerDetailsId")
    @Mapping(target = "isbn", source = "isbn")
    @Mapping(target = "status", source = "status", qualifiedByName = "mapToInt")
    @Mapping(target = "returnedDate", source = "returnedDate")
    @Mapping(target = "commentary", source = "commentary")
    public abstract LendingMongo toLendingMongo(Lending lendingMongo);


    @Named("mapToInt")
    int mapToInt(Number value) {
        if (value == null) {
            throw new IllegalArgumentException("Status cannot be null");
        }
        return value.intValue();
    }

    @Named("mapToLendingNumber")
     LendingNumber mapStringToLendingNumber(String value) {
        return value != null ? new LendingNumber(value) : null;
    }
}