package pt.psoft.g1.psoftg1.readermanagement.publishers;


import pt.psoft.g1.psoftg1.readermanagement.model.ReaderDetails;
import pt.psoft.g1.psoftg1.shared.api.LendingViewAMQP;

public interface ReaderEventsPublisher {
    void sendReaderCreated(ReaderDetails book);
    void sendReaderValidatedToLending(LendingViewAMQP lending);

}
