package io.nightfrost.reactivemytube.services;

import io.nightfrost.reactivemytube.auth.JwtTokenProvider;
import io.nightfrost.reactivemytube.auth.models.AuthResponse;
import io.nightfrost.reactivemytube.auth.models.LoginRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.util.Logger;
import reactor.util.Loggers;

import java.util.List;

@AllArgsConstructor
@Service
public class AuthServiceImpl implements AuthService{

    private final Logger LOGGER = Loggers.getLogger(AuthServiceImpl.class);
    private final ReactiveAuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public Mono<ResponseEntity<AuthResponse>> login(LoginRequest loginRequest) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(),
                loginRequest.getPassword());

        return authenticationManager.authenticate(authentication).map(auth -> {
            String token = jwtTokenProvider.generateToken(auth);
            return new AuthResponse(token);
        }).flatMap(authResponse -> Mono.just(ResponseEntity.ok(authResponse))).log(LOGGER);
    }

    @Override
    public Mono<ResponseEntity<String>> renew(String token) {
        return null;
    }

    @Override
    public Mono<ResponseEntity<List<String>>> getRoles(String username) {
        return null;
    }
}
