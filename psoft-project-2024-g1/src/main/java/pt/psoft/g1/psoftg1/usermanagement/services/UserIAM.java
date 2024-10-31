package pt.psoft.g1.psoftg1.usermanagement.services;

import pt.psoft.g1.psoftg1.usermanagement.model.User;

import java.util.Optional;

public interface UserIAM {
    Optional<User> authentication(String token);
}
