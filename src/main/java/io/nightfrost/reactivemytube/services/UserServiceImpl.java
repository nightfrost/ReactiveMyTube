package io.nightfrost.reactivemytube.services;

import io.nightfrost.reactivemytube.models.Roles;
import io.nightfrost.reactivemytube.models.User;
import io.nightfrost.reactivemytube.repositories.UserRepository;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;
import reactor.util.Logger;
import reactor.util.Loggers;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final Logger LOGGER = Loggers.getLogger(UserServiceImpl.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final PasswordValidatorService passwordValidatorService;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, PasswordValidatorService passwordValidatorService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.passwordValidatorService = passwordValidatorService;
    }

    @Override
    public Mono<ResponseEntity<User>> getUserById(String id) {
        return userRepository.findById(id)
                .flatMap(user -> Mono.just(ResponseEntity.ok(user)))
                .switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).build()))
                .onErrorResume(error -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()))
                .log(LOGGER);
    }

    @Override
    public Mono<ResponseEntity<List<User>>> getUsers() {
        return userRepository.findAll()
                .collectList()
                .flatMap(userList -> Mono.just(ResponseEntity.ok(userList)))
                .switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).build()))
                .onErrorResume(error -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()))
                .log(LOGGER);
    }

    @Override
    public Mono<ResponseEntity<User>> saveUser(User newUser) {
        validateCreateUserInput(newUser);
        passwordValidatorService.validatePassword(newUser.getPassword()).flatMap(isValid -> {
            //passwordValidator will throw exception given any validations fail.
            if (isValid) newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
            return Mono.just(true);
        });

        return userRepository.save(newUser)
                .flatMap(createdUser -> Mono.just(ResponseEntity.created(getToUri(createdUser)).body(createdUser)))
                .onErrorResume(error -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()))
                .log(LOGGER);
    }

    @Override
    public Mono<ResponseEntity<User>> updateUser(String id, User updatedUser) {
        return userRepository.findById(id)
                .flatMap(dbUser -> {
                    if (updatedUser.getPassword() != null) {
                        passwordValidatorService.validatePassword(updatedUser.getPassword());
                        updatedUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
                    }

                    User updatedDbUser = (User)HelperService.partialUpdate(dbUser, updatedUser);

                    return userRepository.save(updatedDbUser)
                            .flatMap(savedUser -> Mono.just(ResponseEntity.ok(savedUser)))
                            .onErrorResume(error -> Mono.just(ResponseEntity.internalServerError().build()));
        })
                .switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).build()))
                .onErrorResume(error -> Mono.just(ResponseEntity.internalServerError().build()))
                .log(LOGGER);
    }

    @Override
    public Mono<ResponseEntity<String>> deleteUser(String id) {
        return userRepository.findById(id)
                .flatMap(user -> userRepository.delete(user)
                        .then(Mono.just(ResponseEntity.ok("User deleted."))))
                .switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.")))
                .onErrorResume(error ->
                        Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body("An error occurred while deleting the user: " + error.getMessage()))
                ).log(LOGGER);
    }

    private URI getToUri(User userSaved) {
        if (userSaved == null) return UriComponentsBuilder.newInstance().build("No valid id.");

        return UriComponentsBuilder.fromPath(("/{id}"))
                .buildAndExpand(userSaved.getId()).toUri();
    }

    private User validateCreateUserInput(User newUser) {
        if (!newUser.isEnabled()) newUser.setEnabled(true);
        if (newUser.getCreatedAt() == null) newUser.setCreatedAt(LocalDateTime.now());
        if (newUser.getRoles() == null || newUser.getRoles().isEmpty()) newUser.setRoles(List.of(Roles.USER));
        return newUser;
    }
}
