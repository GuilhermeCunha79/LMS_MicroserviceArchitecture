package pt.psoft.g1.psoftg1.auth.services;

import org.springframework.security.core.Authentication;

public interface AuthProvider {
    Authentication authenticate(String request);
}
