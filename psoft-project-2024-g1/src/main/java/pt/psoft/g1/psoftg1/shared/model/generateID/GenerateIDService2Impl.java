package pt.psoft.g1.psoftg1.shared.model.generateID;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component("IDService2")
@Primary
public class GenerateIDService2Impl implements GenerateIDService {

    private static final SecureRandom secureRandom = new SecureRandom();
    private static final String ALPHANUMERIC_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int ID_LENGTH = 20; // 20 characters long

    @Override
    public String generateID() {
        StringBuilder result = new StringBuilder(ID_LENGTH);

        for (int i = 0; i < ID_LENGTH; i++) {
            // Choose a random character from the ALPHANUMERIC_CHARACTERS string
            int index = secureRandom.nextInt(ALPHANUMERIC_CHARACTERS.length());
            result.append(ALPHANUMERIC_CHARACTERS.charAt(index));
        }

        return String.valueOf(result);
    }
}
