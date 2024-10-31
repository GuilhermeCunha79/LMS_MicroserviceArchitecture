package pt.psoft.g1.psoftg1.usermanagement.model;

import org.springframework.stereotype.Component;

@Component
public class UserFactory {
    public static User create(String username, String password) {
        return new User(username, password);
    }
    public static UserMongo createMongo(String username, String password) {
        return new UserMongo(username, password);
    }
}
