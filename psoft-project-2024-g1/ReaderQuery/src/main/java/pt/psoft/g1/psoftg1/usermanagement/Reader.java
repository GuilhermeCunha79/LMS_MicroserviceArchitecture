package pt.psoft.g1.psoftg1.usermanagement;

import jakarta.persistence.Entity;

@Entity
public class Reader extends User {
    protected Reader() {
        // for ORM only
    }

    public Reader(String username) {
        super(username);
        this.addAuthority(new pt.psoft.g1.psoftg1.usermanagement.Role(Role.READER));
    }

    /**
     * factory method. since mapstruct does not handle protected/private setters neither more than one public
     * constructor, we use these factory methods for helper creation scenarios
     *
     * @param username
     * @param name
     * 
     * @return
     */

    public static Reader newReader(final String username, final String name) {
        final var u = new Reader(username);
        u.setName(name);
        return u;
    }
}
