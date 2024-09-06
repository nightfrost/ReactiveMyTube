package io.nightfrost.reactivemytube.services;

import io.nightfrost.reactivemytube.dtos.MovieDTO;
import io.nightfrost.reactivemytube.models.Metadata;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

public interface MovieService {

    Mono<ResponseEntity<Map<String, String>>> putMovie(Mono<FilePart> fileParts, Metadata metadata);

    Flux<Void> getMovie(String id, ServerWebExchange exchange);

    Mono<ResponseEntity<List<MovieDTO>>> getAllAvailableMovies(ServerWebExchange exchange);

    Mono<ResponseEntity<Boolean>> existsMovie(String id);

    Mono<ResponseEntity<List<MovieDTO>>> queryMovies(String query, ServerWebExchange exchange);

    Mono<ResponseEntity<String>> deleteMovie(String id, ServerWebExchange exchange);
}
