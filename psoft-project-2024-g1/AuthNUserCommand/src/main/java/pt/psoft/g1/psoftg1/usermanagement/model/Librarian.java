package pt.psoft.g1.psoftg1.usermanagement.model;

import jakarta.persistence.Entity;
import pt.psoft.g1.psoftg1.shared.model.Name;

@Entity
public class Librarian extends User {
    protected Librarian() {
        // for ORM only
    }
    public Librarian(String username, String password) {
        super(username, password);
    }

    /**
     * factory method. since mapstruct does not handle protected/private setters
     * neither more than one public constructor, we use these factory methods for
     * helper creation scenarios
     *
     * @param username
     * @param password
     * @param name
     * @return
     */

    public static Librarian newLibrarian(final String username, final String password, final String name) {
        final var u = new Librarian(username, password);
        u.setName(name);
        u.addAuthority(new Role(Role.LIBRARIAN));
        return u;
    }
}
