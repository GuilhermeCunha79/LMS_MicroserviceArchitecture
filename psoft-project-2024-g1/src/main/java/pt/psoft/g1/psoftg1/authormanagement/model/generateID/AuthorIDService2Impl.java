package pt.psoft.g1.psoftg1.authormanagement.model.generateID;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import pt.psoft.g1.psoftg1.authormanagement.api.AuthorView;
import pt.psoft.g1.psoftg1.authormanagement.services.CreateAuthorRequest;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

@Component("AuthorIDService2")
@Primary
public class AuthorIDService2Impl implements AuthorIDService {

    private static final SecureRandom secureRandom = new SecureRandom();
    private static final String ALPHANUMERIC_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int ID_LENGTH = 20; // 20 characters long

    @Override
    public String generateAuthorID() {
        StringBuilder result = new StringBuilder(ID_LENGTH);

        for (int i = 0; i < ID_LENGTH; i++) {
            // Choose a random character from the ALPHANUMERIC_CHARACTERS string
            int index = secureRandom.nextInt(ALPHANUMERIC_CHARACTERS.length());
            result.append(ALPHANUMERIC_CHARACTERS.charAt(index));
        }

        return String.valueOf(result);
    }
}
