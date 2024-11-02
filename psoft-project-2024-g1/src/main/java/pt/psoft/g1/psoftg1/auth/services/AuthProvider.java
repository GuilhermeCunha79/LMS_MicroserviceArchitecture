package pt.psoft.g1.psoftg1.auth.services;

import org.springframework.security.core.Authentication;
import net.minidev.json.parser.ParseException;
import pt.psoft.g1.psoftg1.auth.api.AuthRequest;

public interface AuthProvider {
    Authentication authenticate(String request);
}
