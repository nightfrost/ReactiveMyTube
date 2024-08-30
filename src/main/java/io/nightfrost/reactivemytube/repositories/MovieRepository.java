package io.nightfrost.reactivemytube.repositories;

import com.mongodb.client.gridfs.model.GridFSFile;
import io.nightfrost.reactivemytube.models.Metadata;
import io.nightfrost.reactivemytube.services.MovieServiceImpl;
import lombok.AllArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.ReactiveGridFsResource;
import org.springframework.data.mongodb.gridfs.ReactiveGridFsTemplate;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import reactor.util.Logger;
import reactor.util.Loggers;

import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@AllArgsConstructor
@Repository
public class MovieRepository {

    private final ReactiveGridFsTemplate reactiveGridFsTemplate;

    private final Logger LOGGER = Loggers.getLogger(MovieRepository.class);

    public Mono<ObjectId> store(Mono<FilePart> filePartMono, Metadata metadata) {
        return filePartMono.flatMap(part -> this.reactiveGridFsTemplate.store(part.content(), metadata.getName(), metadata)).log(LOGGER);
    }

    public Mono<ReactiveGridFsResource> getResource(String id) {
        return this.reactiveGridFsTemplate.findOne(query(where("_id").is(id)))
                .log(LOGGER)
                .flatMap(reactiveGridFsTemplate::getResource);
    }

    public Mono<List<GridFSFile>> findAll() {
        return this.reactiveGridFsTemplate.find(new Query())
                .log(LOGGER)
                .collectList();
    }

    public Mono<Boolean> exists(String id) {
        return this.reactiveGridFsTemplate.findOne(query(where("_id").is(id)))
                .log(LOGGER)
                .flatMap(movie -> Mono.just(true)).defaultIfEmpty(false);
    }

    public Mono<Boolean> delete(String id) {
        return this.reactiveGridFsTemplate.delete(query(where("_id").is(id)))
                .then(Mono.just(true))
                .doOnSuccess(success -> LOGGER.info("Successfully deleted object with id {}", id))
                .onErrorResume(error -> {
                    LOGGER.error("Error deleting object with id {}: {}", id, error.getMessage());
                    return Mono.just(false);
                });
    }
}
