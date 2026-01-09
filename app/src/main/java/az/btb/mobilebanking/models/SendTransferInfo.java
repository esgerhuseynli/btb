package az.btb.mobilebanking.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;

public class SendTransferInfo {
    @SerializedName("mtUniqueName")
    @Expose
    private String mtUniqueName;
    @SerializedName("mtSystemName")
    @Expose
    private String mtSystemName;
    @SerializedName("mtTransferStatus")
    @Expose
    private int mtTransferStatus;
    @SerializedName("countryName")
    @Expose
    private String countryName;
    @SerializedName("countryISO3Code")
    @Expose
    private String countryISO3Code;
    @SerializedName("transferAmount")
    @Expose
    private BigDecimal transferAmount;
    @SerializedName("transferCurrency")
    @Expose
    private int transferCurrency;
    @SerializedName("transferNumber")
    @Expose
    private String transferNumber;
    @SerializedName("calculatedCommission")
    @Expose
    private BigDecimal calculatedCommission;
    @SerializedName("calculatedCommissionCurrency")
    @Expose
    private int calculatedCommissionCurrency;

    public SendTransferInfo(String mtUniqueName, String mtSystemName, int mtTransferStatus, String countryName, String countryISO3Code, BigDecimal transferAmount, int transferCurrency, String transferNumber, BigDecimal calculatedCommission, int calculatedCommissionCurrency) {
        this.mtUniqueName = mtUniqueName;
        this.mtSystemName = mtSystemName;
        this.mtTransferStatus = mtTransferStatus;
        this.countryName = countryName;
        this.countryISO3Code = countryISO3Code;
        this.transferAmount = transferAmount;
        this.transferCurrency = transferCurrency;
        this.transferNumber = transferNumber;
        this.calculatedCommission = calculatedCommission;
        this.calculatedCommissionCurrency = calculatedCommissionCurrency;
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

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCountryISO3Code() {
        return countryISO3Code;
    }

    public void setCountryISO3Code(String countryISO3Code) {
        this.countryISO3Code = countryISO3Code;
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

    public String getTransferNumber() {
        return transferNumber;
    }

    public void setTransferNumber(String transferNumber) {
        this.transferNumber = transferNumber;
    }

    public BigDecimal getCalculatedCommission() {
        return calculatedCommission;
    }

    public void setCalculatedCommission(BigDecimal calculatedCommission) {
        this.calculatedCommission = calculatedCommission;
    }

    public int getCalculatedCommissionCurrency() {
        return calculatedCommissionCurrency;
    }

    public void setCalculatedCommissionCurrency(int calculatedCommissionCurrency) {
        this.calculatedCommissionCurrency = calculatedCommissionCurrency;
    }
}
