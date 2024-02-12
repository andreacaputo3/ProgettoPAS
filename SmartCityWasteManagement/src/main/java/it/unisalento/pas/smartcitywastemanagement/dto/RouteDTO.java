package it.unisalento.pas.smartcitywastemanagement.dto;


public class RouteDTO {
    private String binId;
    private int position;

    public String getBinId() {
        return binId;
    }

    public void setBinId(String binId) {
        this.binId = binId;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
