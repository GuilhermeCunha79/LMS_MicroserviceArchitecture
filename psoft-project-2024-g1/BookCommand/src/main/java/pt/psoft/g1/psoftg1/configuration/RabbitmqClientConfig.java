package pt.psoft.g1.psoftg1.configuration;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import pt.psoft.g1.psoftg1.authormanagement.api.AuthorEventRabbitmqReceiver;
import pt.psoft.g1.psoftg1.authormanagement.services.AuthorService;
import pt.psoft.g1.psoftg1.bookmanagement.api.BookEventRabbitmqReceiver;
import pt.psoft.g1.psoftg1.bookmanagement.services.BookService;
import pt.psoft.g1.psoftg1.shared.model.AuthorEvents;
import pt.psoft.g1.psoftg1.shared.model.BookEvents;
import pt.psoft.g1.psoftg1.shared.model.LendingEvents;

@Profile("!test")
@Configuration
public  class RabbitmqClientConfig {

    @Bean
    public DirectExchange direct() {
        return new DirectExchange("LMS.books");
    }

    @Bean
    public DirectExchange direct1() {
        return new DirectExchange("LMSS.lending");
    }

    private static class ReceiverConfig {

        @Bean(name = "autoDeleteQueue_Book_Created")
        public Queue autoDeleteQueue_Book_Created() {

            System.out.println("autoDeleteQueue_Book_Created created!");
            return new AnonymousQueue();
        }

        @Bean
        public Queue autoDeleteQueue_Book_Updated() {
            System.out.println("autoDeleteQueue_Book_Updated created!");
            return new AnonymousQueue();
        }

        @Bean
        public Queue autoDeleteQueue_Book_Deleted() {
            System.out.println("autoDeleteQueue_Book_Deleted created!");
            return new AnonymousQueue();
        }

        @Bean(name = "autoDeleteQueue_Author_Created")
        public Queue autoDeleteQueue_Author_Created() {

            System.out.println("autoDeleteQueue_Author_Created created!");
            return new AnonymousQueue();
        }

        @Bean(name = "autoDeleteQueue_Lending_Validation_Book")
        public Queue autoDeleteQueue_Lending_Validation_Book() {

            System.out.println("autoDeleteQueue_Lending_Validation_Book created!");
            return new AnonymousQueue();
        }

        @Bean
        public Binding binding1(DirectExchange direct,
                                @Qualifier("autoDeleteQueue_Book_Created") Queue autoDeleteQueue_Book_Created) {
            return BindingBuilder.bind(autoDeleteQueue_Book_Created)
                    .to(direct)
                    .with(BookEvents.BOOK_CREATED);
        }

        @Bean
        public Binding binding2(DirectExchange direct,
                                Queue autoDeleteQueue_Book_Updated) {
            return BindingBuilder.bind(autoDeleteQueue_Book_Updated)
                    .to(direct)
                    .with(BookEvents.BOOK_UPDATED);
        }

        @Bean
        public Binding binding3(DirectExchange direct,
                                Queue autoDeleteQueue_Book_Deleted) {
            return BindingBuilder.bind(autoDeleteQueue_Book_Deleted)
                    .to(direct)
                    .with(BookEvents.BOOK_DELETED);
        }

        @Bean
        public Binding binding4(DirectExchange direct,
                                @Qualifier("autoDeleteQueue_Author_Created") Queue autoDeleteQueue_Author_Created) {
            return BindingBuilder.bind(autoDeleteQueue_Author_Created)
                    .to(direct)
                    .with(AuthorEvents.AUTHOR_CREATED);
        }

        @Bean
        public Binding binding5(DirectExchange direct1,
                                @Qualifier("autoDeleteQueue_Lending_Validation_Book") Queue autoDeleteQueue_Lending_Validation_Book) {
            return BindingBuilder.bind(autoDeleteQueue_Lending_Validation_Book)
                    .to(direct1)
                    .with(LendingEvents.LENDING_CREATED_BOOK);
        }



        @Bean
        public BookEventRabbitmqReceiver receiverBooks(BookService bookService, @Qualifier("autoDeleteQueue_Book_Created") Queue autoDeleteQueue_Book_Created) {
            return new BookEventRabbitmqReceiver(bookService);
        }

        @Bean
        public AuthorEventRabbitmqReceiver receiverAuthor(AuthorService authorService, @Qualifier("autoDeleteQueue_Author_Created") Queue autoDeleteQueue_Author_Created) {
            return new AuthorEventRabbitmqReceiver(authorService);
        }

        @Bean
        public BookEventRabbitmqReceiver receiverLending(BookService authorService, @Qualifier("autoDeleteQueue_Lending_Validation_Book") Queue autoDeleteQueue_Lending_Validation_Book) {
            return new BookEventRabbitmqReceiver(authorService);
        }
    }
}
