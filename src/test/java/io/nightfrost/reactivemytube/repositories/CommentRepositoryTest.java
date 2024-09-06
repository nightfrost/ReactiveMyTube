package io.nightfrost.reactivemytube.repositories;

import io.nightfrost.reactivemytube.models.Comment;
import io.nightfrost.reactivemytube.services.HelperService;
import org.junit.jupiter.api.BeforeEach;
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

    private String commentId;

    private String movieId;

    private String userId;

    @BeforeEach
    public void initialize() {
        Comment comment = Comment.builder().message("Test Comment")
                .userId("65a13d11ac06cb22c029fded")
                .movieId("657b833078ac3e636e792174")
                .createdDate(LocalDateTime.now())
                .updatedDate(null)
                .build();

        Comment setup = commentRepository.deleteAll().thenMany(commentRepository.insert(comment)).blockFirst();

        assert setup != null;
        assert setup.getId() != null;
        assert setup.getMovieId() != null;
        assert setup.getUserId() != null;
        commentId = setup.getId();
        movieId = setup.getMovieId();
        userId = setup.getUserId();
    }

    //CREATE
    @Test
    public void GivenValidCommentData_WhenSave_ThenSuccess() {
        Comment comment = Comment.builder().message("Test Comment")
                .userId("65a13d11ac06cb22c029fded")
                .movieId("657b833078ac3e636e792174")
                .createdDate(LocalDateTime.now())
                .updatedDate(null)
                .build();

        Publisher<Comment> setup = commentRepository.deleteAll().thenMany(commentRepository.insert(comment));

        StepVerifier.create(setup)
                .expectNextCount(1)
                .verifyComplete();
    }

    //GET by movieId
    @Test
    public void GivenValidMovieId_WhenGetByMovieId_ThenSuccess() {
        Publisher<Comment> setup = commentRepository.findAllByMovieId(movieId);

        StepVerifier.create(setup).consumeNextWith(comment -> {
            assert comment != null;
            assert comment.getMovieId().equals(movieId);
            assert comment.getUserId().equals(userId);
            assert comment.getMessage().equals("Test Comment");
        }).verifyComplete();
    }

    //GET by userId
    @Test
    public void GivenValidUserId_WhenGetByUserId_ThenSuccess() {
        Publisher<Comment> setup = commentRepository.findAllByUserId(userId);

        StepVerifier.create(setup).consumeNextWith(comment -> {
            assert comment != null;
            assert comment.getMovieId().equals(movieId);
            assert comment.getUserId().equals(userId);
            assert comment.getMessage().equals("Test Comment");
        }).verifyComplete();
    }

    //UPDATE
    @Test
    public void GivenValidCommentData_WhenUpdateComment_ReturnSuccess() {
        Comment updatedComment = Comment.builder().message("Updated Test Comment")
                .build();

        Publisher<Comment> setup = commentRepository.findById(commentId)
                .flatMap(comment -> commentRepository.save((Comment) HelperService.partialUpdate(comment, updatedComment)))
                .thenMany(commentRepository.findById(commentId));

        StepVerifier.create(setup).consumeNextWith(comment -> {
            assert comment != null;
            assert comment.getMessage().equals(updatedComment.getMessage());
        }).verifyComplete();
    }

    //DELETE
    @Test
    public void GivenValidCommentId_WhenDeleteComment_ReturnSuccess() {
        Publisher<Comment> setup = commentRepository.deleteById(commentId).thenMany(commentRepository.findById(commentId));

        StepVerifier.create(setup).expectNextCount(0).verifyComplete();
    }
}
