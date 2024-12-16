package pt.psoft.g1.psoftg1.readermanagement.services;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchReadersQuery {
    private String name;
    private String phoneNumber;
    private String email;
}
