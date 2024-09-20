package pt.psoft.g1.psoftg1.readermanagement.api;

import jakarta.annotation.Nullable;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@NoArgsConstructor
public class TestBody {
    @Nullable
    @Getter
    @Setter
    private String fullName;

    @Nullable
    @Getter
    @Setter
    private MultipartFile photo;

    @Nullable
    @Getter
    @Setter
    private List<Long> ages;
}
