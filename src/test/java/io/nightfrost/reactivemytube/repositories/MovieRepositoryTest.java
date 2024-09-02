package io.nightfrost.reactivemytube.repositories;

import io.nightfrost.reactivemytube.models.Metadata;
import io.nightfrost.reactivemytube.models.Tags;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DataMongoTest
@ExtendWith(SpringExtension.class)
@Disabled
public class MovieRepositoryTest {

    @Autowired
    private MovieRepository movieRepository;

    private String movieId;

    @BeforeEach
    public void initialize() throws IOException {
        // Load the MP4 file from classpath
        ClassPathResource resource = new ClassPathResource("movies/20131214_NEVER GIVE UP YOUR WAAAAAAAAAAAAY.mp4");
        byte[] content = Files.readAllBytes(resource.getFile().toPath());

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
                    //movieRepository.getResource(entity.toString()).flatMapMany(resourcetwo -> resource.getContent().flatMap(tempcontent -> tempcontent.));
                    // Add assertions to check if the entity is correctly stored
                    // For example:
                    //return entity.get().equals("test-video.mp4");
                    return true;
                            //&& entity.getContent().equals(content);
                })
                .verifyComplete();
    }
}
