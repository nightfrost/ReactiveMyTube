package io.nightfrost.reactivemytube.repositories;

import io.nightfrost.reactivemytube.models.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface UserRepository extends ReactiveMongoRepository<User, String> {
}
