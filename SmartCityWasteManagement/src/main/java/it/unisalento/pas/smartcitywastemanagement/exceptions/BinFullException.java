package it.unisalento.pas.smartcitywastemanagement.exceptions;

public class BinFullException extends RuntimeException {

    private String binLocation;

    public BinFullException(String binLocation) {
        super("Il bin situato in " + binLocation + " è pieno.");
        this.binLocation = binLocation;
    }

    public String getBinLocation() {
        return binLocation;
    }
}
