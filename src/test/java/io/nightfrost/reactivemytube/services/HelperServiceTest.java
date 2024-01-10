package io.nightfrost.reactivemytube.services;

import io.nightfrost.reactivemytube.models.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;

@ExtendWith(SpringExtension.class)
public class HelperServiceTest {

    @Test
    public void GivenTwoObjects_WhenUpdatingFields_ThenReturnPartiallyUpdatedObject() {
        User dbUser = User.builder().firstname("FirstName")
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

        User partialUser = User.builder().firstname(null)
                .lastname(null)
                .username(null)
                .password(null)
                .email("email2@email.dk")
                .phone(null)
                .dob(null)
                .nationality(null)
                .createdAt(null)
                .updatedAt(null)
                .enabled(true)
                .build();

        User updatedUser = (User) HelperService.partialUpdate(dbUser, partialUser);

        assert updatedUser.getFirstname().equals("FirstName");
        assert updatedUser.getLastname().equals("LastName");
        assert updatedUser.getUsername().equals("username");
        assert updatedUser.getPassword().equals("password");
        assert updatedUser.getEmail().equals("email2@email.dk");
        assert updatedUser.getPhone().equals("29282726");
        assert updatedUser.getNationality().equals("DK");
    }

}
