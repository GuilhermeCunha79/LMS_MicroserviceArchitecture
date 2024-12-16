package pt.psoft.g1.psoftg1.recommendationmanagement.services;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pt.psoft.g1.psoftg1.recommendationmanagement.model.Recommendation;



/**
 * Brief guide:
 * <a href="https://www.baeldung.com/mapstruct">https://www.baeldung.com/mapstruct</a>
 * */
@Mapper(componentModel = "spring", uses = {RecommendationService.class})
public abstract class RecommendationMapper {

    @Mapping(target = "lendingNumber", source = "lendingNumber")
    @Mapping(target = "readerDetailsId", source = "readerDetailsId")
    @Mapping(target = "isbn", source = "isbn")
    @Mapping(target = "commentary", source = "commentary")
    public abstract Recommendation createRecommendation(CreateRecommendationRequest request);

}
