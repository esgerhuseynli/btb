package az.btb.mobilebanking.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;

public class MoneyTransferRequest {
    @SerializedName("RequestInfo")
    @Expose
    private RequestInfo requestInfo;
    @SerializedName("PayerInfo")
    @Expose
    private PayerInfo payerInfo;
    @SerializedName("MTPointsType")
    @Expose
    private int mTPointsType;
    @SerializedName("MTUniqueName")
    @Expose
    private String mTUniqueName;
    @SerializedName("CountryISO3Code")
    @Expose
    private String countryISO3Code;
    @SerializedName("IdCity")
    @Expose
    private String idCity;
    @SerializedName("IdPoint")
    @Expose
    private String idPoint;
    @SerializedName("TransferAmount")
    @Expose
    private BigDecimal transferAmount;
    @SerializedName("TransferCurrency")
    @Expose
    private int transferCurrency;
    @SerializedName("TransferReceiverInfo")
    @Expose
    private TransferReceiverInfo transferReceiverInfo;

    public MoneyTransferRequest(RequestInfo requestInfo, PayerInfo payerInfo, int mTPointsType, String mTUniqueName, String countryISO3Code, String idCity, String idPoint, BigDecimal transferAmount, int transferCurrency, TransferReceiverInfo transferReceiverInfo) {
        this.requestInfo = requestInfo;
        this.payerInfo = payerInfo;
        this.mTPointsType = mTPointsType;
        this.mTUniqueName = mTUniqueName;
        this.countryISO3Code = countryISO3Code;
        this.idCity = idCity;
        this.idPoint = idPoint;
        this.transferAmount = transferAmount;
        this.transferCurrency = transferCurrency;
        this.transferReceiverInfo = transferReceiverInfo;
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

    public int getMTPointsType() {
        return mTPointsType;
    }

    public void setMTPointsType(int mTPointsType) {
        this.mTPointsType = mTPointsType;
    }

    public String getMTUniqueName() {
        return mTUniqueName;
    }

    public void setMTUniqueName(String mTUniqueName) {
        this.mTUniqueName = mTUniqueName;
    }

    public String getCountryISO3Code() {
        return countryISO3Code;
    }

    public void setCountryISO3Code(String countryISO3Code) {
        this.countryISO3Code = countryISO3Code;
    }

    public String getIdCity() {
        return idCity;
    }

    public void setIdCity(String idCity) {
        this.idCity = idCity;
    }

    public String getIdPoint() {
        return idPoint;
    }

    public void setIdPoint(String idPoint) {
        this.idPoint = idPoint;
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

    public TransferReceiverInfo getTransferReceiverInfo() {
        return transferReceiverInfo;
    }

    public void setTransferReceiverInfo(TransferReceiverInfo transferReceiverInfo) {
        this.transferReceiverInfo = transferReceiverInfo;
    }
}
