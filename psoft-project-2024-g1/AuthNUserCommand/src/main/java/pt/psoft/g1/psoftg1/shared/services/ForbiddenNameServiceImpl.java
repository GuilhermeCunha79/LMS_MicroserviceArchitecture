package pt.psoft.g1.psoftg1.shared.services;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import pt.psoft.g1.psoftg1.shared.model.ForbiddenName;
import pt.psoft.g1.psoftg1.shared.repositories.ForbiddenNameRepository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Service
@RequiredArgsConstructor
public class ForbiddenNameServiceImpl implements ForbiddenNameService {
    private final ForbiddenNameRepository repo;

    public void loadDataFromFile(String fileName) {
        try {
            ClassPathResource resource = new ClassPathResource(fileName);
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    final var fn = repo.findByForbiddenName(line);
                    if (fn.isEmpty()) {
                        ForbiddenName entity = new ForbiddenName(line);
                        repo.save(entity);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
