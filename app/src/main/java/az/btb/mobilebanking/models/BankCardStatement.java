package az.btb.mobilebanking.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;

public class BankCardStatement {

    @SerializedName("operationDate")
    @Expose
    private String operationDate;
    @SerializedName("cardNumber")
    @Expose
    private String cardNumber;
    @SerializedName("paymentType")
    @Expose
    private int paymentType;
    @SerializedName("amount")
    @Expose
    private BigDecimal amount;
    @SerializedName("currency")
    @Expose
    private int currency;
    @SerializedName("amountBilling")
    @Expose
    private BigDecimal amountBilling;
    @SerializedName("currencyBilling")
    @Expose
    private int currencyBilling;
    @SerializedName("operationDescription")
    @Expose
    private String operationDescription;
    @SerializedName("countryCode")
    @Expose
    private String countryCode;

    public BankCardStatement(String operationDate, String cardNumber, int paymentType, BigDecimal amount, int currency, BigDecimal amountBilling, int currencyBilling, String operationDescription, String countryCode) {
        this.operationDate = operationDate;
        this.cardNumber = cardNumber;
        this.paymentType = paymentType;
        this.amount = amount;
        this.currency = currency;
        this.amountBilling = amountBilling;
        this.currencyBilling = currencyBilling;
        this.operationDescription = operationDescription;
        this.countryCode = countryCode;
    }

    public String getOperationDate() {
        return operationDate;
    }

    public void setOperationDate(String operationDate) {
        this.operationDate = operationDate;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public int getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(int paymentType) {
        this.paymentType = paymentType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public int getCurrency() {
        return currency;
    }

    public void setCurrency(int currency) {
        this.currency = currency;
    }

    public BigDecimal getAmountBilling() {
        return amountBilling;
    }

    public void setAmountBilling(BigDecimal amountBilling) {
        this.amountBilling = amountBilling;
    }

    public int getCurrencyBilling() {
        return currencyBilling;
    }

    public void setCurrencyBilling(int currencyBilling) {
        this.currencyBilling = currencyBilling;
    }

    public String getOperationDescription() {
        return operationDescription;
    }

    public void setOperationDescription(String operationDescription) {
        this.operationDescription = operationDescription;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }
}
