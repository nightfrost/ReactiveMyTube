package io.nightfrost.reactivemytube.services;

import com.mongodb.client.gridfs.model.GridFSFile;
import io.nightfrost.reactivemytube.dtos.MovieDTO;
import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.ReactiveGridFsTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@AllArgsConstructor
@Service
public class MovieServiceImpl implements MovieService{

    private final ReactiveGridFsTemplate reactiveGridFsTemplate;

    @Override
    public Mono<ResponseEntity> putMovie(Mono<FilePart> fileParts) {
        return fileParts.flatMap(part -> this.reactiveGridFsTemplate.store(part.content(), part.filename()))
                .map((id) -> ResponseEntity.ok().body(Map.of("id", id.toHexString())));
    }

    @Override

    public Flux<Void> getMovie(String id, ServerWebExchange exchange) {
        return this.reactiveGridFsTemplate.findOne(query(where("_id").is(id)))
                .log()
                .flatMap(reactiveGridFsTemplate::getResource)
                .flatMapMany(r -> exchange.getResponse().writeWith(r.getDownloadStream()));

    }

    @Override
    public Mono<List<MovieDTO>> getAllAvailableMovies(ServerWebExchange exchange) {
        List<MovieDTO> availableMovies = new ArrayList<>();

        return this.reactiveGridFsTemplate.find(new Query())
                .log()
                .collectList().flatMap(listOfFiles -> {
                    for (GridFSFile file : listOfFiles) {
                        availableMovies.add(new MovieDTO(file.getId().toString(), file.getFilename(), file.getUploadDate()));
                    }
                    return Mono.just(availableMovies);
                });
    }
}
