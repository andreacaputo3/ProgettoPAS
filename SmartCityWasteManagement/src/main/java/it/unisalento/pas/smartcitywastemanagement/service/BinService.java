package it.unisalento.pas.smartcitywastemanagement.service;

import it.unisalento.pas.smartcitywastemanagement.domain.Bin;
import it.unisalento.pas.smartcitywastemanagement.domain.WasteDisposal;
import it.unisalento.pas.smartcitywastemanagement.dto.BinDTO;
import it.unisalento.pas.smartcitywastemanagement.exceptions.BinNotFoundException;
import it.unisalento.pas.smartcitywastemanagement.repositories.BinRepository;
import it.unisalento.pas.smartcitywastemanagement.repositories.WasteDisposalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class BinService {

    @Autowired
    private BinRepository binRepository;

    @Autowired
    private WasteDisposalRepository wasteDisposalRepository;

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

    public void emptyBin(String binId) {
        Optional<Bin> binOptional = binRepository.findById(binId);
        if (binOptional.isPresent()) {
            Bin bin = binOptional.get();
            bin.setFull(false);
            bin.setCurrentWeight(BigDecimal.ZERO);
            binRepository.save(bin);
            // Aggiorna lo stato dei disposal associati al bin
           updateDisposalsAfterBinEmpty(binId);
        } else {
            // Gestire il caso in cui il cassonetto non viene trovato
            throw new BinNotFoundException(binId);
        }
    }

    public void updateDisposalsAfterBinEmpty(String binId) {
        List<WasteDisposal> disposals = wasteDisposalRepository.findByBinIdAndIsRecycledFalse(binId);
        for (WasteDisposal disposal : disposals) {
            disposal.setRecycled(true);
            wasteDisposalRepository.save(disposal);
        }
    }

}
