package it.unisalento.pas.smartcitywastemanagement.controllers;

import it.unisalento.pas.smartcitywastemanagement.domain.Bin;
import it.unisalento.pas.smartcitywastemanagement.domain.CleaningPath;
import it.unisalento.pas.smartcitywastemanagement.dto.BinMapMarkerDTO;
import it.unisalento.pas.smartcitywastemanagement.service.BinService;
import it.unisalento.pas.smartcitywastemanagement.service.CleaningPathService;
import it.unisalento.pas.smartcitywastemanagement.service.WasteMonitoringService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/waste-company")
public class WasteCompanyController {

    @Autowired
    private CleaningPathService cleaningPathService;

    @Autowired
    private BinService binService;

    @Autowired
    private WasteMonitoringService wasteMonitoringService;

    @PostMapping("/check-waste-disposals")
    @PreAuthorize("hasRole('ADMIN_AZIENDA')")
    public ResponseEntity<String> checkWasteDisposals() {
        try {
            List<Bin> binsWithOverflowCapacity = wasteMonitoringService.checkBinCapacity(); // Esegui il controllo dei conferimenti
            if (!binsWithOverflowCapacity.isEmpty()) {
                StringBuilder responseMessage = new StringBuilder("Sovrabbondanza di cassonetti: ");
                for (Bin bin : binsWithOverflowCapacity) {
                    responseMessage.append(bin.getLocation()).append(", ");
                }
                responseMessage.delete(responseMessage.length() - 2, responseMessage.length()); // Rimuovi l'ultima virgola
                return new ResponseEntity<>(responseMessage.toString(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Nessun cassonetto in sovrabbondanza.", HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Errore durante il controllo dei conferimenti: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/map")
    @PreAuthorize("hasRole('ADMIN_AZIENDA')")
    public ResponseEntity<List<BinMapMarkerDTO>> getBinsForMap() {
        try {
            List<BinMapMarkerDTO> binMapMarkers = binService.getAllBins().stream()
                    .map(bin -> {
                        BinMapMarkerDTO marker = new BinMapMarkerDTO();
                        marker.setLocation(bin.getLocation());
                        marker.setLatitude(bin.getLatitude());
                        marker.setLongitude(bin.getLongitude());
                        marker.setStatus(bin.isFull() ? "Pieno" : "Non Pieno");
                        return marker;
                    })
                    .collect(Collectors.toList());

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            return new ResponseEntity<>(binMapMarkers, headers, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/cleaning-paths")
    @PreAuthorize("hasRole('ADMIN_AZIENDA')")
    public ResponseEntity<List<CleaningPath>> instantiateCleaningPaths() {
        try {
            List<Bin> bins = binService.getAllBins();
            List<CleaningPath> cleaningPaths = cleaningPathService.generateCleaningPaths(bins); // Dove bins è una lista di cassonetti
            // Puoi fare qualcosa con i percorsi generati, ad esempio salvarli nel database

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            return new ResponseEntity<>(cleaningPaths, headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/{binId}/empty")
    @PreAuthorize("hasRole('ADMIN_AZIENDA')")
    public ResponseEntity<String> emptyBin(@PathVariable String binId) {
        binService.emptyBin(binId);
        return ResponseEntity.ok("Svuotamento del cassonetto avvenuto con successo.");
    }
}
