package pt.psoft.g1.psoftg1.lendingmanagement.infrastructure.publishers.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pt.psoft.g1.psoftg1.lendingmanagement.api.LendingViewAMQP;
import pt.psoft.g1.psoftg1.lendingmanagement.api.LendingViewAMQPMapper;
import pt.psoft.g1.psoftg1.lendingmanagement.model.Lending;
import pt.psoft.g1.psoftg1.lendingmanagement.publishers.LendingEventsPublisher;
import pt.psoft.g1.psoftg1.shared.model.LendingEvents;

@Service
@RequiredArgsConstructor
public class LendingEventsRabbitmqPublisherImpl implements LendingEventsPublisher {

    @Autowired
    private RabbitTemplate template;
    @Autowired
    private DirectExchange direct;
    private final LendingViewAMQPMapper lendingViewAMQPMapper;

    private int count = 0;

    @Override
    public void sendLendingCreatedToBook(Lending lending) {
        sendLendingEvent(lending, LendingEvents.LENDING_CREATED_BOOK);
    }

    @Override
    public void sendLendingReturned(Lending lending, Long currentVersion) {
        sendLendingEvent(lending, LendingEvents.LENDING_RETURNED);
    }

    @Override
    public void sendLendingCreatedToReader(Lending lending) {
        sendLendingEvent(lending, LendingEvents.LENDING_CREATED_READER);
    }

    public void sendLendingEvent(Lending book, String bookEventType) {

        try {
            ObjectMapper objectMapper = new ObjectMapper();

            LendingViewAMQP lendingViewAMQP = lendingViewAMQPMapper.toLendingViewAMQP(book);

            String jsonString = objectMapper.writeValueAsString(lendingViewAMQP);

            this.template.convertAndSend(direct.getName(), bookEventType, jsonString);

            System.out.println(" [x] Sent '" + jsonString + "'");
        }
        catch( Exception ex ) {
            System.out.println(" [x] Exception sending Lending event: '" + ex.getMessage() + "'");
        }
    }
}