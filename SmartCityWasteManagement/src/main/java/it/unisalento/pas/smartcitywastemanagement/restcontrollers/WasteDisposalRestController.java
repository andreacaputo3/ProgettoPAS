package it.unisalento.pas.smartcitywastemanagement.restcontrollers;

import it.unisalento.pas.smartcitywastemanagement.dto.WasteDisposalDTO;
import it.unisalento.pas.smartcitywastemanagement.service.WasteDisposalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/waste-disposals")
public class WasteDisposalRestController {

    @Autowired
    private WasteDisposalService wasteDisposalService;

    @PostMapping("/create")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> submitWasteDisposal(@RequestBody WasteDisposalDTO wasteDisposalDTO) {

        wasteDisposalDTO.setDisposalDate(new Date());

        if (!wasteDisposalService.saveWasteDisposal(wasteDisposalDTO)) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Non puoi inserire questo conferimento, cassonetto pieno");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        Map<String, String> response = new HashMap<>();
        response.put("message", "Conferimento effettuato con successo");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
