package az.btb.mobilebanking.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;

public class SendTransferCustomerCommission {
    @SerializedName("mtUniqueName")
    @Expose
    private String mtUniqueName;
    @SerializedName("mtSystemName")
    @Expose
    private String mtSystemName;
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
    @SerializedName("calculatedCommission")
    @Expose
    private BigDecimal calculatedCommission;
    @SerializedName("calculatedCommissionCurrency")
    @Expose
    private int calculatedCommissionCurrency;
    @SerializedName("minTransferSum")
    @Expose
    private BigDecimal minTransferSum;
    @SerializedName("maxTransferSum")
    @Expose
    private BigDecimal maxTransferSum;

    public SendTransferCustomerCommission(String mtUniqueName, String mtSystemName, String countryName, String countryISO3Code, BigDecimal transferAmount, int transferCurrency, BigDecimal calculatedCommission, int calculatedCommissionCurrency, BigDecimal minTransferSum, BigDecimal maxTransferSum) {
        this.mtUniqueName = mtUniqueName;
        this.mtSystemName = mtSystemName;
        this.countryName = countryName;
        this.countryISO3Code = countryISO3Code;
        this.transferAmount = transferAmount;
        this.transferCurrency = transferCurrency;
        this.calculatedCommission = calculatedCommission;
        this.calculatedCommissionCurrency = calculatedCommissionCurrency;
        this.minTransferSum = minTransferSum;
        this.maxTransferSum = maxTransferSum;
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

    public BigDecimal getMinTransferSum() {
        return minTransferSum;
    }

    public void setMinTransferSum(BigDecimal minTransferSum) {
        this.minTransferSum = minTransferSum;
    }

    public BigDecimal getMaxTransferSum() {
        return maxTransferSum;
    }

    public void setMaxTransferSum(BigDecimal maxTransferSum) {
        this.maxTransferSum = maxTransferSum;
    }
}
