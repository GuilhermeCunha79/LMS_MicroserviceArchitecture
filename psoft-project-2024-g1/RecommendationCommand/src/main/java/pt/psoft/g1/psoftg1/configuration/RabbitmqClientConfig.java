package pt.psoft.g1.psoftg1.configuration;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import pt.psoft.g1.psoftg1.recommendationmanagement.api.RecommendationEventRabbitmqReceiver;
import pt.psoft.g1.psoftg1.recommendationmanagement.services.RecommendationService;
import pt.psoft.g1.psoftg1.shared.model.LendingEvents;

@Profile("!test")
@Configuration
public class RabbitmqClientConfig {

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

        @Bean(name = "autoDeleteQueue_Lending_Returned_Failed")
        public Queue autoDeleteQueue_Lending_Returned_Failed() {

            System.out.println("autoDeleteQueue_Lending_Returned_Failed created!");
            return new AnonymousQueue();
        }

        @Bean(name = "autoDeleteQueue_Lending_Returned_Final")
        public Queue autoDeleteQueue_Lending_Returned_Final() {

            System.out.println("autoDeleteQueue_Lending_Returned_Final created!");
            return new AnonymousQueue();
        }

        @Bean
        public Binding binding1(DirectExchange direct,
                                @Qualifier("autoDeleteQueue_Lending_Returned") Queue autoDeleteQueue_Book_Created) {
            return BindingBuilder.bind(autoDeleteQueue_Book_Created)
                    .to(direct)
                    .with(LendingEvents.LENDING_RETURNED);
        }

        @Bean
        public Binding binding2(DirectExchange direct,
                                @Qualifier("autoDeleteQueue_Lending_Returned_Final") Queue autoDeleteQueue_Lending_Returned_Final) {
            return BindingBuilder.bind(autoDeleteQueue_Lending_Returned_Final)
                    .to(direct)
                    .with(LendingEvents.LENDING_RETURNED_FINAL);
        }

        @Bean
        public Binding binding3(DirectExchange direct,
                                @Qualifier("autoDeleteQueue_Lending_Returned_Failed") Queue autoDeleteQueue_Lending_Returned_Failed) {
            return BindingBuilder.bind(autoDeleteQueue_Lending_Returned_Failed)
                    .to(direct)
                    .with(LendingEvents.LENDING_FAILED);
        }

        @Bean
        public RecommendationEventRabbitmqReceiver receiver(RecommendationService bookService,
                                                            @Qualifier("autoDeleteQueue_Lending_Returned") Queue autoDeleteQueue_Lending_Returned) {
            return new RecommendationEventRabbitmqReceiver(bookService);
        }

        @Bean
        public RecommendationEventRabbitmqReceiver receiver1(RecommendationService bookService,
                                                             @Qualifier("autoDeleteQueue_Lending_Returned_Final") Queue autoDeleteQueue_Lending_Returned_Final) {
            return new RecommendationEventRabbitmqReceiver(bookService);
        }

        @Bean
        public RecommendationEventRabbitmqReceiver receiver2(RecommendationService bookService,
                                                             @Qualifier("autoDeleteQueue_Lending_Returned_Failed") Queue autoDeleteQueue_Lending_Returned_Failed) {
            return new RecommendationEventRabbitmqReceiver(bookService);
        }
    }
}
