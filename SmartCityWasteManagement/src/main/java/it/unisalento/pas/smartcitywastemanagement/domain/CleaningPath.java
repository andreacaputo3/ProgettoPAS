package it.unisalento.pas.smartcitywastemanagement.domain;

public class CleaningPath {

    private Bin startBin;
    private Bin endBin;
    private double distance; // Distanza del percorso

    public Bin getStartBin() {
        return startBin;
    }

    public void setStartBin(Bin startBin) {
        this.startBin = startBin;
    }

    public Bin getEndBin() {
        return endBin;
    }

    public void setEndBin(Bin endBin) {
        this.endBin = endBin;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
}
