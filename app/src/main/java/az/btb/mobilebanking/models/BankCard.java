package az.btb.mobilebanking.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.math.BigDecimal;

public class BankCard implements Serializable {
    @SerializedName("branchCode")
    @Expose
    private int branchCode;
    @SerializedName("branchName")
    @Expose
    private String branchName;
    @SerializedName("cardServiceName")
    @Expose
    private String cardServiceName;
    @SerializedName("cardHolderShortName")
    @Expose
    private String cardHolderShortName;
    @SerializedName("idCard")
    @Expose
    private String idCard;
    @SerializedName("currency")
    @Expose
    private int currency;
    @SerializedName("cardNumber")
    @Expose
    private String cardNumber;
    @SerializedName("cardExpiryDate")
    @Expose
    private String cardExpiryDate;
    @SerializedName("cardStatus")
    @Expose
    private int cardStatus;
    @SerializedName("cardBalance")
    @Expose
    private BigDecimal cardBalance;
    @SerializedName("cardAltName")
    @Expose
    private String cardAltName;
    @SerializedName("cardColor")
    @Expose
    private int cardColor;
    @SerializedName("bankCardType")
    @Expose
    private int bankCardType;

    public int getBranchCode() {
        return branchCode;
    }

    public void setBranchCode(Integer branchCode) {
        this.branchCode = branchCode;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getCardServiceName() {
        return cardServiceName;
    }

    public void setCardServiceName(String cardServiceName) {
        this.cardServiceName = cardServiceName;
    }

    public String getCardHolderShortName() {
        return cardHolderShortName;
    }

    public void setCardHolderShortName(String cardHolderShortName) {
        this.cardHolderShortName = cardHolderShortName;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public int getCurrency() {
        return currency;
    }

    public void setCurrency(Integer currency) {
        this.currency = currency;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardExpiryDate() {
        return cardExpiryDate;
    }

    public void setCardExpiryDate(String cardExpiryDate) {
        this.cardExpiryDate = cardExpiryDate;
    }

    public int getCardStatus() {
        return cardStatus;
    }

    public void setCardStatus(Integer cardStatus) {
        this.cardStatus = cardStatus;
    }

    public BigDecimal getCardBalance() {
        return cardBalance;
    }

    public void setCardBalance(BigDecimal cardBalance) {
        this.cardBalance = cardBalance;
    }

    public String getCardAltName() {
        return cardAltName;
    }

    public void setCardAltName(String cardAltName) {
        this.cardAltName = cardAltName;
    }

    public int getCardColor() {
        return cardColor;
    }

    public void setCardColor(Integer cardColor) {
        this.cardColor = cardColor;
    }

    public int getBankCardType() {
        return bankCardType;
    }

    public void setBankCardType(Integer bankCardType) {
        this.bankCardType = bankCardType;
    }

}