package pt.psoft.g1.psoftg1.recommendationmanagement.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pt.psoft.g1.psoftg1.exceptions.ConflictException;
import pt.psoft.g1.psoftg1.publishers.RecommendationEventsPublisher;
import pt.psoft.g1.psoftg1.recommendationmanagement.api.RecommendationViewAMQP;
import pt.psoft.g1.psoftg1.recommendationmanagement.model.Recommendation;
import pt.psoft.g1.psoftg1.recommendationmanagement.repositories.RecommendationRepository;
import pt.psoft.g1.psoftg1.shared.api.LendingViewAMQP;
import pt.psoft.g1.psoftg1.shared.model.Commentary;
import pt.psoft.g1.psoftg1.shared.model.CommentaryAnalysis;
import pt.psoft.g1.psoftg1.shared.model.RecommendationStatus;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RecommendationServiceImpl implements RecommendationService {

    private final RecommendationRepository recommendationRepository;
    private final RecommendationMapper recommendationMapper;
    private final RecommendationEventsPublisher recommendationEventsPublisher;

    @Override
    public Recommendation create(CreateRecommendationRequest request) {

        return recommendationRepository.save(recommendationMapper.createRecommendation(request));

    }

    @Override
    public Recommendation create(LendingViewAMQP lendingViewAMQP) {
        final String lendingNumber = lendingViewAMQP.getLendingNumber();
        final String readerNumber = lendingViewAMQP.getReaderDetailsId();
        final String isbn = lendingViewAMQP.getIsbn();

        Commentary commentary;
        try {
            commentary = CommentaryAnalysis.analyzeSentiment(lendingViewAMQP.getCommentary());
        } catch (IllegalArgumentException e) {
            recommendationEventsPublisher.sendRecommendationCreatedFailedToLending(new Recommendation(lendingNumber, readerNumber, isbn, Commentary.NEGATIVE));
            throw new ConflictException("Invalid commentary: " + e.getMessage());
        }

        return create(lendingNumber, readerNumber, isbn, commentary);
    }


    private Recommendation create(String lendingNumber, String readerNumber, String isbn, Commentary commentary) {

        if (recommendationRepository.findByLendingNumber(lendingNumber).isPresent()) {
            recommendationEventsPublisher.sendRecommendationCreatedFailedToLending(new Recommendation(lendingNumber, readerNumber, isbn, Commentary.NEGATIVE));
            throw new ConflictException("Recommendation for Lending " + lendingNumber + " already exists");
        }

        Recommendation recommendation = new Recommendation(lendingNumber, readerNumber, isbn, commentary);
        recommendationEventsPublisher.sendRecommendationCreatedToLending(recommendation);

        return recommendationRepository.save(recommendation);
    }

    @Override
    public Recommendation update(LendingViewAMQP lendingViewAMQP) {
        Optional<Recommendation> optionalRecommendation = recommendationRepository.findByLendingNumber(lendingViewAMQP.getLendingNumber());

        optionalRecommendation.ifPresent(recommendation -> {
            recommendation.setStatus(RecommendationStatus.RECOMMENDATION_VALID);
            recommendationRepository.save(recommendation);
        });

        return optionalRecommendation.orElseThrow(() -> new NoSuchElementException("Recommendation not found"));
    }


    @Override
    public void delete(String recommendationNumber) {
        Recommendation recommendation = recommendationRepository.findByLendingNumber(recommendationNumber)
                .orElseThrow(() -> new NoSuchElementException("Recommendation not found for recommendation Number: " + recommendationNumber));

        recommendationRepository.delete(recommendation);
    }



    private List<Recommendation> findAll() {

        return recommendationRepository.findAll();
    }

}
