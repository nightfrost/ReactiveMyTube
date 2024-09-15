package io.nightfrost.reactivemytube.handlers;

import io.nightfrost.reactivemytube.auth.models.LoginRequest;
import io.nightfrost.reactivemytube.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.util.Logger;
import reactor.util.Loggers;

import static org.springframework.web.reactive.function.BodyInserters.fromValue;

@Component
@RequiredArgsConstructor
public class AuthHandler {

    private final Logger LOGGER = Loggers.getLogger(AuthHandler.class);

    private final AuthService authService;

    public Mono<ServerResponse> login(ServerRequest request) {
        return request.bodyToMono(LoginRequest.class)
                .flatMap(authService::login)
                .flatMap(this::mapEntityToServerResponse)
                .log(LOGGER);
    }

    public Mono<ServerResponse> renew(ServerRequest request) {
        return null;
    }

    public Mono<ServerResponse> getRoles(ServerRequest request) {
        return null;
    }

    private Mono<ServerResponse> mapEntityToServerResponse(ResponseEntity<?> entity) {
        return ServerResponse.status(entity.getStatusCode()).body(fromValue(entity));
    }
}
