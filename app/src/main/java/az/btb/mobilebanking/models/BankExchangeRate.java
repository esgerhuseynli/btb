package az.btb.mobilebanking.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;

public class BankExchangeRate {
    @SerializedName("currency")
    @Expose
    private Integer currency;
    @SerializedName("exchangeRate")
    @Expose
    private BigDecimal exchangeRate;
    @SerializedName("rateDate")
    @Expose
    private String rateDate;
    @SerializedName("rateType")
    @Expose
    private Integer rateType;
    @SerializedName("rateForCash")
    @Expose
    private BigDecimal rateForCash;
    @SerializedName("rateForNonCash")
    @Expose
    private BigDecimal rateForNonCash;

    public BankExchangeRate(Integer currency, BigDecimal exchangeRate, String rateDate, Integer rateType, BigDecimal rateForCash, BigDecimal rateForNonCash) {
        this.currency = currency;
        this.exchangeRate = exchangeRate;
        this.rateDate = rateDate;
        this.rateType = rateType;
        this.rateForCash = rateForCash;
        this.rateForNonCash = rateForNonCash;
    }

    public Integer getCurrency() {
        return currency;
    }

    public void setCurrency(Integer currency) {
        this.currency = currency;
    }

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public String getRateDate() {
        return rateDate;
    }

    public void setRateDate(String rateDate) {
        this.rateDate = rateDate;
    }

    public Integer getRateType() {
        return rateType;
    }

    public void setRateType(Integer rateType) {
        this.rateType = rateType;
    }

    public BigDecimal getRateForCash() {
        return rateForCash;
    }

    public void setRateForCash(BigDecimal rateForCash) {
        this.rateForCash = rateForCash;
    }

    public BigDecimal getRateForNonCash() {
        return rateForNonCash;
    }

    public void setRateForNonCash(BigDecimal rateForNonCash) {
        this.rateForNonCash = rateForNonCash;
    }
}
