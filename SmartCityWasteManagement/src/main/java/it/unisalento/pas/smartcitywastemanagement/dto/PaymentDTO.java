package it.unisalento.pas.smartcitywastemanagement.dto;

import java.math.BigDecimal;
import java.util.Date;

public class PaymentDTO {

    private String id;
    private BigDecimal amount;
    private Date paymentDate;
    private boolean paid;

    public PaymentDTO(String id, BigDecimal amount, Date paymentDate, boolean paid) {
        this.id = id;
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.paid = paid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

}
