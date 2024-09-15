package io.nightfrost.reactivemytube.services;

import io.nightfrost.reactivemytube.auth.models.AuthResponse;
import io.nightfrost.reactivemytube.auth.models.LoginRequest;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

import java.util.List;

public interface AuthService {

    Mono<ResponseEntity<AuthResponse>> login(LoginRequest loginRequest);

    Mono<ResponseEntity<String>> renew(String token);

    Mono<ResponseEntity<List<String>>> getRoles(String username);
}
