package io.nightfrost.reactivemytube.services;

import io.nightfrost.reactivemytube.exceptions.ResourceNotFoundException;
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
        return commentRepository.findById(id).switchIfEmpty(Mono.error(new ResourceNotFoundException("Comment not found with id: " + id)));
    }

    @Override
    public Flux<Comment> getCommentsByMovieId(String movieId) {
        return commentRepository.findAllByMovieId(movieId)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Found no comments for video with id: " + movieId)));
    }

    @Override
    public Flux<Comment> getCommentsByUserId(String userId) {
        return commentRepository.findAllByUserId(userId).switchIfEmpty(Mono.error(new ResourceNotFoundException("Found no comment for user with id: " + userId)));
    }

    @Override
    public Mono<Comment> saveComment(Comment newComment) {
        return commentRepository.save(newComment);
    }

    @Override
    public Mono<Comment> updateComment(String id, Comment updatedComment) {
        return commentRepository.findById(id).flatMap(comment -> {
            Comment temp = (Comment) HelperService.partialUpdate(comment, updatedComment);
            return commentRepository.save(temp);
        }).switchIfEmpty(Mono.error(new ResourceNotFoundException("Couldn't update comment with id: " + id)));
    }

    @Override
    public Mono<Void> deleteComment(String id) {
        return commentRepository.deleteById(id);
    }
}
