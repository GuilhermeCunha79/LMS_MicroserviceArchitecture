package pt.psoft.g1.psoftg1.authormanagement.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.authormanagement.services.AuthorService;
import pt.psoft.g1.psoftg1.authormanagement.services.CreateAuthorRequest;
import pt.psoft.g1.psoftg1.authormanagement.services.UpdateAuthorRequest;
import pt.psoft.g1.psoftg1.bookmanagement.api.BookView;
import pt.psoft.g1.psoftg1.bookmanagement.api.BookViewMapper;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.exceptions.NotFoundException;
import pt.psoft.g1.psoftg1.readermanagement.model.ReaderDetails;
import pt.psoft.g1.psoftg1.shared.api.ListResponse;
import pt.psoft.g1.psoftg1.shared.services.ConcurrencyService;
import pt.psoft.g1.psoftg1.shared.services.FileStorageService;
import pt.psoft.g1.psoftg1.usermanagement.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Tag(name = "Author", description = "Endpoints for managing Authors")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/authors")
public class AuthorController {
    private final AuthorService authorService;
    private final AuthorViewMapper authorViewMapper;
    private final ConcurrencyService concurrencyService;
    private final FileStorageService fileStorageService;
    private final BookViewMapper bookViewMapper;


    //Create
    @Operation(summary = "Creates a new Author")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<AuthorView> create(@Valid CreateAuthorRequest resource) {
        //Guarantee that the client doesn't provide a link on the body, null = no photo or error
        resource.setPhotoURI(null);
        MultipartFile file = resource.getPhoto();

        String fileName = this.fileStorageService.getRequestPhoto(file);

        if (fileName != null) {
            resource.setPhotoURI(fileName);
        }

        final var author = authorService.create(resource);

        final var newauthorUri = ServletUriComponentsBuilder.fromCurrentRequestUri()
                .build().toUri();

        return ResponseEntity.created(newauthorUri).eTag(Long.toString(author.getVersion()))
                .body(authorViewMapper.toAuthorView(author));

    }


    //Update
    @Operation(summary = "Updates a specific author")
    @PatchMapping(value = "/{authorNumber}")
    public ResponseEntity<AuthorView> partialUpdate(
            @PathVariable("authorNumber")
            @Parameter(description = "The number of the Author to find") final Long authorNumber,
            final WebRequest request,
            @Valid UpdateAuthorRequest resource) {

        final String ifMatchValue = request.getHeader(ConcurrencyService.IF_MATCH);
        if (ifMatchValue == null || ifMatchValue.isEmpty() || ifMatchValue.equals("null")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "You must issue a conditional PATCH using 'if-match'");
        }

        MultipartFile file = resource.getPhoto();

        String fileName = this.fileStorageService.getRequestPhoto(file);

        if (fileName != null) {
            resource.setPhotoURI(fileName);
        }
        Author author = authorService.partialUpdate(authorNumber, resource, concurrencyService.getVersionFromIfMatchHeader(ifMatchValue));

        return ResponseEntity.ok()
                .eTag(Long.toString(author.getVersion()))
                .body(authorViewMapper.toAuthorView(author));
    }

    //Gets
    @Operation(summary = "Know an author’s detail given its author number")
    @GetMapping(value = "/{authorNumber}")
    public ResponseEntity<AuthorView> findByAuthorNumber(
            @PathVariable("authorNumber")
            @Parameter(description = "The number of the Author to find") final Long authorNumber) {

        final var author = authorService.findByAuthorNumber(authorNumber)
                .orElseThrow(() -> new NotFoundException(Author.class, authorNumber));

        return ResponseEntity.ok()
                .eTag(Long.toString(author.getVersion()))
                .body(authorViewMapper.toAuthorView(author));
    }

    @Operation(summary = "Search authors by name")
    @GetMapping
    public ListResponse<AuthorView> findByName(@RequestParam("name") final String name) {

        final var authors = authorService.findByName(name);
        return new ListResponse<>(authorViewMapper.toAuthorView(authors));
    }


    //Know the books of an Author
    @Operation(summary = "Know the books of an author")
    @GetMapping("/{authorNumber}/books")
    public ListResponse<BookView> getBooksByAuthorNumber(
           @PathVariable("authorNumber")
             @Parameter(description = "The number of the Author to find")
             final Long authorNumber) {

        //Checking if author exists with this id
        authorService.findByAuthorNumber(authorNumber)
                .orElseThrow(() -> new NotFoundException(Author.class, authorNumber));

        return new ListResponse<>(bookViewMapper.toBookView(authorService.findBooksByAuthorNumber(authorNumber)));
    }

    //Know the Top 5 authors which have the most lent books
    @Operation(summary = "Know the Top 5 authors which have the most lent books")
    @GetMapping("/top5")
    public ListResponse<AuthorLendingView> getTop5() {
        final var list = authorService.findTopAuthorByLendings();

        if(list.isEmpty())
            throw new NotFoundException("No authors to show");

        return new ListResponse<>(list);
    }

    //get - Photo
    @Operation(summary= "Gets a author photo")
    @GetMapping("/{authorNumber}/photo")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<byte[]> getSpecificAuthorPhoto(@PathVariable("authorNumber")
                                                             @Parameter(description = "The number of the Author to find")
                                                             final Long authorNumber) {

        Author authorDetails = authorService.findByAuthorNumber(authorNumber)
                .orElseThrow(() -> new NotFoundException(Author.class, authorNumber));

        //In case the user has no photo, just return a 200 OK without body
        if(authorDetails.getPhoto() == null) {
            return ResponseEntity.ok().build();
        }

        String photoFile = authorDetails.getPhoto().getPhotoFile();
        byte[] image = this.fileStorageService.getFile(photoFile);
        String fileFormat = this.fileStorageService.getExtension(authorDetails.getPhoto().getPhotoFile())
                .orElseThrow(() -> new ValidationException("Unable to get file extension"));

        if(image == null) {
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.ok()
                .contentType(fileFormat.equals("png") ? MediaType.IMAGE_PNG : MediaType.IMAGE_JPEG)
                .body(image);
    }
    //Co-authors and their respective books
    @Operation(summary = "Get co-authors and their respective books for a specific author")
    @GetMapping("/{authorNumber}/coauthors")
    public AuthorCoAuthorBooksView getAuthorWithCoAuthors(@PathVariable("authorNumber")Long authorNumber) {
        var author = authorService.findByAuthorNumber(authorNumber)
                .orElseThrow(() -> new NotFoundException("Author not found"));
        var coAuthors = authorService.findCoAuthorsByAuthorNumber(authorNumber);
        List<CoAuthorView> coAuthorViews = new ArrayList<>();
        for (Author coAuthor : coAuthors ) {
            var books = authorService.findBooksByAuthorNumber(coAuthor.getAuthorNumber());
            var coAuthorView = authorViewMapper.toCoAuthorView(coAuthor,books);
            coAuthorViews.add(coAuthorView);
        }
        return authorViewMapper.toAuthorCoAuthorBooksView(author, coAuthorViews);
    }

    //Delete a foto
    @Operation(summary = "Deletes a author photo")
    @DeleteMapping("/{authorNumber}/photo")
    public ResponseEntity<Void> deleteBookPhoto(@PathVariable("authorNumber") final Long authorNumber) {

        Optional<Author> optionalAuthor = authorService.findByAuthorNumber(authorNumber);
        if(optionalAuthor.isEmpty()) {
            throw new AccessDeniedException("A author could not be found with provided authorNumber");
        }
        Author author = optionalAuthor.get();
        if(author.getPhoto() == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        this.fileStorageService.deleteFile(author.getPhoto().getPhotoFile());
        authorService.removeAuthorPhoto(author.getAuthorNumber(), author.getVersion());

        return ResponseEntity.ok().build();
    }
}
