package pt.psoft.g1.psoftg1.lendingmanagement.publishers;

import pt.psoft.g1.psoftg1.lendingmanagement.model.Lending;

public interface LendingEventsPublisher {

    void sendLendingCreatedToBook(Lending lending);
    void sendLendingFailed(String lending);
    void sendLendingReturned(Lending lending);
    void sendLendingReturnedFinal(Lending lending);

    void sendLendingCreatedToReader(Lending lending);

  //  void sendLendingUpdated(Lending lending);
}
