package io.nightfrost.reactivemytube.services;

import com.mongodb.client.gridfs.model.GridFSFile;
import io.nightfrost.reactivemytube.dtos.MovieDTO;
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


@AllArgsConstructor
@Service
public class MovieServiceImpl implements MovieService{

    private final MovieRepository movieRepository;

    private final Logger logger = LoggerFactory.getLogger(MovieServiceImpl.class);

    @Override
    public Mono<ResponseEntity> putMovie(Mono<FilePart> fileParts) {
        return movieRepository.store(fileParts)
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
                availableMovies.add(new MovieDTO(file.getId().toString(), file.getFilename(), file.getUploadDate()));
            }
            return Mono.just(availableMovies);
        });
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
                    availableMovies.add(new MovieDTO(file.getId().toString(), file.getFilename(), file.getUploadDate()));
                }
            }
            return Mono.just(availableMovies);
        });
    }
}
