package pt.psoft.g1.psoftg1.lendingmanagement.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pt.psoft.g1.psoftg1.exceptions.NotFoundException;
import pt.psoft.g1.psoftg1.lendingmanagement.model.Lending;
import pt.psoft.g1.psoftg1.lendingmanagement.services.*;
import pt.psoft.g1.psoftg1.shared.api.ListResponse;
import pt.psoft.g1.psoftg1.shared.services.Page;
import pt.psoft.g1.psoftg1.shared.services.SearchRequest;

import java.util.List;

@Tag(name = "Lendings", description = "Endpoints for managing Lendings")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/lendings")
public class LendingController {
    private final LendingService lendingService;
    private final LendingViewMapper lendingViewMapper;

    @Operation(summary = "Gets a specific Lending")
    @GetMapping(value = "/{year}/{seq}")
    public ResponseEntity<LendingView> findByLendingNumber(
            Authentication authentication,
            @PathVariable("year")
                @Parameter(description = "The year of the Lending to find")
                final Integer year,
            @PathVariable("seq")
                @Parameter(description = "The sequencial of the Lending to find")
                final Integer seq) {

        String ln = year + "/" + seq;
        final var lending = lendingService.findByLendingNumber(ln)
                .orElseThrow(() -> new NotFoundException(Lending.class, ln));

        final var lendingUri = ServletUriComponentsBuilder.fromCurrentRequestUri()
                .build().toUri();

        return ResponseEntity.ok().location(lendingUri)
                .contentType(MediaType.parseMediaType("application/hal+json"))
                .eTag(Long.toString(lending.getVersion()))
                .body(lendingViewMapper.toLendingView(lending));
    }

    @Operation(summary = "Get average lendings duration")
    @GetMapping(value = "/avgDuration")
    public @ResponseBody ResponseEntity<LendingsAverageDurationView> getAvgDuration() {

        return ResponseEntity.ok().body(lendingViewMapper.toLendingsAverageDurationView(lendingService.getAverageDuration()));
    }

    @Operation(summary = "Get list of overdue lendings")
    @GetMapping(value = "/overdue")
    public ListResponse<LendingView> getOverdueLendings(@Valid @RequestBody Page page) {
        final List<Lending> overdueLendings = lendingService.getOverdue(page);
        if(overdueLendings.isEmpty())
            throw new NotFoundException("No lendings to show");
        return new ListResponse<>(lendingViewMapper.toLendingView(overdueLendings));
    }

    @PostMapping("/search")
    public ListResponse<LendingView> searchReaders(
            @RequestBody final SearchRequest<SearchLendingQuery> request) {
        final var readerList = lendingService.searchLendings(request.getPage(), request.getQuery());
        return new ListResponse<>(lendingViewMapper.toLendingView(readerList));
    }


}
