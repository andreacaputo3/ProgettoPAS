package it.unisalento.pas.smartcitywastemanagement.repositories;

import it.unisalento.pas.smartcitywastemanagement.domain.WasteDisposal;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface WasteDisposalRepository extends MongoRepository<WasteDisposal, String> {
    List<WasteDisposal> findByUserId(String userId);
    // Puoi definire qui eventuali metodi personalizzati se necessario
}
