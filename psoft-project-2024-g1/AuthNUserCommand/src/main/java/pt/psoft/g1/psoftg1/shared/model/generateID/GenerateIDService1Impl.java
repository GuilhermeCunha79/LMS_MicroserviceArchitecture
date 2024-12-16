package pt.psoft.g1.psoftg1.shared.model.generateID;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
@Profile("IDService1")
public class GenerateIDService1Impl implements GenerateIDService {

    private static final SecureRandom secureRandom = new SecureRandom();

    @Override
    public String generateID() {
        long randomLong = secureRandom.nextLong();

        return String.valueOf(randomLong);
    }
}