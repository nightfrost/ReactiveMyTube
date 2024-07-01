package io.nightfrost.reactivemytube.services;

import io.nightfrost.reactivemytube.models.Comment;
import io.nightfrost.reactivemytube.repositories.CommentRepository;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    public CommentServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    public Mono<Comment> getCommentById(String id) {
        return commentRepository.findById(id);
    }

    @Override
    public Flux<Comment> getCommentsByMovieId(String movieId) {
        return commentRepository.findAllByMovieId(movieId);
    }

    @Override
    public Flux<Comment> getCommentsByUserId(String userId) {
        return commentRepository.findAllByUserId(userId);
    }

    @Override
    public Mono<Comment> saveComment(Comment newComment) {

        if(ObjectId.isValid(newComment.getMovieId())) {
            boolean movieExists;

        }
        return commentRepository.save(newComment);
    }

    @Override
    public Mono<Comment> updateComment(String id, Comment updatedComment) {
        return commentRepository.findById(id).flatMap(comment -> {
            Comment temp = (Comment) HelperService.partialUpdate(comment, updatedComment);
            return commentRepository.save(temp);
        });
    }

    @Override
    public Mono<Void> deleteComment(String id) {
        return commentRepository.deleteById(id);
    }
}
