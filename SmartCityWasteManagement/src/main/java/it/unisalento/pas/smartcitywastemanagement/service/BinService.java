package it.unisalento.pas.smartcitywastemanagement.service;

import it.unisalento.pas.smartcitywastemanagement.domain.Bin;
import it.unisalento.pas.smartcitywastemanagement.dto.BinDTO;
import it.unisalento.pas.smartcitywastemanagement.repositories.BinRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BinService {

    @Autowired
    private BinRepository binRepository;

    public Bin createBin(BinDTO binDTO) {
        Bin bin = new Bin();
        bin.setLocation(binDTO.getLocation());
        bin.setType(binDTO.getType());
        bin.setFull(false); // Default a false quando viene creato un nuovo cassonetto
        bin.setMaxWeight(binDTO.getMaxWeight());

        return binRepository.save(bin);
    }
}
