package az.btb.mobilebanking.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;

public class MoneyTransferReceiveRequest {
    @SerializedName("RequestInfo")
    @Expose
    private RequestInfo requestInfo;
    @SerializedName("PayerInfo")
    @Expose
    private PayerInfo payerInfo;
    @SerializedName("MTUniqueName")
    @Expose
    private String mTUniqueName;
    @SerializedName("TransferNumber")
    @Expose
    private String transferNumber;
    @SerializedName("TransferAmount")
    @Expose
    private BigDecimal transferAmount;
    @SerializedName("TransferCurrency")
    @Expose
    private int transferCurrency;

    public MoneyTransferReceiveRequest(RequestInfo requestInfo, PayerInfo payerInfo, String mTUniqueName, String transferNumber, BigDecimal transferAmount, int transferCurrency) {
        this.requestInfo = requestInfo;
        this.payerInfo = payerInfo;
        this.mTUniqueName = mTUniqueName;
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

    public PayerInfo getPayerInfo() {
        return payerInfo;
    }

    public void setPayerInfo(PayerInfo payerInfo) {
        this.payerInfo = payerInfo;
    }

    public String getMTUniqueName() {
        return mTUniqueName;
    }

    public void setMTUniqueName(String mTUniqueName) {
        this.mTUniqueName = mTUniqueName;
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
