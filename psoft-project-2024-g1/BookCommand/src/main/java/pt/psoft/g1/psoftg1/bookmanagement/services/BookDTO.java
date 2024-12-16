package pt.psoft.g1.psoftg1.bookmanagement.services;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookDTO {
    private String isbn;
    private String title;
    private String description;
    private String genre;
    private List<String> authors;
    private String photoURI;
}
