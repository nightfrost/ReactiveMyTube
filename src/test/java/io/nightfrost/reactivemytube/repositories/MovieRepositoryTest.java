package io.nightfrost.reactivemytube.repositories;

import io.nightfrost.reactivemytube.configurations.MongoConfiguration;
import io.nightfrost.reactivemytube.models.Metadata;
import io.nightfrost.reactivemytube.models.Tags;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@Import(MongoConfiguration.class)
@SpringBootTest
public class MovieRepositoryTest {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private MongoConverter mongoConverter;

    private String movieId;

    public void initialize() throws IOException {
        // Load the MP4 file from classpath
        ClassPathResource localResource = new ClassPathResource("movies/20131214_NEVER GIVE UP YOUR WAAAAAAAAAAAAY.mp4");
        byte[] content = Files.readAllBytes(localResource.getFile().toPath());

        // Mock FilePart
        FilePart filePart = mock(FilePart.class);
        when(filePart.filename()).thenReturn("20131214_NEVER GIVE UP YOUR WAAAAAAAAAAAAY.mp4");
        when(filePart.content()).thenReturn(Flux.just(DefaultDataBufferFactory.sharedInstance.wrap(content)));

        //Mock Metadata
        Metadata metadata = mock(Metadata.class);
        when(metadata.getName()).thenReturn("Never Give up your way");
        when(metadata.getPosterUrl()).thenReturn("https://google.dk/img.jpg");
        ArrayList<Tags> tags = new ArrayList<>();
        tags.add(Tags.COMEDY);
        tags.add(Tags.HORROR);
        when(metadata.getTags()).thenReturn(tags);

        // Call the repository method to store the file
        Mono<ObjectId> result = movieRepository.store(Mono.just(filePart), metadata);

        // Verify the result
        StepVerifier.create(result)
                .expectNextMatches(entity -> {
                    movieRepository.findById(entity.toString()).flatMap(savedMovie -> {
                        assert savedMovie.getFilename().equals("20131214_NEVER GIVE UP YOUR WAAAAAAAAAAAAY.mp4");

                        assert savedMovie.getMetadata() != null;
                        Metadata savedMetadata = mongoConverter.read(Metadata.class, savedMovie.getMetadata());
                        assert savedMetadata.getPosterUrl().equals("https://google.dk/img.jpg");
                        assert savedMetadata.getTags().size() == 2;
                        assert savedMetadata.getName().equals("Never Give up your way");
                        return null;
                    });
                    movieRepository.getResource(entity.toString())
                            .flatMapMany(resource -> resource.getDownloadStream().collectList())
                            .map(listOfBuffers -> {
                                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                listOfBuffers.forEach(buffer -> {
                                    byte[] bytes = new byte[buffer.readableByteCount()];
                                    buffer.read(bytes);
                                    byteArrayOutputStream.write(bytes, 0, bytes.length);
                                    DataBufferUtils.release(buffer);
                        });

                        assert Arrays.equals(byteArrayOutputStream.toByteArray(), content);
                        return null;
                    });
                    return true;
                })
                .verifyComplete();
    }

    @Test
    public void initialTest() throws IOException {
        // Load the MP4 file from classpath
        ClassPathResource localResource = new ClassPathResource("movies/20131214_NEVER GIVE UP YOUR WAAAAAAAAAAAAY.mp4");
        byte[] content = Files.readAllBytes(localResource.getFile().toPath());

        // Mock FilePart
        FilePart filePart = mock(FilePart.class);
        when(filePart.filename()).thenReturn("20131214_NEVER GIVE UP YOUR WAAAAAAAAAAAAY.mp4");
        when(filePart.content()).thenReturn(Flux.just(DefaultDataBufferFactory.sharedInstance.wrap(content)));

        //Mock Metadata
        Metadata metadata = mock(Metadata.class);
        when(metadata.getName()).thenReturn("Never Give up your way");
        when(metadata.getPosterUrl()).thenReturn("https://google.dk/img.jpg");
        ArrayList<Tags> tags = new ArrayList<>();
        tags.add(Tags.COMEDY);
        tags.add(Tags.HORROR);
        when(metadata.getTags()).thenReturn(tags);

        // Call the repository method to store the file
        Mono<ObjectId> result = movieRepository.store(Mono.just(filePart), metadata);

        // Verify the result
        StepVerifier.create(result)
                .expectNextMatches(entity -> {
                    movieRepository.findById(entity.toString()).flatMap(savedMovie -> {
                        assert savedMovie.getFilename().equals("20131214_NEVER GIVE UP YOUR WAAAAAAAAAAAAY.mp4");

                        assert savedMovie.getMetadata() != null;
                        Metadata savedMetadata = mongoConverter.read(Metadata.class, savedMovie.getMetadata());
                        assert savedMetadata.getPosterUrl().equals("https://google.dk/img.jpg");
                        assert savedMetadata.getTags().size() == 2;
                        assert savedMetadata.getName().equals("Never Give up your way");
                        return null;
                    });
                    movieRepository.getResource(entity.toString())
                            .flatMapMany(resource -> resource.getDownloadStream().collectList())
                            .map(listOfBuffers -> {
                                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                listOfBuffers.forEach(buffer -> {
                                    byte[] bytes = new byte[buffer.readableByteCount()];
                                    buffer.read(bytes);
                                    byteArrayOutputStream.write(bytes, 0, bytes.length);
                                    DataBufferUtils.release(buffer);
                                });

                                assert Arrays.equals(byteArrayOutputStream.toByteArray(), content);
                                return null;
                            });
                    return true;
                })
                .verifyComplete();
    }
}
