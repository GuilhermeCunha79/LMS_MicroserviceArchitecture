package pt.psoft.g1.psoftg1.readermanagement.api;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;

import java.util.List;

@Data
@Schema(description = "A Reader from AMQP communication")
public class ReaderDetailsViewAMQP {
    private String readerNumber;
    private String email;
    private String fullName;
    private String birthDate;
    private String phoneNumber;
    private boolean gdprConsent;
    private boolean marketingConsent;
    private boolean thirdPartySharingConsent;
    private List<String> interestList;
}
