package pt.psoft.g1.psoftg1.shared.model.generateID;

import org.springframework.stereotype.Component;
import java.security.SecureRandom;

@Component("IDService1")
public class GenerateIDService1Impl implements GenerateIDService {

    private static final SecureRandom secureRandom = new SecureRandom();

    @Override
    public String generateID() {
        long randomLong = secureRandom.nextLong();

        return String.valueOf(randomLong);
    }
}