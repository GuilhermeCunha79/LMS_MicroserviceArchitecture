package pt.psoft.g1.psoftg1.configuration;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import pt.psoft.g1.psoftg1.recommendationmanagement.api.RecommendationEventRabbitmqReceiver;
import pt.psoft.g1.psoftg1.recommendationmanagement.services.RecommendationService;
import pt.psoft.g1.psoftg1.shared.model.LendingEvents;

@Profile("!test")
@Configuration
public  class RabbitmqClientConfig {

    @Bean
    public DirectExchange direct() {
        return new DirectExchange("LMS.lending");
    }

    private static class ReceiverConfig {

        @Bean(name = "autoDeleteQueue_Lending_Returned")
        public Queue autoDeleteQueue_Book_Created() {

            System.out.println("autoDeleteQueue_Lending_Returned created!");
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
        public RecommendationEventRabbitmqReceiver receiver(RecommendationService bookService, @Qualifier("autoDeleteQueue_Lending_Returned") Queue autoDeleteQueue_Lending_Returned) {
            return new RecommendationEventRabbitmqReceiver(bookService);
        }
    }
}
