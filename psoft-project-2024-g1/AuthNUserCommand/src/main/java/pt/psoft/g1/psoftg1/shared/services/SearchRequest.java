package pt.psoft.g1.psoftg1.shared.services;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Based on <a href="https://github.com/Yoh0xFF/java-spring-security-example">https://github.com/Yoh0xFF/java-spring-security-example</a>
 *
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SearchRequest<T> {
    @Valid
    @NotNull
    Page page;

    @Valid
    @NotNull
    T query;
}