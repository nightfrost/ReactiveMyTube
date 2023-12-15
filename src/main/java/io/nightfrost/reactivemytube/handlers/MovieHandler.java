/*

##############  Functional version of the MovieController, called MovieHandler. ##########################
Left as example.





package io.nightfrost.reactivemytube.handlers;

import io.nightfrost.reactivemytube.services.MovieService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.codec.multipart.Part;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

import static org.springframework.web.reactive.function.BodyInserters.fromValue;

@Component
@RequiredArgsConstructor
public class MovieHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(MovieHandler.class);

    private final MovieService movieService;

    public Mono<ServerResponse> putMovie(ServerRequest request) {
        Mono<FilePart> fileParts = request.multipartData().flatMap(parts ->  {
            Map<String, Part> partMap = parts.toSingleValueMap();
            FilePart file = (FilePart) partMap.get("file");
            return Mono.just(file);
        });


        return movieService.putMovie(fileParts)
                .doOnSuccess(response -> LOGGER.info("Put movie succeeded with status code: {}", response.getStatusCode()))
                .doOnError(e -> LOGGER.error("Put movie failed with reason:", e))
                .flatMap(response -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(fromValue(response)))
                .switchIfEmpty(ServerResponse.unprocessableEntity().build());
    }

    public Mono<ServerResponse> getMovie(ServerRequest request) {
        String id = request.pathVariable("id");

        return movieService.getMovie(id).flatMap(Mono::just);
    }
}
*/
