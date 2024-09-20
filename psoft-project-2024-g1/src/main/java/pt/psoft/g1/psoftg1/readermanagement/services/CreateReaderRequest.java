package pt.psoft.g1.psoftg1.readermanagement.services;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;

import java.util.List;

@Data
@NoArgsConstructor
public class CreateReaderRequest {
    @NotBlank
    @Email
    @NonNull
    private String username;

    @NotBlank
    @NonNull
    private String password;

    @NotBlank
    @NonNull
    private String fullName;

    @NonNull
    @NotBlank
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private String birthDate;

    @NonNull
    @NotBlank
    private String phoneNumber;

    @Nullable
    @Getter
    @Setter
    private MultipartFile photo;

    @Setter
    private boolean gdpr;

    @Setter
    private boolean marketing;

    @Setter
    private boolean thirdParty;

    @Nullable
    @Getter
    @Setter
    private List<String> interestList;

    public boolean getThirdParty() {
        return thirdParty;
    }

    public boolean getMarketing() {
        return marketing;
    }

    public boolean getGdpr() {
        return gdpr;
    }
}
