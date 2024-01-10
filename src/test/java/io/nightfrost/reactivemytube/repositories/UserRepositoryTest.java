package io.nightfrost.reactivemytube.repositories;

import io.nightfrost.reactivemytube.models.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.time.LocalDateTime;


@DataMongoTest
@ExtendWith(SpringExtension.class)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void GivenValidUserData_WhenSave_ThenSuccess() {
        User user = User.builder().firstname("FirstName")
                .lastname("LastName")
                .username("username")
                .password("password")
                .email("email@email.dk")
                .phone("29282726")
                .dob(LocalDate.now())
                .nationality("DK")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .enabled(true)
                .build();

        Publisher<User> setup = userRepository.deleteAll().thenMany(userRepository.save(user));
        StepVerifier.create(setup)
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    public void GivenCorrectUserId_WhenGetById_ThenSuccess() {
        final String TEST_USERNAME = "usernameTest";
        User user = User.builder().firstname("FirstName")
                .lastname("LastName")
                .username(TEST_USERNAME)
                .password("password")
                .email("email@email.dk")
                .phone("29282726")
                .dob(LocalDate.now())
                .nationality("DK")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .enabled(true)
                .build();

        Publisher<User> setup = userRepository.deleteAll().then(userRepository.save(user));
        Mono<User> find = userRepository.findByUsername(TEST_USERNAME);

        Publisher<User> composite = Mono
                .from(setup)
                .then(find);

        StepVerifier
                .create(composite)
                .consumeNextWith(account -> {
                    assert account.getId() != null;
                    assert account.getUsername().equals(TEST_USERNAME);
                    assert account.isEnabled();
                    assert account.getEmail().equals("email@email.dk");
                }).verifyComplete();
    }

}
