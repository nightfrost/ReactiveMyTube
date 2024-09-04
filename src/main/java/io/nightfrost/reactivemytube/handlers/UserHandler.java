package io.nightfrost.reactivemytube.handlers;

import io.nightfrost.reactivemytube.models.User;
import io.nightfrost.reactivemytube.services.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;

import static org.springframework.web.reactive.function.BodyInserters.fromValue;

@Component
@RequiredArgsConstructor
public class UserHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserHandler.class);

    private final UserService userService;

    public Mono<ServerResponse> getAll(ServerRequest request) {
        return userService.getUsers().flatMap(users -> {
            if (users.getBody() != null && users.getBody().isEmpty()) {
                LOGGER.error("Found no users");
                return ServerResponse.noContent().build();
            }
            LOGGER.info("Found {} users", users.getBody().size());
            return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                    .body(fromValue(users));
        });
    }

    public Mono<ServerResponse> getUserById(ServerRequest request) {
        String id = request.pathVariable("id");

        return userService.getUserById(id)
                .doOnSuccess(user -> LOGGER.info("User with id {} retrieved", id))
                .doOnError(exception -> LOGGER.error("User with id: " + id  +" not found", exception))
                .flatMap(user -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(fromValue(user)))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> createUser(ServerRequest request) {
        return request.bodyToMono(User.class)
                .flatMap(userService::saveUser)
                .doOnSuccess(userSaved -> LOGGER.info("User saved with id: {}", userSaved.getBody().getId()))
                .doOnError(e -> LOGGER.error("Error in save user method", e))
                .flatMap(user -> ServerResponse.created(getToUri(user.getBody())).bodyValue(user));
    }

    public Mono<ServerResponse> updateUser(ServerRequest request) {
        String id = request.pathVariable("id");
        return request.bodyToMono(User.class)
                .flatMap(user -> userService.updateUser(id, user))
                .doOnSuccess(userSaved -> LOGGER.info("User with id {} updated", id))
                .doOnError(e -> LOGGER.error("Save failed.. Log more...", e))
                .flatMap(user -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                        .body(fromValue(user))).switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> deleteUser(ServerRequest request) {
        String id = request.pathVariable("id");
        return userService.deleteUser(id)
                .doOnSuccess(voidThing -> LOGGER.info("User with id {} deleted", id))
                .doOnError(e -> LOGGER.error("Failed to delete user with ID {}", id, e))
                .then(ServerResponse.noContent().build())
                .switchIfEmpty(ServerResponse.notFound().build());
    }


    private URI getToUri(User userSaved) {
        return UriComponentsBuilder.fromPath(("/{id}"))
                .buildAndExpand(userSaved.getId()).toUri();
    }
}
