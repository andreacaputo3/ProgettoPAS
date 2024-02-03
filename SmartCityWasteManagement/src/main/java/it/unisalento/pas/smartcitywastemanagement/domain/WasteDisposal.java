package it.unisalento.pas.smartcitywastemanagement.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.util.Date;
@Document("disposals")
public class WasteDisposal {

    @Id
    private String id;
    @Field("binId")
    private String binId;
    @Field("userId")
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
