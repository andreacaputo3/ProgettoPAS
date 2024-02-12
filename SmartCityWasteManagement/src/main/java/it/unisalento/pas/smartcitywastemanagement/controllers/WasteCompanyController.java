package it.unisalento.pas.smartcitywastemanagement.controllers;

import it.unisalento.pas.smartcitywastemanagement.domain.Bin;
import it.unisalento.pas.smartcitywastemanagement.domain.CleaningPath;
import it.unisalento.pas.smartcitywastemanagement.domain.Route;
import it.unisalento.pas.smartcitywastemanagement.dto.BinMapMarkerDTO;
import it.unisalento.pas.smartcitywastemanagement.dto.RouteDTO;
import it.unisalento.pas.smartcitywastemanagement.service.BinService;
import it.unisalento.pas.smartcitywastemanagement.service.CleaningPathService;
import it.unisalento.pas.smartcitywastemanagement.service.WasteMonitoringService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;
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
    public ResponseEntity<?> checkWasteDisposals() {
        try {
            List<Bin> binsWithOverflowCapacity = wasteMonitoringService.checkBinCapacity();
            if (!binsWithOverflowCapacity.isEmpty()) {
                Map<String, Object> response = new HashMap<>();
                response.put("overloadedBins", binsWithOverflowCapacity);

                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("message", "Nessun cassonetto in sovrabbondanza.");
                return new ResponseEntity<>(response, HttpStatus.OK);
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

    @PostMapping("/{binId}/empty")
    @PreAuthorize("hasRole('ADMIN_AZIENDA')")
    public ResponseEntity<?> emptyBin(@PathVariable String binId) {
        binService.emptyBin(binId);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Svuotamento del cassonetto avvenuto con successo.");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/cleaning-paths")
    @PreAuthorize("hasRole('ADMIN_AZIENDA')")
    public ResponseEntity<List<Route>> saveCleaningPaths(@RequestBody List<String> binIds) {
        try {
            // Mantieni l'ordine degli ID dei cassonetti selezionati
            LinkedHashMap<String, Bin> selectedBinsMap = new LinkedHashMap<>();
            for (String binId : binIds) {
                Bin bin = binService.getBinById(binId);
                selectedBinsMap.put(binId, bin);
            }

            // Utilizza una lista ordinata per conservare l'ordine dei cassonetti
            List<Bin> selectedBins = new ArrayList<>(selectedBinsMap.values());

            // Genera le rotte di pulizia basate sui cassonetti selezionati
            List<Route> cleaningPaths = cleaningPathService.generateRoutes(selectedBins);

            // Salva le rotte generate e restituiscile al frontend
            List<Route> savedRoutes = cleaningPathService.saveRoutes(cleaningPaths);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            return new ResponseEntity<>(savedRoutes, headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/get-cleaning-paths")
    @PreAuthorize("hasRole('ADMIN_AZIENDA')")
    public ResponseEntity<?> getAllCleaningPaths() {
        try {
            List<Route> cleaningPaths = cleaningPathService.getAllRoutes();
            List<Map<String, Object>> routeDTOs = new ArrayList<>();

            for (Route route : cleaningPaths) {
                Bin bin = binService.getBinById(route.getBinId());
                if (bin != null) {
                    Map<String, Object> routeMap = new HashMap<>();
                    routeMap.put("binId", bin.getId());
                    routeMap.put("binLocation", bin.getLocation());
                    routeMap.put("position", route.getPosition());
                    routeDTOs.add(routeMap);
                }
            }

            return ResponseEntity.ok(routeDTOs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }





}
