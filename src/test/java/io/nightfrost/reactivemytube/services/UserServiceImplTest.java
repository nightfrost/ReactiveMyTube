package io.nightfrost.reactivemytube.services;

import io.nightfrost.reactivemytube.ReactiveMyTubeApplication;
import io.nightfrost.reactivemytube.models.User;
import io.nightfrost.reactivemytube.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = ReactiveMyTubeApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserServiceImplTest {

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private WebTestClient client;

    private final String API_ROUTE = "api/v1/users";

    @Test
    public void GivenUserID_WhenRetrieveUserInfo_Succeed() {
        User dbUser = User.builder().id("658352d90cb2a36e461551ab")
                .firstname("FirstName")
                .lastname("LastName")
                .username("username")
                .password("password")
                .email("test@email.dk")
                .phone("29282726")
                .dob(LocalDate.now())
                .nationality("DK")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .enabled(true)
                .build();

        when(userRepository.findById("658352d90cb2a36e461551ab"))
                .thenReturn(Mono.just(dbUser));

        client.get().uri(API_ROUTE + "/" + dbUser.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody(User.class)
                .isEqualTo(dbUser);
    }

    @Test
    public void GivenNothing_WhenGetAllUsers_ReturnSuccessWithUsers() {
        User dbUser = User.builder().id("658352d90cb2a36e461551ab")
                .firstname("FirstName")
                .lastname("LastName")
                .username("username")
                .password("password")
                .email("test@email.dk")
                .phone("29282726")
                .dob(LocalDate.now())
                .nationality("DK")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .enabled(true)
                .build();

        User dbUserTwo = User.builder().id("658352d90cb2a36e461551ac")
                .firstname("FirstNameTwo")
                .lastname("LastNameTwo")
                .username("usernameTwo")
                .password("passwordTwo")
                .email("testTwo@email.dk")
                .phone("29282726Two")
                .dob(LocalDate.now())
                .nationality("DK")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .enabled(true)
                .build();

        List<User> listOfDbUsers = Arrays.asList(dbUser, dbUserTwo);

        when(userRepository.findAll())
                .thenReturn(Flux.fromIterable(listOfDbUsers));

        client.get().uri(API_ROUTE)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(User.class)
                .isEqualTo(listOfDbUsers);
    }


    @Test
    public void GivenNewUser_WhenStoreUser_ReturnSuccessAndUser() {
        User dbUser = User.builder().id("658352d90cb2a36e461551ab")
                .firstname("FirstName")
                .lastname("LastName")
                .username("username")
                .password("password")
                .email("test@email.dk")
                .phone("29282726")
                .dob(LocalDate.now())
                .nationality("DK")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .enabled(true)
                .build();

        when(userRepository.save(dbUser))
                .thenReturn(Mono.just(dbUser));

        client.post().uri(API_ROUTE)
                .bodyValue(dbUser)
                .header("Content-Type", "application/json")
                .exchange()
                .expectStatus().isCreated()
                .expectBody(User.class)
                .isEqualTo(dbUser);
    }

    @Test
    public void GivenUpdatedUser_WhenUpdateUser_ReturnSuccessAndUpdatedUser() {
        User updatedUser = User.builder().id("658352d90cb2a36e461551ab")
                .firstname("FirstName")
                .lastname("LastName")
                .username("username")
                .password("password")
                .email("test@email.dk")
                .phone("29282726")
                .dob(LocalDate.now())
                .nationality("DK")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .enabled(true)
                .build();

        when(userRepository.findById("658352d90cb2a36e461551ab"))
                .thenReturn(Mono.just(updatedUser));

        when(userRepository.save(updatedUser))
                .thenReturn(Mono.just(updatedUser));

        client.put().uri(API_ROUTE + "/" + updatedUser.getId())
                .bodyValue(updatedUser)
                .header("Content-Type", "application/json")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(User.class)
                .isEqualTo(updatedUser);
    }

    @Test
    public void GivenUserID_WhenDeleteUserByID_ReturnSuccessAndNoContent() {
        String userId = "658352d90cb2a36e461551ab";

        when(userRepository.deleteById("658352d90cb2a36e461551ab"))
                .thenReturn(Mono.empty());

        client.delete().uri(API_ROUTE + "/" + userId)
                .exchange()
                .expectStatus().isNoContent();
    }
}
