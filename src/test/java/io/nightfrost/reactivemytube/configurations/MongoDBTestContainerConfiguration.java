package io.nightfrost.reactivemytube.configurations;

import org.testcontainers.containers.MongoDBContainer;

public class MongoDBTestContainerConfiguration {

    public static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:latest")
            .withExposedPorts(27017);

    static {
        mongoDBContainer.start();
        var mappedPort = mongoDBContainer.getMappedPort(27017);
        System.setProperty("mongodb.container.port", String.valueOf(mappedPort));
    }
}
