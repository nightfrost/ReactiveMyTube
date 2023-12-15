package io.nightfrost.reactivemytube.services;

import io.nightfrost.reactivemytube.models.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserService {

    Mono<User> getUserById(String id);

    Flux<User> getUsers();

    Mono<User> saveUser(User newUser);

    Mono<User> updateUser(String id, User updatedUser);

    Mono<Void> deleteUser(String id);
}
