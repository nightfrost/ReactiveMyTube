package io.nightfrost.reactivemytube.handlers;

import io.nightfrost.reactivemytube.dtos.MovieDTO;
import io.nightfrost.reactivemytube.exceptions.ResourceNotFoundException;
import io.nightfrost.reactivemytube.services.MovieServiceImpl;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.codec.multipart.Part;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/movies")
@RequiredArgsConstructor
public class MovieController {

    private final MovieServiceImpl movieService;

    private static final Logger LOGGER = LoggerFactory.getLogger(MovieController.class);

    @PostMapping()
    public Mono<ResponseEntity> postMovie (ServerWebExchange exchange) {
        Mono<FilePart> fileParts = exchange.getMultipartData().flatMap(parts ->  {
            Map<String, Part> partMap = parts.toSingleValueMap();
            FilePart file = (FilePart) partMap.get("file");
            return Mono.just(file);
        });

        return movieService.putMovie(fileParts)
                .doOnSuccess(response -> LOGGER.info("Put movie succeeded with status code: {}", response.getStatusCode()))
                .doOnError(e -> LOGGER.error("Put movie failed with reason:", e));
    }

    @GetMapping("/{id}")
    public Flux<Void> getMovie(@PathVariable String id, ServerWebExchange exchange) {
        return movieService.getMovie(id, exchange)
                .doOnCancel(() -> LOGGER.error("Stream cancelled."))
                .doOnError(exception -> LOGGER.error("Stream error, see stack", exception))
                .doOnComplete(() -> LOGGER.info("Stream finished."));
    }

    @GetMapping()
    public Mono<List<MovieDTO>> getAllAvailableMovies(ServerWebExchange exchange) {
        return movieService.getAllAvailableMovies(exchange)
                .doOnError(exception -> LOGGER.error("Retrieval of all movies failed, see stack: ", exception))
                .doOnSuccess(response -> LOGGER.info("Retrieved all ({}) available movies.", response.size()));
    }

    @GetMapping(params = "query")
    public Mono<List<MovieDTO>> queryMovies(@RequestParam String query, ServerWebExchange exchange) {
        return movieService.queryMovies(query, exchange)
                .doOnError(exception -> LOGGER.error("Retrieval of all movies failed, see stack: ", exception))
                .doOnSuccess(response -> LOGGER.info("Retrieved all ({}) available movies.", response.size()));
    }
}
