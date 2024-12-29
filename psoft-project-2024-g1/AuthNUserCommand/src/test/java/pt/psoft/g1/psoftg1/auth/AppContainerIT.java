package pt.psoft.g1.psoftg1.auth;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AppContainerIT {

    private final int port = 8071;
    private GenericContainer<?> appContainer;

    private static final DockerImageName IMAGE_NAME = DockerImageName.parse("lmsusers:latest");

    @BeforeEach
    public void setUp() {
        appContainer = new GenericContainer<>(IMAGE_NAME)
                .withExposedPorts(port);

        appContainer.start();
    }

    @Test
    public void testApplicationIsRunning() throws IOException {
        String containerUrl = "http://" + appContainer.getHost() + ":" + appContainer.getMappedPort(port);

        HttpURLConnection connection = (HttpURLConnection) new URL(containerUrl + "/health").openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        assertEquals(200, responseCode, "Responding");
    }

    @AfterEach
    public void tearDown() {
        if (appContainer != null && appContainer.isRunning()) {
            appContainer.stop();
        }
    }
}
