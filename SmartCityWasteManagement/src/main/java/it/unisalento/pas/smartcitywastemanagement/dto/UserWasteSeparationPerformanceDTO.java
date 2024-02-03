package it.unisalento.pas.smartcitywastemanagement.dto;

import java.util.Map;

public class UserWasteSeparationPerformanceDTO {
    private int totalDisposals;
    private Map<String, Integer> wasteTypeCounts; // Tipo di rifiuto -> Numero di conferimenti per tipo
    private int incorrectDisposalCount;

    public int getTotalDisposals() {
        return totalDisposals;
    }

    public void setTotalDisposals(int totalDisposals) {
        this.totalDisposals = totalDisposals;
    }

    public Map<String, Integer> getWasteTypeCounts() {
        return wasteTypeCounts;
    }

    public void setWasteTypeCounts(Map<String, Integer> wasteTypeCounts) {
        this.wasteTypeCounts = wasteTypeCounts;
    }

    public int getIncorrectDisposalCount() {
        return incorrectDisposalCount;
    }

    public void setIncorrectDisposalCount(int incorrectDisposalCount) {
        this.incorrectDisposalCount = incorrectDisposalCount;
    }
}
