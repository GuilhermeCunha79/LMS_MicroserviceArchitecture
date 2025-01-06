package pt.psoft.g1.psoftg1.lendingmanagement.services;


import pt.psoft.g1.psoftg1.lendingmanagement.model.Lending;
import pt.psoft.g1.psoftg1.shared.api.LendingViewAMQP;

import java.util.Optional;

public interface LendingService {
    Lending create(CreateLendingRequest resource); //No ID passed, as it is auto generated
    Lending create(LendingViewAMQP resource);
    Lending createReader(LendingViewAMQP resource);
    Lending updateLendingRecommendation(LendingViewAMQP resource);
    Lending updateLendingRecommendationFailed(LendingViewAMQP resource);
    Lending setReturned(String id, SetLendingReturnedRequest resource);
    Lending setReturned(LendingViewAMQP resource);
    Optional<Lending> findByLendingNumber(String lendingNumber);

    Optional<Lending> update(LendingViewAMQP resource);
}
