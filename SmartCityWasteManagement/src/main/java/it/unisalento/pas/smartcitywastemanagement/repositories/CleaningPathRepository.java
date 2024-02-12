package it.unisalento.pas.smartcitywastemanagement.repositories;

import it.unisalento.pas.smartcitywastemanagement.domain.CleaningPath;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CleaningPathRepository extends MongoRepository<CleaningPath, String> {
    // In questa interfaccia non è necessario aggiungere metodi personalizzati se si utilizzano solo le operazioni CRUD di base fornite da MongoRepository.
    // Tuttavia, è possibile aggiungere metodi personalizzati se necessario.
}
