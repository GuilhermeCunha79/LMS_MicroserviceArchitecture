package pt.psoft.g1.psoftg1.authormanagement.infrastructure.repositories.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.authormanagement.repositories.AuthorRepository;

import java.util.List;
import java.util.Optional;

@Repository
@Profile("relational")
@RequiredArgsConstructor
public class RelationalAuthorRepository implements AuthorRepository {
    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public Optional<Author> findByAuthorNumber(String authorNumber) {
        return Optional.ofNullable(entityManager.find(Author.class, authorNumber));
    }

    @Override
    @Transactional
    public Author save(Author author) {
        System.out.println("Author savedddddd: " + author.getAuthorNumber());
        author.setAuthorNumber(author.getAuthorNumber());
        entityManager.persist(author);

        System.out.println("Author saved: " + author.getAuthorNumber());
        return author;
    }


    @Override
    public void delete(Author author) {
        entityManager.remove(entityManager.contains(author) ? author : entityManager.merge(author));
    }

    @Override
    public List<Author> searchByNameName(String name) {
        return entityManager.createQuery("SELECT a FROM Author a WHERE a.name.name = :name", Author.class)
                .setParameter("name", name)
                .getResultList();
    }
}
