package pt.psoft.g1.psoftg1.configuration;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import pt.psoft.g1.psoftg1.readermanagement.api.ReaderEventRabbitmqReceiver;
import pt.psoft.g1.psoftg1.readermanagement.services.ReaderService;
import pt.psoft.g1.psoftg1.shared.model.LendingEvents;
import pt.psoft.g1.psoftg1.shared.model.ReaderEvents;

@Profile("!test")
@Configuration
public  class RabbitmqClientConfig {

    @Bean
    public DirectExchange direct1() {
        return new DirectExchange("LMSS.lending");
    }
    @Bean
    public DirectExchange direct() {
        return new DirectExchange("LMSSS.reader");
    }

    private static class ReceiverConfig {

        @Bean(name = "autoDeleteQueue_Reader_Created")
        public Queue autoDeleteQueue_Reader_Created() {

            System.out.println("autoDeleteQueue_Reader_Created created!");
            return new AnonymousQueue();
        }

        @Bean(name = "autoDeleteQueue_Lending_Validation_Reader")
        public Queue autoDeleteQueue_Lending_Created() {

            System.out.println("autoDeleteQueue_Lending_Validation_Reader created!");
            return new AnonymousQueue();
        }


        @Bean
        public Binding binding1(DirectExchange direct,
                                @Qualifier("autoDeleteQueue_Reader_Created") Queue autoDeleteQueue_Lending_Returned) {
            return BindingBuilder.bind(autoDeleteQueue_Lending_Returned)
                    .to(direct)
                    .with(ReaderEvents.READER_CREATED);
        }

        @Bean
        public Binding binding2(DirectExchange direct1,
                                @Qualifier("autoDeleteQueue_Lending_Validation_Reader") Queue autoDeleteQueue_Lending_Validation_Reader) {
            return BindingBuilder.bind(autoDeleteQueue_Lending_Validation_Reader)
                    .to(direct1)
                    .with(LendingEvents.LENDING_CREATED_READER);
        }

        @Bean
        public ReaderEventRabbitmqReceiver receiverReader(ReaderService readerService, @Qualifier("autoDeleteQueue_Reader_Created") Queue autoDeleteQueue_Reader_Created) {
            return new ReaderEventRabbitmqReceiver(readerService);
        }

        @Bean
        public ReaderEventRabbitmqReceiver receiverLending(ReaderService readerService, @Qualifier("autoDeleteQueue_Lending_Validation_Reader") Queue autoDeleteQueue_Lending_Validation_Reader) {
            return new ReaderEventRabbitmqReceiver(readerService);
        }
    }
}
