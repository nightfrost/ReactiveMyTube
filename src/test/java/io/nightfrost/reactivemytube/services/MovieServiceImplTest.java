package io.nightfrost.reactivemytube.services;

import io.nightfrost.reactivemytube.ReactiveMyTubeApplication;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.gridfs.ReactiveGridFsTemplate;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.server.ServerWebExchangeDecorator;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = ReactiveMyTubeApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MovieServiceImplTest {

    @Autowired
    private WebTestClient client;

    @Autowired
    private ReactiveGridFsTemplate reactiveGridFsTemplate;

    private final String API_ROUTE = "api/v1/movies";


    @Test
    public void GivenMovieID_WhenGetMovieByID_ReturnStreamOfMovie() {
        String movieId = "657c6e8875baa3616385b996";

        client.get().uri(API_ROUTE + "/" + movieId)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType("video/mp4");

    }

    public void GivenMovie_WhenPutMovie_ReturnSuccess() {
    }
}
