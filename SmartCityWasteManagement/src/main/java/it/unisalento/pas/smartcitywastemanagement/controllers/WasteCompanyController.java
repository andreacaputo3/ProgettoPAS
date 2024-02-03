package it.unisalento.pas.smartcitywastemanagement.controllers;

import it.unisalento.pas.smartcitywastemanagement.domain.Bin;
import it.unisalento.pas.smartcitywastemanagement.domain.CleaningPath;
import it.unisalento.pas.smartcitywastemanagement.dto.BinMapMarkerDTO;
import it.unisalento.pas.smartcitywastemanagement.service.BinService;
import it.unisalento.pas.smartcitywastemanagement.service.CleaningPathService;
import it.unisalento.pas.smartcitywastemanagement.service.WasteMonitoringService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
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
            wasteMonitoringService.checkBinCapacity(); // Esegui il controllo dei conferimenti
            return new ResponseEntity<>("Controllo dei conferimenti avviato con successo", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Errore durante il controllo dei conferimenti: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/map")
    @PreAuthorize("hasRole('ADMIN_AZIENDA')")
    public ResponseEntity<List<BinMapMarkerDTO>> getBinsForMap() {
        List<Bin> bins = binService.getAllBins();

        List<BinMapMarkerDTO> binMapMarkers = bins.stream()
                .map(bin -> {
                    BinMapMarkerDTO marker = new BinMapMarkerDTO();
                    marker.setId(bin.getId());
                    marker.setLocation(bin.getLocation());
                    marker.setLatitude(bin.getLatitude());
                    marker.setLongitude(bin.getLongitude());
                    marker.setStatus(bin.isFull() ? "Full" : "Not Full");
                    return marker;
                })
                .collect(Collectors.toList());

        return new ResponseEntity<>(binMapMarkers, HttpStatus.OK);
    }

    @PostMapping("/cleaning-paths")
    @PreAuthorize("hasRole('ADMIN_AZIENDA')")
    public ResponseEntity<List<CleaningPath>> instantiateCleaningPaths() {
        try {
            List<Bin> bins = binService.getAllBins();
            List<CleaningPath> cleaningPaths = cleaningPathService.generateCleaningPaths(bins); // Dove bins è una lista di cassonetti
            // Puoi fare qualcosa con i percorsi generati, ad esempio salvarli nel database

            return new ResponseEntity<>(cleaningPaths, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
