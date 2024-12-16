package pt.psoft.g1.psoftg1.authormanagement.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
public class AuthorCoAuthorBooksView {
    private AuthorView author;
    private List<CoAuthorView> coauthors;

}
