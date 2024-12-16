package pt.psoft.g1.psoftg1.readermanagement.infraestructure.publishers.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pt.psoft.g1.psoftg1.readermanagement.api.LendingViewAMQP;
import pt.psoft.g1.psoftg1.readermanagement.api.ReaderDetailsViewAMQP;
import pt.psoft.g1.psoftg1.readermanagement.api.ReaderViewAMQPMapper;
import pt.psoft.g1.psoftg1.readermanagement.model.ReaderDetails;
import pt.psoft.g1.psoftg1.shared.model.ReaderEvents;
import publishers.ReaderEventsPublisher;

@Service
@RequiredArgsConstructor
public class ReaderEventsRabbitmqPublisherImpl implements ReaderEventsPublisher {

    @Autowired
    private RabbitTemplate template;
    @Autowired
    private DirectExchange direct;
    @Autowired
    private DirectExchange direct1;  // Exchange de empréstimos (LMS.lending)
    private final ReaderViewAMQPMapper readerViewAMQPMapper;

    private int count = 0;

    @Override
    public void sendReaderCreated(ReaderDetails readerDetails) {
        sendReaderEvent(readerDetails, ReaderEvents.READER_CREATED);
    }

    @Override
    public void sendReaderValidatedToLending(LendingViewAMQP lendingViewAMQP) {
        sendLendingEvent(lendingViewAMQP, ReaderEvents.READER_VALIDATED);
    }

    public void sendReaderEvent(ReaderDetails readerDetails, String readerEventType) {

        try {
            ObjectMapper objectMapper = new ObjectMapper();

            ReaderDetailsViewAMQP readerViewAMQP = readerViewAMQPMapper.toReaderViewAMQP(readerDetails);

            String jsonString = objectMapper.writeValueAsString(readerViewAMQP);

            this.template.convertAndSend(direct.getName(), readerEventType, jsonString);

            System.out.println(" [x] Sent '" + jsonString + "'");
        }
        catch( Exception ex ) {
            System.out.println(" [x] Exception sending Reader event: '" + ex.getMessage() + "'");
        }
    }

    public void sendLendingEvent(LendingViewAMQP lendingViewAMQP, String bookEventType) {

        try {
            ObjectMapper objectMapper = new ObjectMapper();

            String jsonString = objectMapper.writeValueAsString(lendingViewAMQP);

            this.template.convertAndSend(direct1.getName(), bookEventType, jsonString);

            System.out.println(" [x] Sent Lending Validated '" + jsonString + "'");
        }
        catch( Exception ex ) {
            System.out.println(" [x] Exception sending Lending Validated event: '" + ex.getMessage() + "'");
        }
    }
}