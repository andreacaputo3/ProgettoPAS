package it.unisalento.pas.smartcitywastemanagement.service;

import it.unisalento.pas.smartcitywastemanagement.domain.User;
import it.unisalento.pas.smartcitywastemanagement.domain.WasteDisposal;
import it.unisalento.pas.smartcitywastemanagement.dto.UserDTO;
import it.unisalento.pas.smartcitywastemanagement.dto.UserWasteSeparationPerformanceDTO;
import it.unisalento.pas.smartcitywastemanagement.repositories.UserRepository;
import it.unisalento.pas.smartcitywastemanagement.repositories.WasteDisposalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MunicipalOfficeService {

    @Autowired
    private WasteDisposalRepository wasteDisposalRepository;

    @Autowired
    private WasteSeparationPerformanceService wasteSeparationPerformanceService;

    @Autowired
    private UserRepository userRepository;

    public Map<String, Double> calculateYearlyPaymentAmounts() {
        // Ottieni tutti gli utenti registrati nel comune
        List<User> users = userRepository.findByRole("USER");

        // Costo fisso per unità di spazzatura (in euro)
        double costPerUnitOfWaste = 0.5; // Esempio, puoi modificare questo valore secondo le tue esigenze

        // Mappa per memorizzare gli importi da pagare per ogni cittadino
        Map<String, Double> yearlyPaymentAmounts = new HashMap<>();

        // Calcola l'importo da pagare per ogni cittadino
        for (User user : users) {
            // Calcola la quantità totale di spazzatura prodotta dall'utente
            double totalWasteProducedByUser = getTotalWasteProducedByUser(user.getId());

            // Calcola l'importo da pagare per l'utente
            double yearlyPaymentAmountForUser = totalWasteProducedByUser * costPerUnitOfWaste;

            // Aggiungi l'importo da pagare per l'utente alla mappa
            yearlyPaymentAmounts.put(user.getId(), yearlyPaymentAmountForUser);
        }

        return yearlyPaymentAmounts;
    }

    public List<UserDTO> identifyCitizensToAware() {
        // Ottieni tutti gli utenti da sensibilizzare (isAware = false)
        List<User> usersToEducate = userRepository.findByIsAwared(false);

        // Converte gli utenti in DTO
        List<UserDTO> userDTOs = usersToEducate.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return userDTOs;
    }

    public UserWasteSeparationPerformanceDTO analyzeWasteSeparationPerformance() {
        // Ottieni tutti gli utenti registrati nel comune
        List<User> users = userRepository.findAll();

        if (users.isEmpty()) {
         //da finire
        }

        // Variabili per memorizzare le statistiche aggregate
        int totalDisposals = 0;
        int incorrectDisposalCount = 0; // Contatore per il numero totale di smaltimenti errati
        Map<String, Integer> wasteTypeCounts = new HashMap<>();

        // Calcola le statistiche aggregate per tutti gli utenti
        for (User user : users) {
            UserWasteSeparationPerformanceDTO userPerformance = wasteSeparationPerformanceService.calculateUserWasteSeparationPerformance(user.getId());
            totalDisposals += userPerformance.getTotalDisposals();
            incorrectDisposalCount += userPerformance.getIncorrectDisposalCount();
            // Aggiungi il conteggio dei tipi di rifiuti al conteggio complessivo
            for (Map.Entry<String, Integer> entry : userPerformance.getWasteTypeCounts().entrySet()) {
                String wasteType = entry.getKey();
                int count = entry.getValue();
                wasteTypeCounts.put(wasteType, wasteTypeCounts.getOrDefault(wasteType, 0) + count);
            }
        }

        // Crea un nuovo oggetto UserWasteSeparationPerformanceDTO con le statistiche aggregate
        UserWasteSeparationPerformanceDTO municipalPerformanceDTO = new UserWasteSeparationPerformanceDTO();
        municipalPerformanceDTO.setTotalDisposals(totalDisposals);
        municipalPerformanceDTO.setIncorrectDisposalCount(incorrectDisposalCount);
        municipalPerformanceDTO.setWasteTypeCounts(wasteTypeCounts);

        return municipalPerformanceDTO;
    }

    private UserDTO convertToDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setName(user.getName());
        userDTO.setSurname(user.getSurname());
        userDTO.setMail(user.getMail());
        userDTO.setAge(user.getAge());
        userDTO.setUsername(user.getUsername());
        userDTO.setRole(user.getRole());
        return userDTO;
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
