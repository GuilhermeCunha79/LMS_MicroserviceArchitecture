package pt.psoft.g1.psoftg1.readermanagement.services;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;

import java.util.List;

@Getter
@Setter
@Data
@NoArgsConstructor
public class UpdateReaderRequest {
    @Setter
    @Getter
    private String number;

    @Getter
    @Setter
    @Nullable
    private String username;

    @Getter
    @Setter
    @Nullable
    private String password;

    @Getter
    @Nullable
    private String fullName;

    @Getter
    @Nullable
    private String birthDate;

    @Getter
    @Nullable
    private String phoneNumber;

    @Nullable
    private boolean marketing;

    @Nullable
    private boolean thirdParty;

    @Nullable
    @Getter
    @Setter
    private List<String> interestList;

    @Nullable
    @Getter
    @Setter
    private MultipartFile photo;

    public boolean getThirdParty() {
        return thirdParty;
    }

    public boolean getMarketing() {
        return marketing;
    }
}
