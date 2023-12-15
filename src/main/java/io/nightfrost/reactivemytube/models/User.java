package io.nightfrost.reactivemytube.models;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.sql.Timestamp;
import java.util.Date;

@Document
@Getter
@Setter
@NoArgsConstructor
public class User {

    @Id
    private String id;

    private String firstname;

    private String lastname;
    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @NotBlank
    private String email;

    private String phone;

    private Date dob;

    private String nationality;

    private Timestamp createdAt;

    private Timestamp updatedAt;

    private boolean enabled;
}
