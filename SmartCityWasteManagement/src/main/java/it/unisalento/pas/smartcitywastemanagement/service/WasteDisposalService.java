package it.unisalento.pas.smartcitywastemanagement.service;

import it.unisalento.pas.smartcitywastemanagement.domain.WasteDisposal;
import it.unisalento.pas.smartcitywastemanagement.dto.UserWasteSeparationPerformanceDTO;
import it.unisalento.pas.smartcitywastemanagement.dto.WasteDisposalDTO;
import it.unisalento.pas.smartcitywastemanagement.repositories.WasteDisposalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WasteDisposalService {

    @Autowired
    private WasteDisposalRepository wasteDisposalRepository;

    public void saveWasteDisposal(WasteDisposalDTO wasteDisposalDTO) {
        WasteDisposal wasteDisposal = new WasteDisposal();
        // Map and set properties from DTO to entity
        wasteDisposal.setBinId(wasteDisposalDTO.getBinId());
        wasteDisposal.setUserId(wasteDisposalDTO.getUserId());
        wasteDisposal.setWasteType(wasteDisposalDTO.getWasteType());
        wasteDisposal.setDisposalDate(wasteDisposalDTO.getDisposalDate());
        wasteDisposal.setRecycled(wasteDisposalDTO.isRecycled());

        wasteDisposalRepository.save(wasteDisposal);
    }

}
