package it.unisalento.pas.smartcitywastemanagement.domain;

public class WasteSeparationPerformance {
    private int totalWaste;
    private int recycledWaste;
    private double recyclingPercentage;
    private int recyclableWaste;


    public int getTotalWaste() {
        return totalWaste;
    }

    public void setTotalWaste(int totalWaste) {
        this.totalWaste = totalWaste;
    }

    public int getRecycledWaste() {
        return recycledWaste;
    }

    public void setRecycledWaste(int recycledWaste) {
        this.recycledWaste = recycledWaste;
    }

    public double getRecyclingPercentage() {
        return recyclingPercentage;
    }

    public void setRecyclingPercentage(double recyclingPercentage) {
        this.recyclingPercentage = recyclingPercentage;
    }

    public int getRecyclableWaste() {
        return recyclableWaste;
    }

    public void setRecyclableWaste(int recyclableWaste) {
        this.recyclableWaste = recyclableWaste;
    }
}
