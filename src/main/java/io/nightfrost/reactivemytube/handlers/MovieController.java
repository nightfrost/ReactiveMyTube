package io.nightfrost.reactivemytube.handlers;

import io.nightfrost.reactivemytube.dtos.MovieDTO;
import io.nightfrost.reactivemytube.exceptions.ResourceNotFoundException;
import io.nightfrost.reactivemytube.models.Metadata;
import io.nightfrost.reactivemytube.models.Tags;
import io.nightfrost.reactivemytube.services.MovieServiceImpl;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.codec.multipart.Part;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple4;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/movies")
@RequiredArgsConstructor
public class MovieController {

    private final MovieServiceImpl movieService;

    private static final Logger LOGGER = LoggerFactory.getLogger(MovieController.class);

    @PostMapping()
    public Mono<ResponseEntity> postMovie (ServerWebExchange exchange) {
        Mono<Tuple4<FilePart, String, String, ArrayList<Tags>>> extractedMetdata = extractMetadata(exchange);

        return extractedMetdata.flatMap(tuple -> {
            Mono<FilePart> file = Mono.just(tuple.getT1());
            String name = tuple.getT2();
            String posterUrl = tuple.getT3();
            ArrayList<Tags> tags = tuple.getT4();

            Metadata metadata = Metadata.builder().name(name).posterUrl(posterUrl).tags(tags).build();

            return movieService.putMovie(file, metadata)
                    .doOnSuccess(response -> LOGGER.info("Put movie succeeded with status code: {}", response.getStatusCode()))
                    .doOnError(e -> LOGGER.error("Put movie failed with reason:", e));
        });
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

    private Mono<Tuple4<FilePart, String, String, ArrayList<Tags>>> extractMetadata (ServerWebExchange exchange) {
        return exchange.getMultipartData().flatMap(parts -> {
            Map<String, Part> partMap = parts.toSingleValueMap();
            FilePart file = (FilePart) partMap.get("file");

            Mono<String> monoName = Mono.from((partMap.get("name")).content().map(dataBuffer -> {
                byte[] bytes = new byte[dataBuffer.readableByteCount()];
                dataBuffer.read(bytes);
                DataBufferUtils.release(dataBuffer);
                return new String(bytes, StandardCharsets.UTF_8);
            }));

            Mono<String> monoPosterUrl = Mono.from((partMap.get("posterUrl")).content().map(dataBuffer -> {
                byte[] bytes = new byte[dataBuffer.readableByteCount()];
                dataBuffer.read(bytes);
                DataBufferUtils.release(dataBuffer);
                return new String(bytes, StandardCharsets.UTF_8);
            }));

            Mono<ArrayList<Tags>> monoTags = Mono.from((partMap.get("tags")).content().map(dataBuffer -> {
                byte[] bytes = new byte[dataBuffer.readableByteCount()];
                dataBuffer.read(bytes);
                DataBufferUtils.release(dataBuffer);
                String tagString = new String(bytes, StandardCharsets.UTF_8);

                return Arrays.stream(tagString.split(",")).map(String::trim).map(tag -> Tags.valueOf(tag.toUpperCase())).collect(Collectors.toCollection(ArrayList::new));
            }));

            return Mono.zip(Mono.just(file), monoName, monoPosterUrl, monoTags);
        });
    }
}
