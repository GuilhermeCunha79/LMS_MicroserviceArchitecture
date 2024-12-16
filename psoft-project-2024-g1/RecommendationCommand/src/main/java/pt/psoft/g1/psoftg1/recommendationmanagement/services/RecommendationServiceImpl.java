package pt.psoft.g1.psoftg1.recommendationmanagement.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pt.psoft.g1.psoftg1.exceptions.ConflictException;
import pt.psoft.g1.psoftg1.recommendationmanagement.api.RecommendationViewAMQP;
import pt.psoft.g1.psoftg1.recommendationmanagement.model.Recommendation;
import pt.psoft.g1.psoftg1.recommendationmanagement.repositories.RecommendationRepository;
import pt.psoft.g1.psoftg1.shared.model.Commentary;
import pt.psoft.g1.psoftg1.shared.model.CommentaryAnalysis;

@Service
@RequiredArgsConstructor
public class RecommendationServiceImpl implements RecommendationService {

    private final RecommendationRepository recommendationRepository;
    private final RecommendationMapper recommendationMapper;

    @Override
    public Recommendation create(CreateRecommendationRequest request) {

        return recommendationRepository.save(recommendationMapper.createRecommendation(request));

    }

    @Override
    public Recommendation create(RecommendationViewAMQP recommendationViewAMQP) {
        final String lendingNumber = recommendationViewAMQP.getLendingNumber();
        final String readerNumber = recommendationViewAMQP.getReaderDetailsId();
        final String isbn = recommendationViewAMQP.getIsbn();
        final Commentary commentary = CommentaryAnalysis.analyzeSentiment(recommendationViewAMQP.getCommentary());

        return create(lendingNumber, readerNumber, isbn, commentary);
    }

    private Recommendation create(String lendingNumber, String readerNumber, String isbn, Commentary commentary) {

        if (recommendationRepository.findByLendingNumber(lendingNumber).isPresent()) {
            throw new ConflictException("Recommendation for Lending " + lendingNumber + " already exists");
        }

        Recommendation recommendation = new Recommendation(lendingNumber, readerNumber, isbn, commentary);

        return recommendationRepository.save(recommendation);
    }

}
