package pt.psoft.g1.psoftg1.lendingmanagement.model.generateID;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component("LendingIDService2")
@Primary
public class LendingIDService2Impl implements LendingIDService {

    private static final SecureRandom secureRandom = new SecureRandom();
    private static final String ALPHANUMERIC_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int ID_LENGTH = 20; // 20 characters long

    @Override
    public Long generateLendingID() {
        StringBuilder result = new StringBuilder(ID_LENGTH);

        for (int i = 0; i < ID_LENGTH; i++) {
            // Choose a random character from the ALPHANUMERIC_CHARACTERS string
            int index = secureRandom.nextInt(ALPHANUMERIC_CHARACTERS.length());
            result.append(ALPHANUMERIC_CHARACTERS.charAt(index));
        }

        return Long.valueOf(result.toString());
    }
}
