package pt.psoft.g1.psoftg1.auth.api;

import org.springframework.security.core.Authentication;

public interface AuthProvider {

    Authentication authenticate(AuthRequest request);
}
