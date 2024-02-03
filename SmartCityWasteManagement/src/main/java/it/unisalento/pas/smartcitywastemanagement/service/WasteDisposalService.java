package it.unisalento.pas.smartcitywastemanagement.service;

import java.math.BigDecimal;
import java.util.List;

import it.unisalento.pas.smartcitywastemanagement.domain.Bin;
import it.unisalento.pas.smartcitywastemanagement.domain.User;
import it.unisalento.pas.smartcitywastemanagement.domain.WasteDisposal;
import it.unisalento.pas.smartcitywastemanagement.dto.WasteDisposalDTO;
import it.unisalento.pas.smartcitywastemanagement.exceptions.BinFullException;
import it.unisalento.pas.smartcitywastemanagement.exceptions.BinNotFoundException;
import it.unisalento.pas.smartcitywastemanagement.exceptions.BinWasteTypeMismatchException;
import it.unisalento.pas.smartcitywastemanagement.exceptions.UserNotFoundException;
import it.unisalento.pas.smartcitywastemanagement.repositories.BinRepository;
import it.unisalento.pas.smartcitywastemanagement.repositories.UserRepository;
import it.unisalento.pas.smartcitywastemanagement.repositories.WasteDisposalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WasteDisposalService {

    @Autowired
    private WasteDisposalRepository wasteDisposalRepository;

    @Autowired
    private BinRepository binRepository;

    @Autowired
    private UserRepository userRepository;

    public void saveWasteDisposal(WasteDisposalDTO wasteDisposalDTO) {
        WasteDisposal wasteDisposal = new WasteDisposal();

        wasteDisposal.setBinId(wasteDisposalDTO.getBinId());
        wasteDisposal.setUserId(wasteDisposalDTO.getUserId());
        wasteDisposal.setWasteType(wasteDisposalDTO.getWasteType());
        wasteDisposal.setDisposalDate(wasteDisposalDTO.getDisposalDate());
        wasteDisposal.setWeight(wasteDisposalDTO.getWeight());
        wasteDisposal.setRecycled(false);

        Bin bin = binRepository.findById(wasteDisposalDTO.getBinId())
                .orElseThrow(() -> new BinNotFoundException(wasteDisposalDTO.getBinId()));

        if (bin.isFull()) {
            throw new BinFullException(bin.getLocation());
        }

        // Verifica se il tipo di conferimento non corrisponde al tipo del bidone
        if (!bin.getType().equals(wasteDisposal.getWasteType())) {
            // Incrementa il contatore di conferimenti errati per l'utente
            User user = userRepository.findById(wasteDisposalDTO.getUserId())
                    .orElseThrow(() -> new UserNotFoundException(wasteDisposalDTO.getUserId()));
            user.setIncorrectDisposalCount(user.getIncorrectDisposalCount() + 1);
            userRepository.save(user);
            //finire con eccezioni
        }

        // Aggiungo disposal
        wasteDisposalRepository.save(wasteDisposal);

        // Aggiorno il peso del cassonetto
        updateBinCurrentWeight(bin);
    }

    private void updateBinCurrentWeight(Bin bin) {
        BigDecimal currentWeight = getCurrentWeightOfBin(bin);

        bin.setCurrentWeight(currentWeight);
        binRepository.save(bin);

        // Controlla se il cassonetto è pieno
        if (currentWeight.compareTo(bin.getMaxWeight()) >= 0) {
            bin.setFull(true);
            binRepository.save(bin);
        }
    }

    private BigDecimal getCurrentWeightOfBin(Bin bin) {
        // Ottieni tutti i conferimenti di rifiuti non riciclati relativi a questo cassonetto
        List<WasteDisposal> disposals = wasteDisposalRepository.findByBinIdAndIsRecycledFalse(bin.getId());

        // Calcola il peso totale dei rifiuti non riciclati nel cassonetto
        BigDecimal totalWeight = BigDecimal.ZERO;
        for (WasteDisposal disposal : disposals) {
            totalWeight = totalWeight.add(disposal.getWeight());
        }
        return totalWeight;
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

    public void updateDisposalsAfterBinEmpty(String binId) {
        List<WasteDisposal> disposals = wasteDisposalRepository.findByBinIdAndIsRecycledFalse(binId);
        for (WasteDisposal disposal : disposals) {
            disposal.setRecycled(true);
            wasteDisposalRepository.save(disposal);
        }
    }
}
