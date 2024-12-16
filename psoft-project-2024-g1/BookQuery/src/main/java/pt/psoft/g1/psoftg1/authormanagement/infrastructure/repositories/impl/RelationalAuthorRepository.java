package pt.psoft.g1.psoftg1.authormanagement.infrastructure.repositories.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pt.psoft.g1.psoftg1.authormanagement.api.AuthorLendingView;
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
    public Page<AuthorLendingView> findTopAuthorByLendings(Pageable pageable) {

        List<AuthorLendingView> results = entityManager.createQuery(
                        "SELECT new pt.psoft.g1.psoftg1.authormanagement.api.AuthorLendingView(a.name.name, COUNT(l.pk)) " +
                                "FROM Book b " +
                                "JOIN b.authors a " +
                                "JOIN Lending l ON l.book.pk = b.pk " +
                                "GROUP BY a.name " +
                                "ORDER BY COUNT(l) DESC", AuthorLendingView.class)
                .setFirstResult(pageable.getPageNumber() * pageable.getPageSize())
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        long totalResults = entityManager.createQuery(
                        "SELECT COUNT(DISTINCT a) " +
                                "FROM Book b " +
                                "JOIN b.authors a " +
                                "JOIN Lending l ON l.book.pk = b.pk", Long.class)
                .getSingleResult();

        return new PageImpl<>(results, pageable, totalResults);
    }

    @Override
    public List<Author> findCoAuthorsByAuthorNumber(String authorNumber) {
        return entityManager.createQuery(
                        "SELECT DISTINCT coAuthor FROM Book b " +
                                "JOIN b.authors coAuthor " +
                                "WHERE b IN (SELECT b FROM Book b JOIN b.authors a WHERE a.authorNumber = :authorNumber) " +
                                "AND coAuthor.authorNumber <> :authorNumber", Author.class)
                .setParameter("authorNumber", authorNumber)
                .getResultList();
    }

    @Override
    public List<Author> searchByNameNameStartsWith(String name) {
        return entityManager.createQuery("SELECT a FROM Author a WHERE a.name.name LIKE :name", Author.class)
                .setParameter("name", name + "%")
                .getResultList();
    }

    @Override
    public List<Author> searchByNameName(String name) {
        return entityManager.createQuery("SELECT a FROM Author a WHERE a.name.name = :name", Author.class)
                .setParameter("name", name)
                .getResultList();
    }

    @Override
    @Transactional
    public Author save(Author author) {
        if (entityManager.contains(author)) {
            author = entityManager.merge(author);
        } else {
            entityManager.persist(author);
        }
        return author;
    }


    @Override
    public Iterable<Author> findAll() {
        return entityManager.createQuery("SELECT a FROM Author a", Author.class).getResultList();
    }

    @Override
    public void delete(Author author) {
        entityManager.remove(entityManager.contains(author) ? author : entityManager.merge(author));
    }
}
