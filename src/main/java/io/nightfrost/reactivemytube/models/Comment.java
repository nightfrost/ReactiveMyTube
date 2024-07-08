package io.nightfrost.reactivemytube.models;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document
@Data
@Builder
public class Comment {

    @Id
    private String id;

    @NotBlank
    @Max(value = 256)
    private String message;

    @NotBlank
    private String userId;

    @NotBlank
    private String movieId;

    private LocalDateTime createdDate;

    private LocalDateTime updatedDate;
}
