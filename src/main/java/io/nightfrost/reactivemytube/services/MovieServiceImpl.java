package io.nightfrost.reactivemytube.services;

import com.mongodb.client.gridfs.model.GridFSFile;
import io.nightfrost.reactivemytube.dtos.MovieDTO;
import io.nightfrost.reactivemytube.models.Metadata;
import io.nightfrost.reactivemytube.models.Tags;
import io.nightfrost.reactivemytube.repositories.MovieRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.Logger;
import reactor.util.Loggers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@AllArgsConstructor
@Service
public class MovieServiceImpl implements MovieService{

    private final MovieRepository movieRepository;

    private final Logger LOGGER = Loggers.getLogger(MovieServiceImpl.class);

    @Override
    public Mono<ResponseEntity<Map<String, String>>> putMovie(Mono<FilePart> fileParts, Metadata metadata) {
        return movieRepository.store(fileParts, metadata).doOnSuccess((id) -> LOGGER.info("Successfully stored movie with given ID: " + id))
                .doOnError((throwable) -> LOGGER.error("Failed to store movie.", throwable))
                .map((id) -> ResponseEntity.ok().body(Map.of("id", id.toHexString())));
    }

    @Override
    public Flux<Void> getMovie(String id, ServerWebExchange exchange) {
        return movieRepository.getResource(id)
                .flatMapMany(resource -> exchange.getResponse().writeWith(resource.getDownloadStream())).doOnError(Flux::error);
    }

    @Override
    public Mono<ResponseEntity<List<MovieDTO>>> getAllAvailableMovies(ServerWebExchange exchange) {
        List<MovieDTO> availableMovies = new ArrayList<>();

        return movieRepository.findAll().flatMap(movieList -> {
            for (GridFSFile file : movieList) {
                mapMovieToMovieDTO(availableMovies, file);
            }
            return Mono.just(ResponseEntity.ok(availableMovies));
        }).switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.NO_CONTENT).build()))
                .onErrorResume(Mono::error);
    }

    @Override
    public Mono<ResponseEntity<Boolean>> existsMovie(String id) {
        return movieRepository.exists(id).map(result -> result ? ResponseEntity.ok(result) : ResponseEntity.notFound().build());
    }

    @Override
    public Mono<ResponseEntity<List<MovieDTO>>> queryMovies(String query, ServerWebExchange exchange) {
        List<MovieDTO> availableMovies = new ArrayList<>();

        return movieRepository.findAll().flatMap(movieList -> {
            for (GridFSFile file : movieList) {
                if (file.getFilename().toLowerCase().contains(query.toLowerCase())) {
                    mapMovieToMovieDTO(availableMovies, file);
                }
            }
            return Mono.just(ResponseEntity.ok(availableMovies));
        }).switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.NO_CONTENT).build()))
                .onErrorResume(Mono::error);
    }

    @Override
    public Mono<ResponseEntity<String>> deleteMovie(String id, ServerWebExchange exchange) {
        return movieRepository.delete(id).doOnSuccess(success -> LOGGER.info("Movie has been deleted.")).doOnError(error -> LOGGER.error("Failed to delete movie.")).map(status -> {
            if (status) {
                return ResponseEntity.ok().body("Deleted movie with id: " + id);
            } else {
                return ResponseEntity.notFound().build();
            }
        });
    }

    private void mapMovieToMovieDTO(List<MovieDTO> availableMovies, GridFSFile file) {
        if (file.getMetadata() != null && file.getMetadata().containsKey("posterUrl") && file.getMetadata().containsKey("tags")) {
            ArrayList<Tags> tags = file.getMetadata()
                    .getList("tags", String.class)
                    .stream()
                    .map(Tags::valueOf)
                    .collect(Collectors.toCollection(ArrayList::new));

            availableMovies.add(MovieDTO.builder()
                    ._id(file.getId().toString())
                    .filename(file.getFilename())
                    .tags(tags)
                    .posterUrl(file.getMetadata().getString("posterUrl"))
                    .build());
        } else {
            LOGGER.warn("No Metadata at creation, filling with available data");
            availableMovies.add(MovieDTO.builder()
                    ._id(file.getId().toString()).filename(file.getFilename()).uploadDate(file.getUploadDate()).build());
        }
    }
}
