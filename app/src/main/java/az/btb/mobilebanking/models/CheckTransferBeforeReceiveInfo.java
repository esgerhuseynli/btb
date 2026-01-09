package az.btb.mobilebanking.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.math.BigDecimal;

public class CheckTransferBeforeReceiveInfo implements Serializable {
    @SerializedName("mtUniqueName")
    @Expose
    private String mtUniqueName;
    @SerializedName("mtSystemName")
    @Expose
    private String mtSystemName;
    @SerializedName("mtTransferStatus")
    @Expose
    private int mtTransferStatus;
    @SerializedName("transferDate")
    @Expose
    private String transferDate;
    @SerializedName("transferNumber")
    @Expose
    private String transferNumber;
    @SerializedName("transferAmount")
    @Expose
    private BigDecimal transferAmount;
    @SerializedName("transferCurrency")
    @Expose
    private int transferCurrency;

    public CheckTransferBeforeReceiveInfo(String mtUniqueName, String mtSystemName, int mtTransferStatus, String transferDate, String transferNumber, BigDecimal transferAmount, int transferCurrency) {
        this.mtUniqueName = mtUniqueName;
        this.mtSystemName = mtSystemName;
        this.mtTransferStatus = mtTransferStatus;
        this.transferDate = transferDate;
        this.transferNumber = transferNumber;
        this.transferAmount = transferAmount;
        this.transferCurrency = transferCurrency;
    }

    public String getMtUniqueName() {
        return mtUniqueName;
    }

    public void setMtUniqueName(String mtUniqueName) {
        this.mtUniqueName = mtUniqueName;
    }

    public String getMtSystemName() {
        return mtSystemName;
    }

    public void setMtSystemName(String mtSystemName) {
        this.mtSystemName = mtSystemName;
    }

    public int getMtTransferStatus() {
        return mtTransferStatus;
    }

    public void setMtTransferStatus(int mtTransferStatus) {
        this.mtTransferStatus = mtTransferStatus;
    }

    public String getTransferDate() {
        return transferDate;
    }

    public void setTransferDate(String transferDate) {
        this.transferDate = transferDate;
    }

    public String getTransferNumber() {
        return transferNumber;
    }

    public void setTransferNumber(String transferNumber) {
        this.transferNumber = transferNumber;
    }

    public BigDecimal getTransferAmount() {
        return transferAmount;
    }

    public void setTransferAmount(BigDecimal transferAmount) {
        this.transferAmount = transferAmount;
    }

    public int getTransferCurrency() {
        return transferCurrency;
    }

    public void setTransferCurrency(int transferCurrency) {
        this.transferCurrency = transferCurrency;
    }
}
