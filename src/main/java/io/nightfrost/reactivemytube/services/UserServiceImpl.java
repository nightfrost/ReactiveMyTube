package io.nightfrost.reactivemytube.services;

import io.nightfrost.reactivemytube.exceptions.ResourceNotFoundException;
import io.nightfrost.reactivemytube.models.User;
import io.nightfrost.reactivemytube.repositories.UserRepository;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ReactiveMongoTemplate template;

    public UserServiceImpl(UserRepository userRepository, ReactiveMongoTemplate template) {
        this.userRepository = userRepository;
        this.template = template;
    }

    @Override
    public Mono<ResponseEntity<User>> getUserById(String id) {
        return userRepository.findById(id)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("User not found with id: " + id)));
    }

    @Override
    public Mono<ResponseEntity<List<User>>> getUsers() {
        return userRepository.findAll()
                .collectList()
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("No users found.")));
    }

    @Override
    public Mono<ResponseEntity<User>> saveUser(User newUser) {
        return userRepository.save(newUser)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.error(new RuntimeException("New user save failed.")));
    }

    @Override
    public Mono<ResponseEntity<User>> updateUser(String id, User updatedUser) {
        return userRepository.findById(id).flatMap(user -> {
            User temp = (User) HelperService.partialUpdate(user, updatedUser);
            return userRepository.save(temp)
                    .map(ResponseEntity::ok);
        }).switchIfEmpty(Mono.error(new ResourceNotFoundException("Couldn't update user with id: " + id)));
    }

    @Override
    public Mono<ResponseEntity<Boolean>> deleteUser(String id) {
        return userRepository.deleteById(id).then(userRepository.findById(id)).map(Objects::isNull).map(ResponseEntity::ok);
    }

}
