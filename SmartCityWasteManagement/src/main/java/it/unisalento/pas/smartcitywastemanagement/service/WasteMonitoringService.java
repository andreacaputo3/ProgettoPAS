package it.unisalento.pas.smartcitywastemanagement.service;

import it.unisalento.pas.smartcitywastemanagement.domain.Bin;
import it.unisalento.pas.smartcitywastemanagement.repositories.BinRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class WasteMonitoringService {

    private static final double CAPACITY_THRESHOLD_PERCENTAGE = 0.8; // Soglia percentuale di capienza del cassonetto

    @Autowired
    private BinRepository binRepository;

    public void checkBinCapacity() {
        try {
            List<Bin> allBins = binRepository.findAll();

            for (Bin bin : allBins) {
                double currentCapacityPercentage = calculateCurrentCapacityPercentage(bin);

                if (currentCapacityPercentage > CAPACITY_THRESHOLD_PERCENTAGE) {
                    System.out.println("Sovrabbondanza di cassonetto: " + bin.getLocation() +
                            " con capienza al " + (int) (currentCapacityPercentage * 100) + "%");
                }
            }
        } catch (Exception e) {
            // Gestisci eventuali eccezioni durante il controllo della capienza dei cassonetti
            System.err.println("Errore durante il controllo della capienza dei cassonetti: " + e.getMessage());
            e.printStackTrace(); // Stampa lo stack trace dell'eccezione per il debug
        }
    }

    private double calculateCurrentCapacityPercentage(Bin bin) {
        BigDecimal currentWeight = getCurrentWeightOfBin(bin);
        BigDecimal maxWeight = bin.getMaxWeight();

        if (currentWeight == null) {
            // Gestire il caso in cui il peso corrente è null
            return 0.0; // O restituire un valore di default o sollevare un'eccezione
        }

        if (maxWeight.compareTo(BigDecimal.ZERO) == 0) {
            // Cassonetto senza limite di peso, considera la capacità al 100%
            return 1.0;
        } else {
            return currentWeight.divide(maxWeight, 2, RoundingMode.HALF_UP).doubleValue();
        }
    }

    private BigDecimal getCurrentWeightOfBin(Bin bin) {
        Bin binFromDB = binRepository.findById(bin.getId()).orElse(null);
        if (binFromDB != null) {
            return binFromDB.getCurrentWeight();
        } else {
            // Gestione del caso in cui il cassonetto non viene trovato
            return BigDecimal.ZERO; // Oppure solleva un'eccezione o restituisce un valore di default
        }
    }
}
