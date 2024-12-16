package pt.psoft.g1.psoftg1.auth.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import pt.psoft.g1.psoftg1.auth.api.AuthRequest;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthProvider authProvider;



    public Authentication authentication(String authRequest){
        return authProvider.authenticate(authRequest);
    }
}
