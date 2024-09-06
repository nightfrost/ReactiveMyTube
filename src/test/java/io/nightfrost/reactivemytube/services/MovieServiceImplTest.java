package io.nightfrost.reactivemytube.services;

import io.nightfrost.reactivemytube.ReactiveMyTubeApplication;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import java.util.ArrayList;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = ReactiveMyTubeApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MovieServiceImplTest {

    @Autowired
    private WebTestClient client;

    private String movieId;

    private static final String API_ROUTE = "api/v1/movies";

    @BeforeEach
    public void getAvailableTestMovie() {
        MultipartBodyBuilder bodyBuilder = new MultipartBodyBuilder();

        bodyBuilder.part("file", new ClassPathResource("movies/20131214_NEVER GIVE UP YOUR WAAAAAAAAAAAAY.mp4"))
                .filename("Never Give up your way")
                .contentType(MediaType.APPLICATION_OCTET_STREAM);

        bodyBuilder.part("name", "Never give up your way");
        bodyBuilder.part("posterUrl", "http;//imagesource.example/example.jpg");
        bodyBuilder.part("tags", "Comedy, Horror");

        client.post().uri(API_ROUTE)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(bodyBuilder.build()))
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.id").value((id) -> movieId = String.valueOf(id));

        movieId = movieId.replace("{", "");
        movieId = movieId.replace("}", "");

        assert !movieId.contains("{") && !movieId.contains("}") && !movieId.isBlank();
    }

    @AfterEach
    public void cleanupTest() {
        client.delete().uri(API_ROUTE + "/" + movieId).exchange().expectStatus().isOk().expectBody();
    }

    @Test
    public void GivenMovieID_WhenGetMovieByID_ReturnStreamOfMovie() {
        client.get().uri(API_ROUTE + "/" + movieId)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType("video/mp4");
    }

    @Test
    public void GivenMovie_WhenPutMovie_ReturnSuccessAndCreatedID() {
        MultipartBodyBuilder bodyBuilder = new MultipartBodyBuilder();
        ArrayList<String> tempMovieId = new ArrayList<>();

        bodyBuilder.part("file", new ClassPathResource("movies/20131214_NEVER GIVE UP YOUR WAAAAAAAAAAAAY.mp4"))
                .filename("Never Give up your way")
                .contentType(MediaType.APPLICATION_OCTET_STREAM);

        bodyBuilder.part("name", "Never give up your way");
        bodyBuilder.part("posterUrl", "http;//imagesource.example/example.jpg");
        bodyBuilder.part("tags", "Comedy, Horror");

        client.post().uri(API_ROUTE)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(bodyBuilder.build()))
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.id").value((id) -> tempMovieId.add(String.valueOf(id)));

        client.delete().uri(API_ROUTE + "/" + tempMovieId.getFirst()).exchange().expectStatus().isOk();
    }

    @Test
    public void GivenNothing_WhenGetAllAvailableMovies_ReturnSuccessAndList() {
        client.get().uri(API_ROUTE).exchange().expectStatus().isOk().expectBody();
    }

    @Test
    public void GivenQuery_WhenGetQueryMovies_ReturnSuccess() {
        String query = "d";
        client.get().uri(API_ROUTE + "?query=" + query).exchange().expectStatus().isOk().expectBody();
    }

    @Test
    public void GivenMovieId_WhenDeleteMovie_ReturnSuccess() {
        MultipartBodyBuilder bodyBuilder = new MultipartBodyBuilder();
        ArrayList<String> tempMovieId = new ArrayList<>();

        bodyBuilder.part("file", new ClassPathResource("movies/20131214_NEVER GIVE UP YOUR WAAAAAAAAAAAAY.mp4"))
                .filename("Never Give up your way")
                .contentType(MediaType.APPLICATION_OCTET_STREAM);

        bodyBuilder.part("name", "Never give up your way");
        bodyBuilder.part("posterUrl", "http;//imagesource.example/example.jpg");
        bodyBuilder.part("tags", "Comedy, Horror");

        client.post().uri(API_ROUTE)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(bodyBuilder.build()))
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.id").value((id) -> tempMovieId.add(String.valueOf(id)));

        client.delete().uri(API_ROUTE + "/" + tempMovieId.getFirst()).exchange().expectStatus().isOk();
    }

    //TODO: Additional testing - above is only successful requests - no failure tests.
}
