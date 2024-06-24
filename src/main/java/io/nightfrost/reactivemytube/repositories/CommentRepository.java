package io.nightfrost.reactivemytube.repositories;

import io.nightfrost.reactivemytube.models.Comment;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface CommentRepository extends ReactiveMongoRepository<Comment, String> {

    Flux<Comment> findAllByMovieId(String MovieId);

    Flux<Comment> findAllByUserId(String UserId);
}
