package pt.psoft.g1.psoftg1.usermanagement.model;

import jakarta.persistence.Entity;

@Entity
public class Reader extends User {
    protected Reader() {
        // for ORM only
    }
    public Reader(String username, String password) {
        super(username, password);
        this.addAuthority(new Role(Role.READER));
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

    public static Reader newReader(final String username, final String password, final String name) {
        final var u = new Reader(username, password);
        u.setName(name);
        return u;
    }
}
