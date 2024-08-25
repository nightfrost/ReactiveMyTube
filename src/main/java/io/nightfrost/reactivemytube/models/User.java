package io.nightfrost.reactivemytube.models;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Document
@Data
@Builder
public class User {

    @Id
    private String id;

    private String firstname;

    private String lastname;

    @NotNull
    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @NotBlank
    private String email;

    private String phone;

    private LocalDate dob;

    private String nationality;

    @NotNull
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private List<String> likedMovies;

    private boolean enabled;
}
