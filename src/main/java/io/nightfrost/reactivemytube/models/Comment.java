package io.nightfrost.reactivemytube.models;

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

    private String message;

    private String userId;

    private String movieId;

    private LocalDateTime createdDate;

    private LocalDateTime updatedDate;
}
