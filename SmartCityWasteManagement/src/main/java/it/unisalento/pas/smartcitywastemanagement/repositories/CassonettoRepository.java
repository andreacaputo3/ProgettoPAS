package it.unisalento.pas.smartcitywastemanagement.repositories;

import it.unisalento.pas.smartcitywastemanagement.domain.Cassonetto;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface CassonettoRepository extends MongoRepository<Cassonetto, String> {

    Optional<Cassonetto> findById(String id);

    List<Cassonetto> findByTipoRifiuto(String tipoRifiuto);

    List<Cassonetto> findByCittadinoId(String cittadinoId);

}
