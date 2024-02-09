package it.unisalento.pas.smartcitywastemanagement.restcontrollers;

import it.unisalento.pas.smartcitywastemanagement.domain.Bin;
import it.unisalento.pas.smartcitywastemanagement.dto.BinDTO;
import it.unisalento.pas.smartcitywastemanagement.service.BinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bins")
public class BinRestController {

    @Autowired
    private BinService binService;

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Bin> createBin(@RequestBody BinDTO binDTO) {
        Bin createdBin = binService.createBin(binDTO);
        return new ResponseEntity<>(createdBin, HttpStatus.CREATED);
    }

    @GetMapping("/")
    public ResponseEntity<List<Bin>> getAllBinLocations() {
        List<Bin> bins = binService.getAllBins();
        return new ResponseEntity<>(bins, HttpStatus.OK);
    }

}
