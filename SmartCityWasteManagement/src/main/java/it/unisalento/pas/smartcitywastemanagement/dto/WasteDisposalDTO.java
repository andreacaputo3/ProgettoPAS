package it.unisalento.pas.smartcitywastemanagement.dto;

import java.math.BigDecimal;
import java.util.Date;

public class WasteDisposalDTO {

    private String id;
    private String binId;
    private String userId;
    private String wasteType;
    private Date disposalDate;
    private BigDecimal weight;
    private Boolean isRecycled;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBinId() {
        return binId;
    }

    public void setBinId(String binId) {
        this.binId = binId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getWasteType() {
        return wasteType;
    }

    public void setWasteType(String wasteType) {
        this.wasteType = wasteType;
    }

    public Date getDisposalDate() {
        return disposalDate;
    }

    public void setDisposalDate(Date disposalDate) {
        this.disposalDate = disposalDate;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public Boolean getRecycled() {
        return isRecycled;
    }

    public void setRecycled(Boolean recycled) {
        isRecycled = recycled;
    }
}
