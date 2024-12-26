package pt.psoft.g1.psoftg1.lendingmanagement.Controller;



import au.com.dius.pact.consumer.MockServer;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit.MockServerConfig;
import au.com.dius.pact.consumer.junit5.PactConsumerTest;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.consumer.junit5.ProviderType;
import au.com.dius.pact.core.model.PactSpecVersion;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import pt.psoft.g1.psoftg1.lendingmanagement.api.LendingEventRabbitmqReceiver;
import pt.psoft.g1.psoftg1.lendingmanagement.services.LendingService;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@PactConsumerTest
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.NONE
        ,classes = {LendingEventRabbitmqReceiver.class, LendingService.class}
)
@ExtendWith(PactConsumerTestExt.class)
@PactTestFor(providerName = "lending_event-producer", providerType = ProviderType.ASYNCH, pactVersion = PactSpecVersion.V4)
public class LendingBookPactTest {

    @MockBean
    LendingService bookService;

    @Autowired
    LendingEventRabbitmqReceiver listener;

    @BeforeEach
    public void setUp(MockServer mockServer) {
        assertThat(mockServer, is(notNullValue()));
    }

    @Pact(provider="ArticlesProvider", consumer="test_consumer")
    public RequestResponsePact createPact(PactDslWithProvider builder) {
        return builder
                .given("test state")
                .uponReceiving("ExampleJavaConsumerPactTest test interaction")
                .path("/articles.json")
                .method("GET")
                .willRespondWith()
                .status(200)
                .body("{\"responsetest\": true}")
                .toPact();
    }
}
