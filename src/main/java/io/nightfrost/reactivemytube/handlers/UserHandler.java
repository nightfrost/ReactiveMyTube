package io.nightfrost.reactivemytube.handlers;

import io.nightfrost.reactivemytube.auth.JwtTokenProvider;
import io.nightfrost.reactivemytube.auth.models.AuthResponse;
import io.nightfrost.reactivemytube.auth.models.LoginRequest;
import io.nightfrost.reactivemytube.models.User;
import io.nightfrost.reactivemytube.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.util.Logger;
import reactor.util.Loggers;

import static org.springframework.web.reactive.function.BodyInserters.fromValue;

@Component
@RequiredArgsConstructor
public class UserHandler {

    private static final Logger LOGGER = Loggers.getLogger(UserHandler.class);

    private final UserService userService;

    public Mono<ServerResponse> getAll(ServerRequest request) {
        return userService.getUsers()
                .flatMap(this::mapEntityToServerResponse)
                .log(LOGGER);
    }

    public Mono<ServerResponse> getUserById(ServerRequest request) {
        String id = request.pathVariable("id");

        return userService.getUserById(id)
                .flatMap(this::mapEntityToServerResponse)
                .log(LOGGER);
    }

    public Mono<ServerResponse> createUser(ServerRequest request) {
        return request.bodyToMono(User.class)
                .flatMap(userService::saveUser)
                .flatMap(this::mapEntityToServerResponse)
                .log(LOGGER);
    }

    public Mono<ServerResponse> updateUser(ServerRequest request) {
        String id = request.pathVariable("id");
        return request.bodyToMono(User.class)
                .flatMap(user -> userService.updateUser(id, user))
                .flatMap(this::mapEntityToServerResponse)
                .log(LOGGER);
    }

    public Mono<ServerResponse> deleteUser(ServerRequest request) {
        String id = request.pathVariable("id");
        return userService.deleteUser(id)
                .flatMap(this::mapEntityToServerResponse)
                .log(LOGGER);
    }

    private Mono<ServerResponse> mapEntityToServerResponse(ResponseEntity<?> entity) {
        return ServerResponse.status(entity.getStatusCode()).body(fromValue(entity));
    }
}
