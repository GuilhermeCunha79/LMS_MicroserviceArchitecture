package pt.psoft.g1.psoftg1.lendingmanagement.api;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import pt.psoft.g1.psoftg1.lendingmanagement.model.Lending;
import pt.psoft.g1.psoftg1.shared.api.MapperInterface;

import java.util.List;

/**
 * Brief guides:
 * <a href="https://www.baeldung.com/mapstruct">https://www.baeldung.com/mapstruct</a>
 * <p>
 * <a href="https://medium.com/@susantamon/mapstruct-a-comprehensive-guide-in-spring-boot-context-1e7202da033e">https://medium.com/@susantamon/mapstruct-a-comprehensive-guide-in-spring-boot-context-1e7202da033e</a>
 * */
@Mapper(componentModel = "spring")
public abstract class LendingViewMapper extends MapperInterface {

    @Mapping(target = "lendingNumber", source = "lendingNumber")
    @Mapping(target = "fineValueInCents", expression = "java(lending.getFineValueInCents().orElse(null))")
    @Mapping(target = "returnedDate", source = "returnedDate")
    @Mapping(target = "isbn", source = "isbn")
    @Mapping(target = "status", source = "status", qualifiedByName = "mapToInt")
    public abstract LendingView toLendingView(Lending lending);

    public abstract List<LendingView> toLendingView(List<Lending> lendings);

    public abstract LendingsAverageDurationView toLendingsAverageDurationView(Double lendingsAverageDuration);

    @Named("mapToInt")
    int mapToInt(Number value) {
        if (value == null) {
            throw new IllegalArgumentException("Status cannot be null");
        }
        return value.intValue();
    }
}
