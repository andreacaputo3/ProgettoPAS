package it.unisalento.pas.smartcitywastemanagement.repositories;

import it.unisalento.pas.smartcitywastemanagement.domain.Route;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RouteRepository extends MongoRepository<Route, String> {
    // Aggiungi un metodo per recuperare i percorsi di pulizia per un determinato binId
    List<Route> findByBinId(String binId);

    void deleteByPathName(String pathName);
}
