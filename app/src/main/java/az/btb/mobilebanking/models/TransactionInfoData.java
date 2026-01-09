package az.btb.mobilebanking.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;

public class TransactionInfoData {
    @SerializedName("transactionType")
    @Expose
    private Integer transactionType;
    @SerializedName("transactionID")
    @Expose
    private Integer transactionID;
    @SerializedName("amountInLC")
    @Expose
    private BigDecimal amountInLC;
    @SerializedName("amountInFC")
    @Expose
    private BigDecimal amountInFC;
    @SerializedName("currency")
    @Expose
    private Integer currency;

    public TransactionInfoData(Integer transactionType, Integer transactionID, BigDecimal amountInLC, BigDecimal amountInFC, Integer currency) {
        this.transactionType = transactionType;
        this.transactionID = transactionID;
        this.amountInLC = amountInLC;
        this.amountInFC = amountInFC;
        this.currency = currency;
    }

    public Integer getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(Integer transactionType) {
        this.transactionType = transactionType;
    }

    public Integer getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(Integer transactionID) {
        this.transactionID = transactionID;
    }

    public BigDecimal getAmountInLC() {
        return amountInLC;
    }

    public void setAmountInLC(BigDecimal amountInLC) {
        this.amountInLC = amountInLC;
    }

    public BigDecimal getAmountInFC() {
        return amountInFC;
    }

    public void setAmountInFC(BigDecimal amountInFC) {
        this.amountInFC = amountInFC;
    }

    public Integer getCurrency() {
        return currency;
    }

    public void setCurrency(Integer currency) {
        this.currency = currency;
    }
}
