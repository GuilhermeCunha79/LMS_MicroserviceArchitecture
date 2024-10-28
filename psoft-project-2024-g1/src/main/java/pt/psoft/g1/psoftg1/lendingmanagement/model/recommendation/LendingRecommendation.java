package pt.psoft.g1.psoftg1.lendingmanagement.model.recommendation;

import pt.psoft.g1.psoftg1.lendingmanagement.api.LendingView;
import pt.psoft.g1.psoftg1.lendingmanagement.model.Lending;
import pt.psoft.g1.psoftg1.lendingmanagement.services.CreateLendingRequest;
import pt.psoft.g1.psoftg1.lendingmanagement.services.LendingDTO;
import pt.psoft.g1.psoftg1.usermanagement.model.User;

public interface LendingRecommendation {
    Iterable<LendingView> bookRecommendation(final CreateLendingRequest resource);
}
