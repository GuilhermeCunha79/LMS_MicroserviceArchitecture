package pt.psoft.g1.psoftg1.lendingmanagement.api;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import pt.psoft.g1.psoftg1.lendingmanagement.model.Lending;
import pt.psoft.g1.psoftg1.shared.api.LendingViewAMQP;
import pt.psoft.g1.psoftg1.shared.api.MapperInterface;

import java.util.Optional;

@Mapper(componentModel = "spring")
public abstract class LendingViewAMQPMapper extends MapperInterface {

    @Mapping(target = "lendingNumber", source = "lendingNumber")
    @Mapping(target = "readerDetailsId", source = "readerDetailsId")
    @Mapping(target = "isbn", source = "isbn")
    @Mapping(target = "status", source = "status", qualifiedByName = "mapToInt")
    @Mapping(target = "returnedDate", source = "returnedDate")
    @Mapping(target = "commentary", source = "commentary")
    public abstract LendingViewAMQP toLendingViewAMQP(Lending lending);

    @Named("mapToInt")
    int mapToInt(Optional<Integer> value) {
        if (value.isEmpty()) {
            throw new IllegalArgumentException("Status cannot be null or empty");
        }
        return value.get();
    }


}
