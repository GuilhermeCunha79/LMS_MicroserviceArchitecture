package pt.psoft.g1.psoftg1.lendingmanagement.model.recommendation;

import org.springframework.stereotype.Component;
import pt.psoft.g1.psoftg1.lendingmanagement.api.LendingView;
import pt.psoft.g1.psoftg1.lendingmanagement.model.Lending;
import pt.psoft.g1.psoftg1.lendingmanagement.services.CreateLendingRequest;
import pt.psoft.g1.psoftg1.lendingmanagement.services.LendingDTO;
import pt.psoft.g1.psoftg1.usermanagement.model.User;

@Component("alg2")
public class LendingRecommendation2Impl implements LendingRecommendation{
    @Override
    public Iterable<LendingView> bookRecommendation(final CreateLendingRequest resource) {
        return null;
    }
}
