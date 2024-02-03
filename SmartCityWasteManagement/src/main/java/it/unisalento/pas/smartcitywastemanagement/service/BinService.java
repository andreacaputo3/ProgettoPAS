package it.unisalento.pas.smartcitywastemanagement.service;

import it.unisalento.pas.smartcitywastemanagement.domain.Bin;
import it.unisalento.pas.smartcitywastemanagement.dto.BinDTO;
import it.unisalento.pas.smartcitywastemanagement.repositories.BinRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class BinService {

    @Autowired
    private BinRepository binRepository;

    public Bin createBin(BinDTO binDTO) {
        Bin bin = new Bin();
        bin.setLocation(binDTO.getLocation());
        bin.setType(binDTO.getType());
        bin.setFull(false);
        bin.setMaxWeight(binDTO.getMaxWeight());
        bin.setCurrentWeight(BigDecimal.valueOf(0.0));
        bin.setLatitude(binDTO.getLatitude());
        bin.setLongitude(binDTO.getLongitude());

        return binRepository.save(bin);
    }

    public List<Bin> getAllBins() {
        return binRepository.findAll();
    }

}
