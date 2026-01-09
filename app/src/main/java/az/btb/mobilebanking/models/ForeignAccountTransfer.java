package az.btb.mobilebanking.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ForeignAccountTransfer implements Serializable {
    @SerializedName("operationDate")
    @Expose
    private String operationDate;
    @SerializedName("payerInfo")
    @Expose
    private PayerInfo payerInfo;
    @SerializedName("transferNumber")
    @Expose
    private String transferNumber;
    @SerializedName("foreignReceiverInfo")
    @Expose
    private ForeignReceiverInfoHistory foreignReceiverInfo;
    @SerializedName("transferAmount")
    @Expose
    private int transferAmount;
    @SerializedName("transferCurrency")
    @Expose
    private int transferCurrency;
    @SerializedName("operationDescription")
    @Expose
    private String operationDescription;
    @SerializedName("operationAdditinalDescription")
    @Expose
    private String operationAdditinalDescription;
    @SerializedName("foreignAccountTransferStatus")
    @Expose
    private int foreignAccountTransferStatus;
    @SerializedName("commentsBankOperator")
    @Expose
    private String commentsBankOperator;
    @SerializedName("idAccountPaymentTemplate")
    @Expose
    private int idAccountPaymentTemplate;

    public String getOperationDate() {
        return operationDate;
    }

    public void setOperationDate(String operationDate) {
        this.operationDate = operationDate;
    }

    public PayerInfo getPayerInfo() {
        return payerInfo;
    }

    public void setPayerInfo(PayerInfo payerInfo) {
        this.payerInfo = payerInfo;
    }

    public String getTransferNumber() {
        return transferNumber;
    }

    public void setTransferNumber(String transferNumber) {
        this.transferNumber = transferNumber;
    }

    public ForeignReceiverInfoHistory getForeignReceiverInfo() {
        return foreignReceiverInfo;
    }

    public void setForeignReceiverInfo(ForeignReceiverInfoHistory foreignReceiverInfo) {
        this.foreignReceiverInfo = foreignReceiverInfo;
    }

    public int getTransferAmount() {
        return transferAmount;
    }

    public void setTransferAmount(int transferAmount) {
        this.transferAmount = transferAmount;
    }

    public int getTransferCurrency() {
        return transferCurrency;
    }

    public void setTransferCurrency(int transferCurrency) {
        this.transferCurrency = transferCurrency;
    }

    public String getOperationDescription() {
        return operationDescription;
    }

    public void setOperationDescription(String operationDescription) {
        this.operationDescription = operationDescription;
    }

    public String getOperationAdditinalDescription() {
        return operationAdditinalDescription;
    }

    public void setOperationAdditinalDescription(String operationAdditinalDescription) {
        this.operationAdditinalDescription = operationAdditinalDescription;
    }

    public int getForeignAccountTransferStatus() {
        return foreignAccountTransferStatus;
    }

    public void setForeignAccountTransferStatus(int foreignAccountTransferStatus) {
        this.foreignAccountTransferStatus = foreignAccountTransferStatus;
    }

    public String getCommentsBankOperator() {
        return commentsBankOperator;
    }

    public void setCommentsBankOperator(String commentsBankOperator) {
        this.commentsBankOperator = commentsBankOperator;
    }

    public int getIdAccountPaymentTemplate() {
        return idAccountPaymentTemplate;
    }

    public void setIdAccountPaymentTemplate(int idAccountPaymentTemplate) {
        this.idAccountPaymentTemplate = idAccountPaymentTemplate;
    }
}
