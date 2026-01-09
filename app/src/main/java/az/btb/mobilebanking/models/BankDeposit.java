package az.btb.mobilebanking.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.math.BigDecimal;

public class BankDeposit implements Serializable {
    @SerializedName("branchCode")
    @Expose
    private Integer branchCode;
    @SerializedName("branchName")
    @Expose
    private String branchName;
    @SerializedName("depositAccountNumber")
    @Expose
    private String depositAccountNumber;
    @SerializedName("depositSubAccountNumber")
    @Expose
    private Integer depositSubAccountNumber;
    @SerializedName("depositPercent")
    @Expose
    private BigDecimal depositPercent;
    @SerializedName("depositAmount")
    @Expose
    private BigDecimal depositAmount;
    @SerializedName("currency")
    @Expose
    private Integer currency;
    @SerializedName("depositDateOpen")
    @Expose
    private String depositDateOpen;
    @SerializedName("depositDateClose")
    @Expose
    private String depositDateClose;
    @SerializedName("depositDatePlanClose")
    @Expose
    private String depositDatePlanClose;
    @SerializedName("depositDateProlong")
    @Expose
    private String depositDateProlong;
    @SerializedName("depositPercentAmount")
    @Expose
    private BigDecimal depositPercentAmount;
    @SerializedName("depositType")
    @Expose
    private String depositType;
    @SerializedName("depositContractNumber")
    @Expose
    private String depositContractNumber;
    @SerializedName("depositPercentPaymentDays")
    @Expose
    private Integer depositPercentPaymentDays;

    public BankDeposit(Integer branchCode, String branchName, String depositAccountNumber, Integer depositSubAccountNumber, BigDecimal depositPercent, BigDecimal depositAmount, Integer currency, String depositDateOpen, String depositDateClose, String depositDatePlanClose, String depositDateProlong, BigDecimal depositPercentAmount, String depositType, String depositContractNumber, Integer depositPercentPaymentDays) {
        this.branchCode = branchCode;
        this.branchName = branchName;
        this.depositAccountNumber = depositAccountNumber;
        this.depositSubAccountNumber = depositSubAccountNumber;
        this.depositPercent = depositPercent;
        this.depositAmount = depositAmount;
        this.currency = currency;
        this.depositDateOpen = depositDateOpen;
        this.depositDateClose = depositDateClose;
        this.depositDatePlanClose = depositDatePlanClose;
        this.depositDateProlong = depositDateProlong;
        this.depositPercentAmount = depositPercentAmount;
        this.depositType = depositType;
        this.depositContractNumber = depositContractNumber;
        this.depositPercentPaymentDays = depositPercentPaymentDays;
    }

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

    public String getDepositAccountNumber() {
        return depositAccountNumber;
    }

    public void setDepositAccountNumber(String depositAccountNumber) {
        this.depositAccountNumber = depositAccountNumber;
    }

    public Integer getDepositSubAccountNumber() {
        return depositSubAccountNumber;
    }

    public void setDepositSubAccountNumber(Integer depositSubAccountNumber) {
        this.depositSubAccountNumber = depositSubAccountNumber;
    }

    public BigDecimal getDepositPercent() {
        return depositPercent;
    }

    public void setDepositPercent(BigDecimal depositPercent) {
        this.depositPercent = depositPercent;
    }

    public BigDecimal getDepositAmount() {
        return depositAmount;
    }

    public void setDepositAmount(BigDecimal depositAmount) {
        this.depositAmount = depositAmount;
    }

    public Integer getCurrency() {
        return currency;
    }

    public void setCurrency(Integer currency) {
        this.currency = currency;
    }

    public String getDepositDateOpen() {
        return depositDateOpen;
    }

    public void setDepositDateOpen(String depositDateOpen) {
        this.depositDateOpen = depositDateOpen;
    }

    public String getDepositDateClose() {
        return depositDateClose;
    }

    public void setDepositDateClose(String depositDateClose) {
        this.depositDateClose = depositDateClose;
    }

    public String getDepositDatePlanClose() {
        return depositDatePlanClose;
    }

    public void setDepositDatePlanClose(String depositDatePlanClose) {
        this.depositDatePlanClose = depositDatePlanClose;
    }

    public String getDepositDateProlong() {
        return depositDateProlong;
    }

    public void setDepositDateProlong(String depositDateProlong) {
        this.depositDateProlong = depositDateProlong;
    }

    public BigDecimal getDepositPercentAmount() {
        return depositPercentAmount;
    }

    public void setDepositPercentAmount(BigDecimal depositPercentAmount) {
        this.depositPercentAmount = depositPercentAmount;
    }

    public String getDepositType() {
        return depositType;
    }

    public void setDepositType(String depositType) {
        this.depositType = depositType;
    }

    public String getDepositContractNumber() {
        return depositContractNumber;
    }

    public void setDepositContractNumber(String depositContractNumber) {
        this.depositContractNumber = depositContractNumber;
    }

    public Integer getDepositPercentPaymentDays() {
        return depositPercentPaymentDays;
    }

    public void setDepositPercentPaymentDays(Integer depositPercentPaymentDays) {
        this.depositPercentPaymentDays = depositPercentPaymentDays;
    }
}
