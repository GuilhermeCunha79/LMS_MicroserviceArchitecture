package pt.psoft.g1.psoftg1.authormanagement.model.generateID;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import java.security.SecureRandom;

@Component("AuthorIDService1")
public class AuthorIDService1Impl implements AuthorIDService {

    private static final SecureRandom secureRandom = new SecureRandom();

    @Override
    public String generateAuthorID() {
        long randomLong = secureRandom.nextLong();

        return String.valueOf(randomLong);
    }
}