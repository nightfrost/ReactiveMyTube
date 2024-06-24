package io.nightfrost.reactivemytube.services;

import io.nightfrost.reactivemytube.models.Comment;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CommentService {

    Mono<Comment> getCommentById(String id);

    Flux<Comment> getCommentsByMovieId(String movieId);

    Flux<Comment> getCommentsByUserId(String userId);

    Mono<Comment> saveComment(Comment newComment);

    Mono<Comment> updateComment(String id, Comment updatedComment);

    Mono<Void> deleteComment(String id);
}
