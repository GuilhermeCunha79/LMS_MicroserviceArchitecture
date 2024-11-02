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
package pt.psoft.g1.psoftg1.auth.api;

import static java.lang.String.format;
import static java.util.stream.Collectors.joining;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.client.RestTemplate;
import pt.psoft.g1.psoftg1.usermanagement.api.UserView;
import pt.psoft.g1.psoftg1.usermanagement.api.UserViewMapper;
import pt.psoft.g1.psoftg1.usermanagement.model.User;
import pt.psoft.g1.psoftg1.usermanagement.services.CreateUserRequest;
import pt.psoft.g1.psoftg1.usermanagement.services.UserService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * Based on https://github.com/Yoh0xFF/java-spring-security-example
 *
 */
@Tag(name = "Authentication")
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "api/public")
public class AuthApi {

	private final AuthenticationManager authenticationManager;

	private final JwtEncoder jwtEncoder;

	private final UserViewMapper userViewMapper;

	private final UserService userService;

	private final AuthService authService;

	@Value("${spring.security.oauth2.client.registration.google.client-secret}")
	private String clientSecret;

	@Value("${spring.security.oauth2.client.registration.google.client-id}")
	private String clientId;

	@Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
	private String redirectUrii;

	/*@GetMapping("login")
	public ResponseEntity<Void> loginPage() {
		String redirectUri = "http://localhost:8081/api/public/login/oauth2/code/google";
		String clientId = "44446670216-cpcs16r8h1d8f589q9lafnpgnghlahur.apps.googleusercontent.com";
		String scope = "openid+profile+email";

		String url = String.format(
				"https://accounts.google.com/o/oauth2/auth?client_id=%s&redirect_uri=%s&response_type=code&scope=%s",
				clientId,
				redirectUri,
				scope
		);

		return ResponseEntity.status(HttpStatus.FOUND) // 302 Found
				.location(URI.create(url))
				.build();
	}

	/*@PostMapping("login")
	public ResponseEntity<UserView> login(@RequestBody @Valid final AuthRequest request) {
		try {
			final Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

			// if the authentication is successful, Spring will store the authenticated user
			// in its "principal"
			final User user = (User) authentication.getPrincipal();

			final Instant now = Instant.now();
			final long expiry = 36000L; // 1 hours is usually too long for a token to be valid. adjust for production

			final String scope = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority)
					.collect(joining(" "));

			final JwtClaimsSet claims = JwtClaimsSet.builder().issuer("example.io").issuedAt(now)
					.expiresAt(now.plusSeconds(expiry)).subject(format("%s,%s", user.getId(), user.getUsername()))
					.claim("roles", scope).build();

			final String token = this.jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

			return ResponseEntity.ok().header(HttpHeaders.AUTHORIZATION, token).body(userViewMapper.toUserView(user));
		} catch (final BadCredentialsException ex) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
	}*/
	@GetMapping("/login/oauth2/code/google")
	public ResponseEntity<UserView> loginWithGoogle(@RequestParam("code") String code) {
    	Optional<User> userOptional = Optional.ofNullable(userService.loginIam(exchangeCodeForToken(code)));

    	if (userOptional.isPresent()) {
     	   User user = userOptional.get();
     	   return ResponseEntity.ok(userViewMapper.toUserView(user));
    	}

    	return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	}




	@GetMapping("login")
	public ResponseEntity<Void> loginPage() {
		try {
			String redirectUri = "http://localhost:8081/api/public/login/oauth2/code/google";
			String clientId = "44446670216-cpcs16r8h1d8f589q9lafnpgnghlahur.apps.googleusercontent.com";
			String scope = "openid profile email";

			// Codifica os parâmetros para URL
			String encodedRedirectUri = URLEncoder.encode(redirectUri, StandardCharsets.UTF_8);
			String encodedScope = URLEncoder.encode(scope, StandardCharsets.UTF_8);

			// Monta a URL com os parâmetros codificados
			String url = String.format(
					"https://accounts.google.com/o/oauth2/auth?client_id=%s&redirect_uri=%s&response_type=code&scope=%s",
					clientId,
					encodedRedirectUri,
					encodedScope
			);

			return ResponseEntity.status(HttpStatus.FOUND) // 302 Found
					.location(URI.create(url))
					.build();
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}



	/*@PostMapping("/login")
	public ResponseEntity<UserView> loginWithFirebase(@RequestBody @Valid final String idToken) {
		try {
			// Verifica o token de ID do Firebase
			FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken);
			String uid = decodedToken.getUid();

			// Busca ou cria um usuário com base no UID do Firebase
			User user = userService.getUser(Long.valueOf(uid));

			return ResponseEntity.ok(userViewMapper.toUserView(user));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
	}

	 */
	@PostMapping("/login")
	public ResponseEntity<UserView> login(@RequestBody @Valid final AuthRequest request) {
		try {
			final Authentication authentication = authService.authenticate(request);

			final User user = userService.findByUsername(authentication.getName()).orElseThrow();
			System.out.println("User: " + user);
			final Instant now = Instant.now();
			final long expiry = 36000L; // 1 hours is usually too long for a token to be valid. adjust for production

			final String scope = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority)
					.collect(joining(" "));
			System.out.println("tou aqui");
			final JwtClaimsSet claims = JwtClaimsSet.builder().issuer("example.io").issuedAt(now)
					.expiresAt(now.plusSeconds(expiry)).subject(format("%s,%s", user.getId(), user.getUsername()))
					.claim("roles", scope).build();
			System.out.println("tou aqui");
			final String token = this.jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
			System.out.println("tou aqui");

			return ResponseEntity.ok().header(HttpHeaders.AUTHORIZATION, token).body(userViewMapper.toUserView(user));
		} catch (final BadCredentialsException ex) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
	}

	private String exchangeCodeForToken(String code) {
		RestTemplate restTemplate = new RestTemplate();
		String tokenUrl = "https://oauth2.googleapis.com/token";

		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("code", code);
		params.add("client_id", clientId);
		params.add("client_secret", clientSecret);
		params.add("redirect_uri", redirectUrii);
		params.add("grant_type", "authorization_code");

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

		try {
			ResponseEntity<Map> response = restTemplate.postForEntity(tokenUrl, request, Map.class);
			if (response.getStatusCode() == HttpStatus.OK) {
				Map<String, Object> responseBody = response.getBody();
				return (String) responseBody.get("access_token");
			} else {
				System.out.println("Response Status Code: " + response.getStatusCode());
				System.out.println("Response Body: " + response.getBody());
				throw new RuntimeException("Failed to obtain access token: " + response.getStatusCode());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	/**
	 * signup to the service
	 *
	 * @param request
	 * @return
	 */
	@PostMapping("register")
	public UserView register(@RequestBody @Valid final CreateUserRequest request) {
		final var user = userService.create(request);
		return userViewMapper.toUserView(user);
	}

	@GetMapping("/loginSuccess")
	public ResponseEntity<UserView> loginSuccess(Authentication authentication) {
		// Aqui você pode acessar o usuário autenticado e retornar suas informações.
		final User user = (User) authentication.getPrincipal();
		return ResponseEntity.ok(userViewMapper.toUserView(user));
	}
}
