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

@RestController
@RequestMapping("/api/waste-disposals")
public class WasteDisposalRestController {

    @Autowired
    private WasteDisposalService wasteDisposalService;

    @PostMapping("/create")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> submitWasteDisposal(@RequestBody WasteDisposalDTO wasteDisposalDTO) {

        wasteDisposalDTO.setDisposalDate(new Date());

        wasteDisposalService.saveWasteDisposal(wasteDisposalDTO);

        return new ResponseEntity<>("Waste disposal submitted successfully", HttpStatus.OK);

    }
}
