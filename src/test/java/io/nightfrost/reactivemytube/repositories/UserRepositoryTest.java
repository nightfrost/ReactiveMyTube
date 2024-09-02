package io.nightfrost.reactivemytube.repositories;

import io.nightfrost.reactivemytube.models.User;
import io.nightfrost.reactivemytube.services.HelperService;
import org.junit.jupiter.api.BeforeEach;
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

    private String userId;

    private String username;

    @BeforeEach
    public void initialize() {
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

        User test_user = userRepository.deleteAll().thenMany(userRepository.save(user)).blockFirst();
        assert test_user != null;
        assert test_user.getId() != null && !test_user.getId().isBlank();
        assert test_user.getUsername() != null && !test_user.getUsername().isBlank();
        userId = test_user.getId();
        username = test_user.getUsername();
    }

    //CREATE
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

    //GET by id
    @Test
    public void GivenCorrectUserId_WhenGetById_ThenSuccess() {
        Publisher<User> setup = userRepository.findById(userId);

        Publisher<User> composite = Mono
                .from(setup);

        StepVerifier
                .create(composite)
                .consumeNextWith(account -> {
                    assert account.getId() != null;
                    assert account.getUsername().equals(username);
                    assert account.isEnabled();
                    assert account.getEmail().equals("email@email.dk");
                }).verifyComplete();
    }

    //GET by username
    @Test
    public void GivenCorrectUsername_WhenGetByUsername_ReturnSuccess() {
        Publisher<User> setup = userRepository.findByUsername(username);

        Publisher<User> composite = Mono
                .from(setup);

        StepVerifier
                .create(composite)
                .consumeNextWith(account -> {
                    assert account.getId().equalsIgnoreCase(userId);
                    assert account.getUsername().equals(username);
                    assert account.isEnabled();
                    assert account.getEmail().equals("email@email.dk");
                }).verifyComplete();
    }

    //UPDATE
    @Test
    public void GivenCorrectUserData_WhenUpdateUser_ReturnSuccess() {
        User updatedUser = User.builder().firstname("UpdatedFirstName")
                .lastname("UpdatedLastName")
                .username("updatedUsername")
                .password("Updatedpassword")
                .email("email@email.dk")
                .phone("29282726")
                .dob(LocalDate.now())
                .nationality("DK")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .enabled(true)
                .build();

        Publisher<User> setup = userRepository.findById(userId)
                .flatMap(user -> userRepository.save((User) HelperService.partialUpdate(user, updatedUser)))
                .thenMany(userRepository.findById(userId));

        Publisher<User> composite = Mono.from(setup);

        StepVerifier.create(composite).consumeNextWith(user -> {
            assert user.getUsername().equals(updatedUser.getUsername());
            assert user.getFirstname().equals(updatedUser.getFirstname());
            assert user.getLastname().equals(updatedUser.getLastname());
            assert user.getPassword().equals(updatedUser.getPassword());
        }).verifyComplete();
    }

    //DELETE
    @Test
    public void GivenCorrectUserId_WhenDeleteUser_ReturnSuccess() {
        Publisher<User> setup = userRepository.findById(userId)
                .flatMap(user -> userRepository.delete(user))
                .thenMany(userRepository.findById(userId));

        Publisher<User> composite = Mono.from(setup);

        StepVerifier.create(composite).expectNextCount(0).verifyComplete();
    }
}
