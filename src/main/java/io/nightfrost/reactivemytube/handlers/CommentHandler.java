package io.nightfrost.reactivemytube.handlers;

import io.nightfrost.reactivemytube.models.Comment;
import io.nightfrost.reactivemytube.services.CommentService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;

import static org.springframework.web.reactive.function.BodyInserters.fromValue;

@Component
@RequiredArgsConstructor
public class CommentHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommentHandler.class);

    private final CommentService commentService;

    //TODO: Should use paging.
    public Mono<ServerResponse> getAllByMovieId(ServerRequest request) {
        String id = request.pathVariable("id");

        return commentService.getCommentsByMovieId(id).collectList().flatMap(comments -> {
            if (comments.isEmpty()) {
                LOGGER.error("Found no comments");
                return ServerResponse.noContent().build();
            } else {
                LOGGER.info("Found {} comments", comments.size());
                return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                        .body(fromValue(comments));
            }
        });
    }

    //TODO: Should use paging
    public Mono<ServerResponse> getAllByUserId(ServerRequest request) {
        String id = request.pathVariable("id");

        return commentService.getCommentsByUserId(id).collectList().flatMap(comments -> {
            if (comments.isEmpty()) {
                LOGGER.error("Found no comments belonging to user {}", id);
                return ServerResponse.noContent().build();
            } else {
                LOGGER.info("Found {} comments belonging to user "+id, comments.size());
                return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                        .body(fromValue(comments));
            }
        });
    }

    public Mono<ServerResponse> getCommentById(ServerRequest request) {
        String id = request.pathVariable("id");

        return commentService.getCommentById(id)
                .flatMap(comment -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(fromValue(comment)))
                .switchIfEmpty(ServerResponse.noContent().build());
    }

    public Mono<ServerResponse> createComment(ServerRequest request) {
        return request.bodyToMono(Comment.class)
                .flatMap(commentService::saveComment)
                .doOnSuccess(commentSaved -> LOGGER.info("Comment saved with id: {}", commentSaved.getId()))
                .doOnError(e -> LOGGER.error("Save failed.. Log Error", e))
                .flatMap(comment -> ServerResponse.created(getToUri(comment)).bodyValue(comment));
    }

    public Mono<ServerResponse> updateComment(ServerRequest request) {
        String id = request.pathVariable("id");

        return request.bodyToMono(Comment.class)
                .flatMap(comment -> commentService.updateComment(id, comment))
                .doOnSuccess(comment -> LOGGER.info("Comment updated with id {}", id))
                .doOnError(e -> LOGGER.error("Update failed.. Log error.. ", e))
                .flatMap(comment -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                        .body(fromValue(comment))).switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> deleteComment(ServerRequest request) {
        String id = request.pathVariable("id");

        return request.bodyToMono(Comment.class)
                .flatMap(comment -> commentService.deleteComment(id))
                .doOnSuccess(comment -> LOGGER.info("Comment with id {} deleted", id))
                .doOnError(e -> LOGGER.error("Delete failed.. Log error..", e))
                .then(ServerResponse.noContent().build())
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    private URI getToUri(Comment commentSaved) {
        return UriComponentsBuilder.fromPath(("/{id}"))
                .buildAndExpand(commentSaved.getId()).toUri();
    }
}
