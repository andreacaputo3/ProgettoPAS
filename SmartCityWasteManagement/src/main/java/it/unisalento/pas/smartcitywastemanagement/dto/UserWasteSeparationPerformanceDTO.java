package it.unisalento.pas.smartcitywastemanagement.dto;

import java.util.Map;

public class UserWasteSeparationPerformanceDTO {
    private String userId;
    private int totalDisposals;
    private Map<String, Integer> wasteTypeCounts; // Tipo di rifiuto -> Numero di conferimenti per tipo
    private int recyclableWasteCount;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

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

    public int getRecyclableWasteCount() {
        return recyclableWasteCount;
    }

    public void setRecyclableWasteCount(int recyclableWasteCount) {
        this.recyclableWasteCount = recyclableWasteCount;
    }
}
