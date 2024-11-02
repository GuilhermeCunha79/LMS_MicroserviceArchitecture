package pt.psoft.g1.psoftg1.auth.api;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthProvider authProvider;

    public Authentication authenticate (AuthRequest request) {
        return authProvider.authenticate(request);
    }
}
