package az.btb.mobilebanking.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.math.BigDecimal;

public class BankAccount implements Serializable {
    @SerializedName("branchCode")
    @Expose
    private Integer branchCode;
    @SerializedName("branchName")
    @Expose
    private String branchName;
    @SerializedName("accountNumber")
    @Expose
    private String accountNumber;
    @SerializedName("ibanAccount")
    @Expose
    private String ibanAccount;
    @SerializedName("accountName")
    @Expose
    private String accountName;
    @SerializedName("accountAltName")
    @Expose
    private String accountAltName;
    @SerializedName("accountColor")
    @Expose
    private Integer accountColor;
    @SerializedName("currency")
    @Expose
    private Integer currency;
    @SerializedName("balanceInLC")
    @Expose
    private BigDecimal balanceInLC;
    @SerializedName("balanceInFC")
    @Expose
    private BigDecimal balanceInFC;
    @SerializedName("dateOpen")
    @Expose
    private String dateOpen;
    @SerializedName("dateClose")
    @Expose
    private String dateClose;
    @SerializedName("dateCloseFromTaxes")
    @Expose
    private String dateCloseFromTaxes;
    @SerializedName("dateCloseFromPension")
    @Expose
    private String dateCloseFromPension;
    @SerializedName("dateCloseFromJustice")
    @Expose
    private String dateCloseFromJustice;

    public Integer getBranchCode() {
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

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getIbanAccount() {
        return ibanAccount;
    }

    public void setIbanAccount(String ibanAccount) {
        this.ibanAccount = ibanAccount;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountAltName() {
        return accountAltName;
    }

    public void setAccountAltName(String accountAltName) {
        this.accountAltName = accountAltName;
    }

    public Integer getAccountColor() {
        return accountColor;
    }

    public void setAccountColor(Integer accountColor) {
        this.accountColor = accountColor;
    }

    public Integer getCurrency() {
        return currency;
    }

    public void setCurrency(Integer currency) {
        this.currency = currency;
    }

    public BigDecimal getBalanceInLC() {
        return balanceInLC;
    }

    public void setBalanceInLC(BigDecimal balanceInLC) {
        this.balanceInLC = balanceInLC;
    }

    public BigDecimal getBalanceInFC() {
        return balanceInFC;
    }

    public void setBalanceInFC(BigDecimal balanceInFC) {
        this.balanceInFC = balanceInFC;
    }

    public String getDateOpen() {
        return dateOpen;
    }

    public void setDateOpen(String dateOpen) {
        this.dateOpen = dateOpen;
    }

    public String getDateClose() {
        return dateClose;
    }

    public void setDateClose(String dateClose) {
        this.dateClose = dateClose;
    }

    public String getDateCloseFromTaxes() {
        return dateCloseFromTaxes;
    }

    public void setDateCloseFromTaxes(String dateCloseFromTaxes) {
        this.dateCloseFromTaxes = dateCloseFromTaxes;
    }

    public String getDateCloseFromPension() {
        return dateCloseFromPension;
    }

    public void setDateCloseFromPension(String dateCloseFromPension) {
        this.dateCloseFromPension = dateCloseFromPension;
    }

    public String getDateCloseFromJustice() {
        return dateCloseFromJustice;
    }

    public void setDateCloseFromJustice(String dateCloseFromJustice) {
        this.dateCloseFromJustice = dateCloseFromJustice;
    }
}
