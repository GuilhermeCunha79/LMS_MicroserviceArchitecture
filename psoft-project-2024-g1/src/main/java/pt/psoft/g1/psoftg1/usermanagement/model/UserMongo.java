package pt.psoft.g1.psoftg1.usermanagement.model;

import jakarta.persistence.Embedded;
import jakarta.persistence.Id;
import jakarta.persistence.Version;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import pt.psoft.g1.psoftg1.shared.model.Generator;
import pt.psoft.g1.psoftg1.shared.model.Name;

import java.io.Serial;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Document(collection = "user") // Nome da coleção no MongoDB
public class UserMongo implements UserDetails {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    @Version
    private Long version;

    @Field("created_at")
    private LocalDateTime createdAt;


    @Field("modified_at")
    private LocalDateTime modifiedAt;


    @Field("created_by")
    private String createdBy;


    @Field("modified_by")
    private String modifiedBy;

    @Setter
    @Getter
    @Field("enabled")
    private boolean enabled = true;

    @Setter
    @Indexed(unique = true)
    @NotNull
    @NotBlank
    @Email
    @Getter
    @Field("username")
    private String username;

    @NotNull
    @NotBlank
    @Getter
    @Field("password")
    private String password;

    @Embedded
    private Name name;

    @Field("authorities")
    @Getter
    private Set<Role> authorities = new HashSet<>();


    protected UserMongo() {
        // for ORM only
    }

    public UserMongo(final String username, final String password) {
        this.id = Generator.generateLongID(); // Se você usar String, pode usar UUID.randomUUID().toString()
        this.username = username;
        setPassword(password);
    }

    public static User newUser(final String username, final String password, final String name) {
        final var u = new User(username, password);
        u.setName(name);
        return u;
    }

    public static User newUser(final String username, final String password, final String name, final String role) {
        final var u = new User(username, password);
        u.setName(name);
        u.addAuthority(new Role(role));
        return u;
    }

    public void setPassword(final String password) {
        Password passwordCheck = new Password(password);
        final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        this.password = passwordEncoder.encode(password);
    }

    public void addAuthority(final Role r) {
        authorities.add(r);
    }

    @Override
    public boolean isAccountNonExpired() {
        return isEnabled();
    }

    @Override
    public boolean isAccountNonLocked() {
        return isEnabled();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isEnabled();
    }

    public void setName(String name) {
        this.name = new Name(name);
    }
}
