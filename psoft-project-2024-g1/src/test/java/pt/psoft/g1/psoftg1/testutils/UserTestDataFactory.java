package pt.psoft.g1.psoftg1.testutils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pt.psoft.g1.psoftg1.usermanagement.api.UserView;
import pt.psoft.g1.psoftg1.usermanagement.api.UserViewMapper;
import pt.psoft.g1.psoftg1.usermanagement.model.User;
import pt.psoft.g1.psoftg1.usermanagement.services.CreateUserRequest;
import pt.psoft.g1.psoftg1.usermanagement.services.UserService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Based on https://github.com/Yoh0xFF/java-spring-security-example
 *
 */
@Service
public class UserTestDataFactory {

    @Autowired
    private UserService userService;
    @Autowired
    private UserViewMapper userViewMapper;

    @Transactional
    public UserView createUser(final String username, final String fullName, final String password) {
        final CreateUserRequest createRequest = new CreateUserRequest(username, fullName, password);

        final User user = userService.create(createRequest);

        assertNotNull(user.getId(), "User id must not be null!");
        assertEquals(fullName, user.getName().toString(), "User name update isn't applied!");

        return userViewMapper.toUserView(user);
    }

    public UserView createUser(final String username, final String fullName) {
        return createUser(username, fullName, "Test12345_");
    }

    @Transactional
    public void deleteUser(final Long id) {
        userService.delete(id);
    }

}