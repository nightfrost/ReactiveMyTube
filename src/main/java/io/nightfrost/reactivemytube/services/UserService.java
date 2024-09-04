package io.nightfrost.reactivemytube.services;

import io.nightfrost.reactivemytube.models.User;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface UserService {

    Mono<ResponseEntity<User>> getUserById(String id);

    Mono<ResponseEntity<List<User>>> getUsers();

    Mono<ResponseEntity<User>> saveUser(User newUser);

    Mono<ResponseEntity<User>> updateUser(String id, User updatedUser);

    Mono<ResponseEntity<String>> deleteUser(String id);
}
