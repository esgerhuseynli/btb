package az.btb.mobilebanking.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MoneyTransferReceiverInfo {
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
    @SerializedName("receiveDate")
    @Expose
    private String receiveDate;
    @SerializedName("receiveAmount")
    @Expose
    private int receiveAmount;
    @SerializedName("receiveAmountCurrency")
    @Expose
    private int receiveAmountCurrency;
    @SerializedName("transferNumber")
    @Expose
    private String transferNumber;

    public MoneyTransferReceiverInfo(String mtUniqueName, String mtSystemName, int mtTransferStatus, String transferDate, String receiveDate, int receiveAmount, int receiveAmountCurrency, String transferNumber) {
        this.mtUniqueName = mtUniqueName;
        this.mtSystemName = mtSystemName;
        this.mtTransferStatus = mtTransferStatus;
        this.transferDate = transferDate;
        this.receiveDate = receiveDate;
        this.receiveAmount = receiveAmount;
        this.receiveAmountCurrency = receiveAmountCurrency;
        this.transferNumber = transferNumber;
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

    public String getReceiveDate() {
        return receiveDate;
    }

    public void setReceiveDate(String receiveDate) {
        this.receiveDate = receiveDate;
    }

    public int getReceiveAmount() {
        return receiveAmount;
    }

    public void setReceiveAmount(int receiveAmount) {
        this.receiveAmount = receiveAmount;
    }

    public int getReceiveAmountCurrency() {
        return receiveAmountCurrency;
    }

    public void setReceiveAmountCurrency(int receiveAmountCurrency) {
        this.receiveAmountCurrency = receiveAmountCurrency;
    }

    public String getTransferNumber() {
        return transferNumber;
    }

    public void setTransferNumber(String transferNumber) {
        this.transferNumber = transferNumber;
    }
}
