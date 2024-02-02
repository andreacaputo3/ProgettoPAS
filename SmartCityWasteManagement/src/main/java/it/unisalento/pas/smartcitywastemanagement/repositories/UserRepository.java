package it.unisalento.pas.smartcitywastemanagement.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import it.unisalento.pas.smartcitywastemanagement.domain.User;

public interface UserRepository extends MongoRepository<User, String> {

    User findByUsername(String username);


    boolean existsByUsername(String username);
}
