package pt.psoft.g1.psoftg1.readermanagement.model;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;

@Embeddable
@AllArgsConstructor
public class EmailAddress {
    @Email
    String address;

    protected EmailAddress() {}
}
