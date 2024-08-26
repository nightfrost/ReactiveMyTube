package io.nightfrost.reactivemytube.services;

import com.mongodb.Tag;
import com.mongodb.client.gridfs.model.GridFSFile;
import io.nightfrost.reactivemytube.dtos.MovieDTO;
import io.nightfrost.reactivemytube.exceptions.ResourceNotFoundException;
import io.nightfrost.reactivemytube.models.Metadata;
import io.nightfrost.reactivemytube.models.Tags;
import io.nightfrost.reactivemytube.repositories.MovieRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@AllArgsConstructor
@Service
public class MovieServiceImpl implements MovieService{

    private final MovieRepository movieRepository;

    private final Logger logger = LoggerFactory.getLogger(MovieServiceImpl.class);

    @Override
    public Mono<ResponseEntity> putMovie(Mono<FilePart> fileParts, Metadata metadata) {
        return movieRepository.store(fileParts, metadata).doOnSuccess((id) -> logger.info("Successfully stored movie with given ID: " + id))
                .doOnError((throwable) -> logger.error("Failed to store movie.", throwable))
                .map((id) -> ResponseEntity.ok().body(Map.of("id", id.toHexString())));
    }

    @Override
    public Flux<Void> getMovie(String id, ServerWebExchange exchange) {
        return movieRepository.getResource(id)
                .flatMapMany(resource -> exchange.getResponse().writeWith(resource.getDownloadStream()));
    }

    @Override
    public Mono<List<MovieDTO>> getAllAvailableMovies(ServerWebExchange exchange) {
        List<MovieDTO> availableMovies = new ArrayList<>();

        return movieRepository.findAll().flatMap(movieList -> {
            for (GridFSFile file : movieList) {
                mapMovieToMovieDTO(availableMovies, file);
            }
            return Mono.just(availableMovies);
        }).switchIfEmpty(Mono.error(new ResourceNotFoundException("No movies found.")));
    }

    @Override
    public Mono<Boolean> existsMovie(String id) {
        return movieRepository.exists(id);
    }

    @Override
    public Mono<List<MovieDTO>> queryMovies(String query, ServerWebExchange exchange) {
        List<MovieDTO> availableMovies = new ArrayList<>();

        return movieRepository.findAll().flatMap(movieList -> {
            for (GridFSFile file : movieList) {
                if (file.getFilename().toLowerCase().contains(query.toLowerCase())) {
                    mapMovieToMovieDTO(availableMovies, file);
                }
            }
            return Mono.just(availableMovies);
        }).switchIfEmpty(Mono.error(new ResourceNotFoundException("Found no movies with query: " + query)));
    }

    private void mapMovieToMovieDTO(List<MovieDTO> availableMovies, GridFSFile file) {
        //If metadata not provided at creation, fill with data available.
        if (file.getMetadata() != null) {
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
            availableMovies.add(MovieDTO.builder()
                    ._id(file.getId().toString()).filename(file.getFilename()).uploadDate(file.getUploadDate()).build());
        }
    }
}
