package io.nightfrost.reactivemytube.services;

import io.nightfrost.reactivemytube.exceptions.ResourceNotFoundException;
import io.nightfrost.reactivemytube.models.User;
import io.nightfrost.reactivemytube.repositories.UserRepository;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ReactiveMongoTemplate template;

    public UserServiceImpl(UserRepository userRepository, ReactiveMongoTemplate template) {
        this.userRepository = userRepository;
        this.template = template;
    }

    @Override
    public Mono<User> getUserById(String id) {
        return userRepository.findById(id).switchIfEmpty(Mono.error(new ResourceNotFoundException("User not found with id: " + id)));
    }

    @Override
    public Flux<User> getUsers() {
        return userRepository.findAll().switchIfEmpty(Mono.error(new ResourceNotFoundException("No users found.")));
    }

    @Override
    public Mono<User> saveUser(User newUser) {
        return userRepository.save(newUser).switchIfEmpty(Mono.error(new RuntimeException("New user save failed.")));
    }

    @Override
    public Mono<User> updateUser(String id, User updatedUser) {
        return userRepository.findById(id).flatMap(user -> {
            User temp = (User) HelperService.partialUpdate(user, updatedUser);
            return userRepository.save(temp);
        }).switchIfEmpty(Mono.error(new ResourceNotFoundException("Couldn't update user with id: " + id)));
    }

    @Override
    public Mono<Void> deleteUser(String id) {
        return userRepository.deleteById(id);
    }

}
