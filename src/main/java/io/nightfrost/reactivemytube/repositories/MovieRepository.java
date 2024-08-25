package io.nightfrost.reactivemytube.repositories;

import com.mongodb.client.gridfs.model.GridFSFile;
import io.nightfrost.reactivemytube.models.Metadata;
import lombok.AllArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Meta;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.ReactiveGridFsResource;
import org.springframework.data.mongodb.gridfs.ReactiveGridFsTemplate;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@AllArgsConstructor
@Repository
public class MovieRepository {

    private final ReactiveGridFsTemplate reactiveGridFsTemplate;

    public Mono<ObjectId> store(Mono<FilePart> filePartMono, Metadata metadata) {
        return filePartMono.flatMap(part -> this.reactiveGridFsTemplate.store(part.content(), metadata.getName(), metadata)).log();
    }

    public Mono<ReactiveGridFsResource> getResource(String id) {
        return this.reactiveGridFsTemplate.findOne(query(where("_id").is(id)))
                .log()
                .flatMap(reactiveGridFsTemplate::getResource);
    }

    public Mono<List<GridFSFile>> findAll() {
        return this.reactiveGridFsTemplate.find(new Query())
                .log()
                .collectList();
    }

    public Mono<Boolean> exists(String id) {
        return this.reactiveGridFsTemplate.findOne(query(where("_id").is(id)))
                .log()
                .flatMap(movie -> Mono.just(true)).defaultIfEmpty(false);
    }
}
