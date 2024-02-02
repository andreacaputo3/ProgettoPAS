package it.unisalento.pas.smartcitywastemanagement.service;

import it.unisalento.pas.smartcitywastemanagement.domain.WasteDisposal;
import it.unisalento.pas.smartcitywastemanagement.domain.WasteSeparationPerformance;
import it.unisalento.pas.smartcitywastemanagement.dto.UserWasteSeparationPerformanceDTO;
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

        // Crea un oggetto UserWasteSeparationPerformanceDTO con i risultati del calcolo
        UserWasteSeparationPerformanceDTO performanceDTO = new UserWasteSeparationPerformanceDTO();
        performanceDTO.setUserId(id);
        performanceDTO.setTotalDisposals(totalDisposals);
        performanceDTO.setWasteTypeCounts(wasteTypeCounts);

        return performanceDTO;
    }

}
