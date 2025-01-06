package pt.psoft.g1.psoftg1.authormanagement.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.authormanagement.publishers.AuthorEventsPublisher;
import pt.psoft.g1.psoftg1.authormanagement.repositories.AuthorRepository;
import pt.psoft.g1.psoftg1.exceptions.ConflictException;
import pt.psoft.g1.psoftg1.exceptions.NotFoundException;
import pt.psoft.g1.psoftg1.shared.api.AuthorViewAMQP;
import pt.psoft.g1.psoftg1.shared.model.generateID.GenerateIDService;
import pt.psoft.g1.psoftg1.shared.repositories.PhotoRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;
    private final AuthorMapper mapper;
    private final PhotoRepository photoRepository;
    private final GenerateIDService authorIDService;
    private final AuthorEventsPublisher authorEventsPublisher;

    @Override
    public Optional<Author> findByAuthorNumber(final String authorNumber) {
        return authorRepository.findByAuthorNumber(authorNumber);
    }


    @Override
    public Author create(final CreateAuthorRequest resource) {
        /*
         * Since photos can be null (no photo uploaded) that means the URI can be null as well.
         * To avoid the client sending false data, photoURI has to be set to any value / null
         * according to the MultipartFile photo object
         *
         * That means:
         * - photo = null && photoURI = null -> photo is removed
         * - photo = null && photoURI = validString -> ignored
         * - photo = validFile && photoURI = null -> ignored
         * - photo = validFile && photoURI = validString -> photo is set
         * */

        MultipartFile photo = resource.getPhoto();
        String photoURI = resource.getPhotoURI();
        if (photo == null && photoURI != null || photo != null && photoURI == null) {
            resource.setPhoto(null);
            resource.setPhotoURI(null);
        }
        final Author author = mapper.create(resource);
        author.setAuthorNumber(authorIDService.generateID());

        authorEventsPublisher.sendAuthorCreated(author);

        return authorRepository.save(author);
    }

    @Override
    public Author create(AuthorViewAMQP authorViewAMQP) {

        final String authorNumber = authorViewAMQP.getAuthorNumber();
        final String name = authorViewAMQP.getName();
        final String bio = authorViewAMQP.getBio();
        final String photo = authorViewAMQP.getPhoto();
        // Chama o método create com os dados extraídos
        Author author = create(authorNumber, name, bio, photo);
        System.out.println("TERCEIROOOOOO" + author.getAuthorNumber());
        return author;
    }

    private Author create(String authorNumber,
                          String name,
                          String bio,
                          String photoURI) {

        if (authorRepository.findByAuthorNumber(authorNumber).isPresent()) {
            throw new ConflictException("Author with AuthorNumber " + authorNumber + " already exists");
        }

        Author newAuthor = new Author(authorNumber, name, bio, photoURI);
        return authorRepository.save(newAuthor);
    }

    @Override
    public Author partialUpdate(final String authorNumber, final UpdateAuthorRequest request, final long desiredVersion) {
        // first let's check if the object exists, so we don't create a new object with
        // save
        final var author = findByAuthorNumber(authorNumber)
                .orElseThrow(() -> new NotFoundException("Cannot update an object that does not yet exist"));
        /*
         * Since photos can be null (no photo uploaded) that means the URI can be null as well.
         * To avoid the client sending false data, photoURI has to be set to any value / null
         * according to the MultipartFile photo object
         *
         * That means:
         * - photo = null && photoURI = null -> photo is removed
         * - photo = null && photoURI = validString -> ignored
         * - photo = validFile && photoURI = null -> ignored
         * - photo = validFile && photoURI = validString -> photo is set
         * */

        MultipartFile photo = request.getPhoto();
        String photoURI = request.getPhotoURI();
        if (photo == null && photoURI != null || photo != null && photoURI == null) {
            request.setPhoto(null);
            request.setPhotoURI(null);
        }
        // since we got the object from the database we can check the version in memory
        // and apply the patch
        author.applyPatch(desiredVersion, request);

        // in the meantime some other user might have changed this object on the
        // database, so concurrency control will still be applied when we try to save
        // this updated object
        return authorRepository.save(author);
    }

    @Override
    public Optional<Author> removeAuthorPhoto(String authorNumber, long desiredVersion) {
        Author author = authorRepository.findByAuthorNumber(authorNumber)
                .orElseThrow(() -> new NotFoundException("Cannot find reader"));

        String photoFile = author.getPhoto().getPhotoFile();
        author.removePhoto(desiredVersion);
        Optional<Author> updatedAuthor = Optional.of(authorRepository.save(author));
        photoRepository.deleteByPhotoFile(photoFile);
        return updatedAuthor;
    }

}

