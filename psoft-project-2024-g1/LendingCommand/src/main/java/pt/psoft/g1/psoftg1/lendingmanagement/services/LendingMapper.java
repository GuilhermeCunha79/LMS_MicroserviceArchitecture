package pt.psoft.g1.psoftg1.lendingmanagement.services;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import pt.psoft.g1.psoftg1.lendingmanagement.model.Lending;
import pt.psoft.g1.psoftg1.lendingmanagement.model.LendingMongo;

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

    public abstract Lending toLending(LendingMongo lendingMongo);

    @Mapping(target = "version", ignore = true)
    public abstract LendingMongo toLendingMongo(Lending lendingMongo);

    public abstract Lending toEntity(LendingDTO lendingDTO);
}