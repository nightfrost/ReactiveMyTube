package io.nightfrost.reactivemytube.services;

import io.nightfrost.reactivemytube.dtos.MovieDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface MovieService {

    Mono<ResponseEntity> putMovie(Mono<FilePart> fileParts);

    Flux<Void> getMovie(String id, ServerWebExchange exchange);

    Mono<List<MovieDTO>> getAllAvailableMovies(ServerWebExchange exchange);
}
