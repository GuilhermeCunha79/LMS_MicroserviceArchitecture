/*
 * Copyright (c) 2022-2024 the original author or authors.
 *
 * MIT License
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package pt.psoft.g1.psoftg1.usermanagement.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import pt.psoft.g1.psoftg1.shared.repositories.ForbiddenNameRepository;
import pt.psoft.g1.psoftg1.shared.services.Page;
import pt.psoft.g1.psoftg1.usermanagement.model.User;
import pt.psoft.g1.psoftg1.usermanagement.repositories.UserRepository;

import java.util.List;
import java.util.Optional;

/**
 * Based on https://github.com/Yoh0xFF/java-spring-security-example
 */
@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepo;
    private final ForbiddenNameRepository forbiddenNameRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepositoryType, ForbiddenNameRepository forbiddenNameRepository, PasswordEncoder passwordEncoder) {
        this.forbiddenNameRepository = forbiddenNameRepository;
        this.passwordEncoder = passwordEncoder;
        this.userRepo = userRepositoryType;

    }

    public List<User> findByName(String name) {
        return this.userRepo.findByNameName(name);
    }

    public List<User> findByNameLike(String name) {
        return this.userRepo.findByNameNameContains(name);
    }

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        return userRepo.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException(String.format("User with username - %s, not found", username)));
    }



    public User getUser(final Long id) {
        return userRepo.getById(id);
    }

    public Optional<User> findByUsername(final String username) {
        return userRepo.findByUsername(username);
    }

    public List<User> searchUsers(Page page, SearchUsersQuery query) {
        if (page == null) {
            page = new Page(1, 10);
        }
        if (query == null) {
            query = new SearchUsersQuery("", "");
        }
        return userRepo.searchUsers(page, query);
    }

    public User getAuthenticatedUser(Authentication authentication) {
        if (authentication == null || !(authentication.getPrincipal() instanceof Jwt jwt)) {
            throw new AccessDeniedException("User is not logged in");
        }

        // split is present because jwt is storing the id before the username, separated by a comma
        String loggedUsername = jwt.getClaimAsString("sub").split(",")[1];

        Optional<User> loggedUser = findByUsername(loggedUsername);
        if (loggedUser.isEmpty()) {
            throw new AccessDeniedException("User is not logged in");
        }

        return loggedUser.get();
    }
}
