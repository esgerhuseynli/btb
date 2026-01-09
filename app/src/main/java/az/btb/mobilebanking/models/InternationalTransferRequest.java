package az.btb.mobilebanking.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;

public class InternationalTransferRequest {
    @SerializedName("RequestInfo")
    @Expose
    private RequestInfo requestInfo;
    @SerializedName("PayerInfo")
    @Expose
    private InternationalTransferPayerInfo payerInfo;
    @SerializedName("TransferNumber")
    @Expose
    private String transferNumber;
    @SerializedName("ForeignReceiverInfo")
    @Expose
    private ForeignReceiverInfo foreignReceiverInfo;
    @SerializedName("TransferAmount")
    @Expose
    private BigDecimal transferAmount;
    @SerializedName("TransferCurrency")
    @Expose
    private int transferCurrency;
    @SerializedName("OperationDescription")
    @Expose
    private String operationDescription;
    @SerializedName("OperationAdditionalDescription")
    @Expose
    private String operationAdditionalDescription;

    public InternationalTransferRequest(RequestInfo requestInfo, InternationalTransferPayerInfo payerInfo, String transferNumber, ForeignReceiverInfo foreignReceiverInfo, BigDecimal transferAmount, int transferCurrency, String operationDescription, String operationAdditionalDescription) {
        this.requestInfo = requestInfo;
        this.payerInfo = payerInfo;
        this.transferNumber = transferNumber;
        this.foreignReceiverInfo = foreignReceiverInfo;
        this.transferAmount = transferAmount;
        this.transferCurrency = transferCurrency;
        this.operationDescription = operationDescription;
        this.operationAdditionalDescription = operationAdditionalDescription;
    }

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public void setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
    }

    public InternationalTransferPayerInfo getPayerInfo() {
        return payerInfo;
    }

    public void setPayerInfo(InternationalTransferPayerInfo payerInfo) {
        this.payerInfo = payerInfo;
    }

    public String getTransferNumber() {
        return transferNumber;
    }

    public void setTransferNumber(String transferNumber) {
        this.transferNumber = transferNumber;
    }

    public ForeignReceiverInfo getForeignReceiverInfo() {
        return foreignReceiverInfo;
    }

    public void setForeignReceiverInfo(ForeignReceiverInfo foreignReceiverInfo) {
        this.foreignReceiverInfo = foreignReceiverInfo;
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

    public String getOperationDescription() {
        return operationDescription;
    }

    public void setOperationDescription(String operationDescription) {
        this.operationDescription = operationDescription;
    }

    public String getOperationAdditionalDescription() {
        return operationAdditionalDescription;
    }

    public void setOperationAdditionalDescription(String operationAdditionalDescription) {
        this.operationAdditionalDescription = operationAdditionalDescription;
    }
}
