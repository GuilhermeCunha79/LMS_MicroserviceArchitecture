package pt.psoft.g1.psoftg1.recommendationmanagement.api;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pt.psoft.g1.psoftg1.recommendationmanagement.model.Recommendation;
import pt.psoft.g1.psoftg1.shared.api.MapperInterface;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public abstract class RecommendationViewAMQPMapper extends MapperInterface {

    @Mapping(target = "recommendationNumber", source = "recommendationNumber")
    @Mapping(target = "lendingNumber", source = "lendingNumber")
    @Mapping(target = "readerDetailsId", source = "readerDetailsId")
    @Mapping(target = "isbn", source = "isbn")
    @Mapping(target = "commentary", source = "commentary")

    public abstract RecommendationViewAMQP toRecommendationViewAMQP(Recommendation book);

}
