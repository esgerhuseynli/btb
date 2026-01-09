package az.btb.mobilebanking.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;

public class MoneyTransferReceiveCheckRequest {
    @SerializedName("RequestInfo")
    @Expose
    private RequestInfo requestInfo;
    @SerializedName("TransferNumber")
    @Expose
    private String transferNumber;
    @SerializedName("TransferAmount")
    @Expose
    private BigDecimal transferAmount;
    @SerializedName("TransferCurrency")
    @Expose
    private int transferCurrency;

    public MoneyTransferReceiveCheckRequest(RequestInfo requestInfo, String transferNumber, BigDecimal transferAmount, int transferCurrency) {
        this.requestInfo = requestInfo;
        this.transferNumber = transferNumber;
        this.transferAmount = transferAmount;
        this.transferCurrency = transferCurrency;
    }

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public void setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
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
