package it.unisalento.pas.smartcitywastemanagement.exceptions;

public class BinWasteTypeMismatchException extends RuntimeException {

    private final String binLocation;
    private final String binType;
    private final String wasteType;

    public BinWasteTypeMismatchException(String binLocation, String binType, String wasteType) {
        super("Il tipo di rifiuto dello smaltimento non corrisponde al tipo di rifiuto del cassonetto a " + binLocation +
                ". Tipo di rifiuto del cassonetto: " + binType + ", Tipo di rifiuto dello smaltimento: " + wasteType);
        this.binLocation = binLocation;
        this.binType = binType;
        this.wasteType = wasteType;
    }

    public String getBinLocation() {
        return binLocation;
    }

    public String getBinType() {
        return binType;
    }

    public String getWasteType() {
        return wasteType;
    }
}
