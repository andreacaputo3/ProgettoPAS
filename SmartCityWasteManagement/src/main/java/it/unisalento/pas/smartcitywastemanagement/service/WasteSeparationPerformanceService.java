package it.unisalento.pas.smartcitywastemanagement.service;

import it.unisalento.pas.smartcitywastemanagement.domain.User;
import it.unisalento.pas.smartcitywastemanagement.domain.WasteDisposal;
import it.unisalento.pas.smartcitywastemanagement.dto.UserWasteSeparationPerformanceDTO;
import it.unisalento.pas.smartcitywastemanagement.exceptions.UserNotFoundException;
import it.unisalento.pas.smartcitywastemanagement.repositories.UserRepository;
import it.unisalento.pas.smartcitywastemanagement.repositories.WasteDisposalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class WasteSeparationPerformanceService {

    @Autowired
    private WasteDisposalRepository wasteDisposalRepository;

    @Autowired
    private UserRepository userRepository;

    public UserWasteSeparationPerformanceDTO calculateUserWasteSeparationPerformance(String id) {
        // Trova tutti i conferimenti dei rifiuti per l'utente specificato
        List<WasteDisposal> userWasteDisposals = wasteDisposalRepository.findByUserId(id);

        int totalDisposals = userWasteDisposals.size();
        Map<String, Integer> wasteTypeCounts = new HashMap<>();

        // Conta il numero di conferimenti per tipo di rifiuto
        for (WasteDisposal disposal : userWasteDisposals) {
            String wasteType = disposal.getWasteType();
            wasteTypeCounts.put(wasteType, wasteTypeCounts.getOrDefault(wasteType, 0) + 1);
        }

        // Ottieni il numero totale di conferimenti errati dall'utente dall'entità User
        int incorrectDisposalCount = getUserIncorrectDisposalCount(id);

        // Crea un oggetto UserWasteSeparationPerformanceDTO con i risultati del calcolo
        UserWasteSeparationPerformanceDTO performanceDTO = new UserWasteSeparationPerformanceDTO();
        performanceDTO.setTotalDisposals(totalDisposals);
        performanceDTO.setWasteTypeCounts(wasteTypeCounts);
        performanceDTO.setIncorrectDisposalCount(incorrectDisposalCount);

        return performanceDTO;
    }

    public int getUserIncorrectDisposalCount(String userId) {
        // Recupera l'utente dal repository utilizzando l'ID
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Utente non trovato con ID: " + userId));

        // Restituisci il valore di incorrectDisposalCount
        return user.getIncorrectDisposalCount();
    }
}
