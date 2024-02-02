package it.unisalento.pas.smartcitywastemanagement.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import it.unisalento.pas.smartcitywastemanagement.domain.User;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {

    User findByUsername(String username);

    Optional<User> findById(String id);
    boolean existsByUsername(String username);
}
