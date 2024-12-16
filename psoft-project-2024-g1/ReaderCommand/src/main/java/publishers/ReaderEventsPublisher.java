package publishers;

import pt.psoft.g1.psoftg1.readermanagement.api.LendingViewAMQP;
import pt.psoft.g1.psoftg1.readermanagement.model.ReaderDetails;

public interface ReaderEventsPublisher {
    void sendReaderCreated(ReaderDetails book);
    void sendReaderValidatedToLending(LendingViewAMQP lending);

}
