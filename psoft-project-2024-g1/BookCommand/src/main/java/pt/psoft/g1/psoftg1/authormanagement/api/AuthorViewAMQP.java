package pt.psoft.g1.psoftg1.authormanagement.api;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Schema(description = "A Book form AMQP communication")
public class AuthorViewAMQP {
    @NotNull
    private String authorNumber;
    @NotNull
    private String name;
    @NotNull
    private String bio;
    private String photo;
    @NotNull
    private Long version;
    @Setter
    @Getter
    private Map<String, Object> _links = new HashMap<>();

}
