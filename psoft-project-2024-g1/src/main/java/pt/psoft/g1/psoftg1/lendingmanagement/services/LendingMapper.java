package pt.psoft.g1.psoftg1.lendingmanagement.services;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.bookmanagement.model.BookMongo;
import pt.psoft.g1.psoftg1.bookmanagement.services.BookService;
import pt.psoft.g1.psoftg1.lendingmanagement.model.Lending;
import pt.psoft.g1.psoftg1.lendingmanagement.model.LendingMongo;
import pt.psoft.g1.psoftg1.readermanagement.services.ReaderService;

import java.util.Optional;

/**
 * Brief guide:
 * <a href="https://www.baeldung.com/mapstruct">https://www.baeldung.com/mapstruct</a>
 * */
@Mapper(componentModel = "spring", uses = {BookService.class, ReaderService.class})
public abstract class LendingMapper {

    @Mapping(target = "commentary", source = "request.commentary")
    public abstract void update(SetLendingReturnedRequest request, @MappingTarget Lending lending);

    @Mapping(target = "daysOverdue", expression = "java(mapDaysOverdue(lending.getDaysOverdue()))")
    public abstract LendingDTO toDTO(Lending lending, Book book);

    protected Integer mapDaysOverdue(Optional<Integer> daysOverdue) {
        return daysOverdue.orElse(null); // ou forneça um valor padrão, como daysOverdue.orElse(0)
    }

    public abstract Lending toLending(LendingMongo lendingMongo);

    @Mapping(target = "version", ignore = true)
    public abstract LendingMongo toLendingMongo(Lending lendingMongo);

    //public abstract Lending toEntity(LendingDTO lendingDTO);
}