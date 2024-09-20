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

import java.time.Instant;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

	@PostMapping("login")
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

}
