package pt.psoft.g1.psoftg1.authormanagement.model.generateID;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import java.security.SecureRandom;

@Component("AuthorIDService1")
@Primary
public class AuthorIDService1Impl implements AuthorIDService {

    private static final SecureRandom secureRandom = new SecureRandom();

    @Override
    public Long generateAuthorID() {
        // Gera um valor hexadecimal aleatório de até 16 caracteres
        long randomLong = secureRandom.nextLong();

        // Converte o número gerado para um Long
        return Math.abs(randomLong);  // Evita números negativos
    }
}