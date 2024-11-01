package pt.psoft.g1.psoftg1.lendingmanagement.model.generateID;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component("LendingIDService1")
public class LendingIDService1Impl implements LendingIDService {

    private static final SecureRandom secureRandom = new SecureRandom();

    @Override
    public Long generateLendingID() {
        // Gera um valor hexadecimal aleatório de até 16 caracteres
        long randomLong = secureRandom.nextLong();

        // Converte o número gerado para um Long
        return Math.abs(randomLong);  // Evita números negativos
    }
}