package pt.psoft.g1.psoftg1.configuration;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import pt.psoft.g1.psoftg1.lendingmanagement.api.LendingEventRabbitmqReceiver;
import pt.psoft.g1.psoftg1.lendingmanagement.services.LendingService;
import pt.psoft.g1.psoftg1.shared.model.BookEvents;
import pt.psoft.g1.psoftg1.shared.model.LendingEvents;
import pt.psoft.g1.psoftg1.shared.model.ReaderEvents;
import pt.psoft.g1.psoftg1.shared.model.RecommendationEvents;

@Profile("!test")
@Configuration
public  class RabbitmqClientConfig {

    @Bean
    public DirectExchange direct() {
        return new DirectExchange("LMSS.lending");
    }

    private static class ReceiverConfig {

        @Bean(name = "autoDeleteQueue_Lending_Returned")
        public Queue autoDeleteQueue_Lending_Returned() {
            System.out.println("autoDeleteQueue_Lending_Returned created!");
            return new AnonymousQueue();
        }

        @Bean(name = "autoDeleteQueue_Lending_Validation_Book")
        public Queue autoDeleteQueue_Lending_Validation_Book() {
            System.out.println("autoDeleteQueue_Lending_Validation_Book created!");
            return new AnonymousQueue();
        }

        @Bean(name = "autoDeleteQueue_Lending_Validation_Reader")
        public Queue autoDeleteQueue_Lending_Validation_Reader() {
            System.out.println("autoDeleteQueue_Lending_Validation_Reader created!");
            return new AnonymousQueue();
        }

        @Bean(name = "autoDeleteQueue_Recommendation_Created")
        public Queue autoDeleteQueue_Recommendation_Created() {
            System.out.println("autoDeleteQueue_Recommendation_Created created!");
            return new AnonymousQueue();
        }

        @Bean(name = "autoDeleteQueue_Recommendation_Created_Failed")
        public Queue autoDeleteQueue_Recommendation_Created_Failed() {
            System.out.println("autoDeleteQueue_Recommendation_Created_Failed created!");
            return new AnonymousQueue();
        }

        @Bean
        public Binding binding2(DirectExchange direct,
                                Queue autoDeleteQueue_Lending_Returned) {
            return BindingBuilder.bind(autoDeleteQueue_Lending_Returned)
                    .to(direct)
                    .with(LendingEvents.LENDING_RETURNED);
        }

        @Bean
        public Binding binding3(DirectExchange direct,
                                @Qualifier("autoDeleteQueue_Lending_Validation_Book") Queue autoDeleteQueue_Lending_Validation_Book) {
            return BindingBuilder.bind(autoDeleteQueue_Lending_Validation_Book)
                    .to(direct)
                    .with(BookEvents.BOOK_VALIDATED);
        }

        @Bean
        public Binding binding4(DirectExchange direct,
                                @Qualifier("autoDeleteQueue_Lending_Validation_Reader") Queue autoDeleteQueue_Lending_Validation_Reader) {
            return BindingBuilder.bind(autoDeleteQueue_Lending_Validation_Reader)
                    .to(direct)
                    .with(ReaderEvents.READER_VALIDATED);
        }

        @Bean
        public Binding binding5(DirectExchange direct,
                                @Qualifier("autoDeleteQueue_Recommendation_Created") Queue autoDeleteQueue_Recommendation_Created_Book) {
            return BindingBuilder.bind(autoDeleteQueue_Recommendation_Created_Book)
                    .to(direct)
                    .with(RecommendationEvents.RECOMMENDATION_CREATED);
        }

        @Bean
        public Binding binding6(DirectExchange direct,
                                @Qualifier("autoDeleteQueue_Recommendation_Created_Failed") Queue autoDeleteQueue_Recommendation_Created_Failed) {
            return BindingBuilder.bind(autoDeleteQueue_Recommendation_Created_Failed)
                    .to(direct)
                    .with(RecommendationEvents.RECOMMENDATION_CREATED_FAILED);
        }

        @Bean
        public LendingEventRabbitmqReceiver receiver1(LendingService lendingService, @Qualifier("autoDeleteQueue_Lending_Validation_Book") Queue autoDeleteQueue_Lending_Validation_Book) {
            return new LendingEventRabbitmqReceiver(lendingService);
        }

        @Bean
        public LendingEventRabbitmqReceiver receiver2(LendingService lendingService, @Qualifier("autoDeleteQueue_Lending_Validation_Reader") Queue autoDeleteQueue_Lending_Validation_Reader) {
            return new LendingEventRabbitmqReceiver(lendingService);
        }
    }
}
