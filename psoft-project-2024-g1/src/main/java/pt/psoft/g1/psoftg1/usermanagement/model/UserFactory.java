package pt.psoft.g1.psoftg1.usermanagement.model;

import org.springframework.stereotype.Component;

@Component
public class UserFactory {
    public User create(String username, String password) {
        return new User(username, password);
    }
}
