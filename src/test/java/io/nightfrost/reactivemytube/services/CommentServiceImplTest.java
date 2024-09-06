package io.nightfrost.reactivemytube.services;

import io.nightfrost.reactivemytube.ReactiveMyTubeApplication;
import io.nightfrost.reactivemytube.models.Comment;
import io.nightfrost.reactivemytube.repositories.CommentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = ReactiveMyTubeApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CommentServiceImplTest {

    @MockBean
    private CommentRepository commentRepository;

    @Autowired
    private WebTestClient client;

    private final String API_ROUTE = "api/v1/comments";

    @Test
    public void GivenCommentId_WhenRetrieveCommentInfo_Succeed() {
        Comment dbComment = Comment.builder()
                .id("658352d90cb2a36e461551ab")
                .message("I'm a comment!")
                .createdDate(LocalDateTime.now())
                .movieId("658352d90cb2a36e461551ab")
                .userId("658352d90cb2a36e461551ab")
                .build();

        when(commentRepository.findById("658352d90cb2a36e461551ab"))
                .thenReturn(Mono.just(dbComment));

        client.get().uri(API_ROUTE + "/" + dbComment.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody(Comment.class);
    }

    @Test
    public void GivenMovieId_WhenRetrieveCommentInfoByMovieId_Succeed() {
        Comment dbComment = Comment.builder()
                .id("658352d90cb2a36e461551aa")
                .message("I'm a comment!")
                .createdDate(LocalDateTime.now())
                .movieId("658352d90cb2a36e461551ab")
                .userId("658352d90cb2a36e461551ab")
                .build();

        Comment dbCommentTwo = Comment.builder()
                .id("658352d90cb2a36e461551ab")
                .message("I'm a comment!")
                .createdDate(LocalDateTime.now())
                .movieId("658352d90cb2a36e461551ab")
                .userId("658352d90cb2a36e461551ab")
                .build();

        List<Comment> listOfComments = Arrays.asList(dbComment, dbCommentTwo);

        when(commentRepository.findAllByMovieId("658352d90cb2a36e461551ab"))
                .thenReturn(Flux.fromIterable(listOfComments));

        client.get().uri(API_ROUTE + "/" + "movie" + "/" + dbComment.getMovieId())
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(Comment.class);
    }

    @Test
    public void GivenUserId_WhenRetrieveCommentInfoByUserId_Succeed() {
        Comment dbComment = Comment.builder()
                .id("658352d90cb2a36e461551aa")
                .message("I'm a comment!")
                .createdDate(LocalDateTime.now())
                .movieId("658352d90cb2a36e461551ab")
                .userId("658352d90cb2a36e461551ab")
                .build();

        Comment dbCommentTwo = Comment.builder()
                .id("658352d90cb2a36e461551ab")
                .message("I'm a comment!")
                .createdDate(LocalDateTime.now())
                .movieId("658352d90cb2a36e461551ab")
                .userId("658352d90cb2a36e461551ab")
                .build();

        List<Comment> listOfComments = Arrays.asList(dbComment, dbCommentTwo);

        when(commentRepository.findAllByUserId("658352d90cb2a36e461551ab"))
                .thenReturn(Flux.fromIterable(listOfComments));

        client.get().uri(API_ROUTE + "/" + "user" + "/" + dbComment.getUserId())
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(Comment.class);
    }

    @Test
    public void GivenCorrectCommentData_WhenSaveComment_Succeed() {
        Comment dbComment = Comment.builder()
                .id("658352d90cb2a36e461551aa")
                .message("I'm a comment!")
                .createdDate(LocalDateTime.now())
                .movieId("658352d90cb2a36e461551ab")
                .userId("658352d90cb2a36e461551ab")
                .build();

        when(commentRepository.save(dbComment))
                .thenReturn(Mono.just(dbComment));

        client.post().uri(API_ROUTE).bodyValue(dbComment).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus().isCreated();
    }

    @Test
    public void GivenCommentId_WhenUpdateComment_Succeed() {
        Comment dbComment = Comment.builder()
                .id("658352d90cb2a36e461551aa")
                .message("I'm a comment!")
                .createdDate(LocalDateTime.now())
                .movieId("658352d90cb2a36e461551ab")
                .userId("658352d90cb2a36e461551ab")
                .build();

        Comment dbCommentUpdated = Comment.builder()
                .id("658352d90cb2a36e461551aa")
                .message("I'm an updated comment!")
                .createdDate(LocalDateTime.now())
                .movieId("658352d90cb2a36e461551ab")
                .userId("658352d90cb2a36e461551ab")
                .build();

        when(commentRepository.findById(dbComment.getId())).thenReturn(Mono.just(dbComment));

        when(commentRepository.save(dbCommentUpdated))
                .thenReturn(Mono.just(dbCommentUpdated));

        client.put().uri(API_ROUTE + "/" + dbComment.getId()).bodyValue(dbCommentUpdated).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Comment.class).isEqualTo(dbCommentUpdated);
    }

    @Test
    public void GivenCommentId_WhenDeleteComment_Succeed() {
        when(commentRepository.deleteById("123")).thenReturn(Mono.empty());

        client.delete().uri(API_ROUTE + "/" + "123").exchange().expectStatus().isNoContent();
    }
}
