package pt.psoft.g1.psoftg1.recommendationmanagement.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pt.psoft.g1.psoftg1.recommendationmanagement.services.CreateRecommendationRequest;
import pt.psoft.g1.psoftg1.recommendationmanagement.services.RecommendationService;

@Tag(name = "Readers", description = "Endpoints to manage readers")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/recommendations")
public class RecommendationController {
    private final RecommendationService recommendationService;
    private final RecommendationViewMapper recommendationViewMapper;

    @Operation(summary = "Creates a new Recommendation")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<RecommendationView> create(@RequestBody @Valid CreateRecommendationRequest resource) {


        final var author = recommendationService.create(resource);

        final var newauthorUri = ServletUriComponentsBuilder.fromCurrentRequestUri()
                .build().toUri();

        return ResponseEntity.created(newauthorUri).eTag(Long.toString(author.getVersion()))
                .body(recommendationViewMapper.toRecommendationView(author));
    }

}
