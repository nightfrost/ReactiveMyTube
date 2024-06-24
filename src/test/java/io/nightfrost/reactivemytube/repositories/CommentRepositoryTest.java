package io.nightfrost.reactivemytube.repositories;

import io.nightfrost.reactivemytube.models.Comment;
import org.junit.BeforeClass;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;

@DataMongoTest
@ExtendWith(SpringExtension.class)
public class CommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;

    @Test
    public void GivenValidCommentData_WhenSave_ThenSuccess() {
        Comment comment = Comment.builder().message("Test Comment")
                .userId("65a13d11ac06cb22c029fded")
                .movieId("657b833078ac3e636e792174")
                .createdDate(LocalDateTime.now())
                .updatedDate(null)
                .build();

        Publisher<Comment> setup = commentRepository.deleteAll().thenMany(commentRepository.save(comment));


        StepVerifier.create(setup)
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    public void GivenValidMovieId_WhenGetByMovieId_ThenSuccess() {
        Comment comment = Comment.builder().message("Test Comment1")
                .userId("65a13d11ac06cb22c029fded")
                .movieId("657b833078ac3e636e792174")
                .createdDate(LocalDateTime.now())
                .updatedDate(null)
                .build();

        Comment commentTwo = Comment.builder().message("Test Comment2")
                .userId("65a13d11ac06cb22c029fded")
                .movieId("657b833078ac3e636e792174")
                .createdDate(LocalDateTime.now())
                .updatedDate(null)
                .build();

        Publisher<Comment> setup = commentRepository.deleteAll().thenMany(commentRepository.save(comment));
    }
}
