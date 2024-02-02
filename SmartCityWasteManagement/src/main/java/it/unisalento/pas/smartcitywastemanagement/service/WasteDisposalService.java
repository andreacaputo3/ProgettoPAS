package it.unisalento.pas.smartcitywastemanagement.service;

import it.unisalento.pas.smartcitywastemanagement.domain.Bin;
import it.unisalento.pas.smartcitywastemanagement.domain.User;
import it.unisalento.pas.smartcitywastemanagement.domain.WasteDisposal;
import it.unisalento.pas.smartcitywastemanagement.dto.UserWasteSeparationPerformanceDTO;
import it.unisalento.pas.smartcitywastemanagement.dto.WasteDisposalDTO;
import it.unisalento.pas.smartcitywastemanagement.exceptions.BinFullException;
import it.unisalento.pas.smartcitywastemanagement.exceptions.BinNotFoundException;
import it.unisalento.pas.smartcitywastemanagement.repositories.BinRepository;
import it.unisalento.pas.smartcitywastemanagement.repositories.WasteDisposalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class WasteDisposalService {

    @Autowired
    private WasteDisposalRepository wasteDisposalRepository;
    @Autowired
    private BinRepository binRepository;

    public void saveWasteDisposal(WasteDisposalDTO wasteDisposalDTO) {
        WasteDisposal wasteDisposal = new WasteDisposal();

        wasteDisposal.setBinId(wasteDisposalDTO.getBinId());
        wasteDisposal.setUserId(wasteDisposalDTO.getUserId());
        wasteDisposal.setWasteType(wasteDisposalDTO.getWasteType());
        wasteDisposal.setDisposalDate(wasteDisposalDTO.getDisposalDate());
        wasteDisposal.setWeight(wasteDisposalDTO.getWeight());
        wasteDisposal.setRecycled(wasteDisposalDTO.isRecycled());

        Bin bin = binRepository.findById(wasteDisposalDTO.getBinId()).orElseThrow(() -> new BinNotFoundException(wasteDisposalDTO.getBinId()));

        if (bin.isFull()) {
            throw new BinFullException(bin.getLocation());
        }

        wasteDisposalRepository.save(wasteDisposal);

        // Ottieni la capacità massima del bin
        BigDecimal maxWeight = bin.getMaxWeight();

        // Ottieni tutti i disposals relativi a questo bin
        List<WasteDisposal> disposals = wasteDisposalRepository.findByBinId(bin.getId());

        // Calcola il peso totale dei rifiuti nel bin
        BigDecimal totalWasteWeight = BigDecimal.ZERO.add(wasteDisposal.getWeight());
        for (WasteDisposal disposal : disposals) {
            BigDecimal disposalWeight = disposal.getWeight();
            if (disposalWeight != null) {
                totalWasteWeight = totalWasteWeight.add(disposalWeight);
            }
        }

        if(totalWasteWeight.compareTo(maxWeight) > -1){
            bin.setFull(true);
            binRepository.save(bin);
        }
    }

    public double getTotalWasteProducedByUser(String userId) {
        // Ottieni tutti i conferimenti di rifiuti dell'utente specificato
        List<WasteDisposal> wasteDisposals = wasteDisposalRepository.findByUserId(userId);

        // Variabile per memorizzare la quantità totale di rifiuti prodotti dall'utente
        double totalWasteProducedByUser = 0.0;

        // Itera su ciascun conferimento di rifiuti dell'utente e aggiungi il peso al totale
        for (WasteDisposal disposal : wasteDisposals) {
            totalWasteProducedByUser += disposal.getWeight().doubleValue();
        }

        return totalWasteProducedByUser;
    }

}
