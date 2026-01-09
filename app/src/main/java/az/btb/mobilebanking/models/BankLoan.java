package az.btb.mobilebanking.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.math.BigDecimal;

public class BankLoan implements Serializable {
    @SerializedName("branchCode")
    @Expose
    private Integer branchCode;
    @SerializedName("branchName")
    @Expose
    private String branchName;
    @SerializedName("loanAccountNumber")
    @Expose
    private String loanAccountNumber;
    @SerializedName("loanSubAccountNumber")
    @Expose
    private Integer loanSubAccountNumber;
    @SerializedName("loanPercent")
    @Expose
    private BigDecimal loanPercent;
    @SerializedName("loanAmount")
    @Expose
    private BigDecimal loanAmount;
    @SerializedName("loanBalance")
    @Expose
    private BigDecimal loanBalance;
    @SerializedName("currency")
    @Expose
    private Integer currency;
    @SerializedName("loanDateOpen")
    @Expose
    private String loanDateOpen;
    @SerializedName("loanDateClose")
    @Expose
    private String loanDateClose;
    @SerializedName("loanDatePlanClose")
    @Expose
    private String loanDatePlanClose;
    @SerializedName("loanDateProlong")
    @Expose
    private String loanDateProlong;
    @SerializedName("loanOverdueBalance")
    @Expose
    private BigDecimal loanOverdueBalance;
    @SerializedName("loanType")
    @Expose
    private String loanType;

    public BankLoan(Integer branchCode, String branchName, String loanAccountNumber, Integer loanSubAccountNumber, BigDecimal loanPercent, BigDecimal loanAmount, BigDecimal loanBalance, Integer currency, String loanDateOpen, String loanDateClose, String loanDatePlanClose, String loanDateProlong, BigDecimal loanOverdueBalance, String loanType) {
        this.branchCode = branchCode;
        this.branchName = branchName;
        this.loanAccountNumber = loanAccountNumber;
        this.loanSubAccountNumber = loanSubAccountNumber;
        this.loanPercent = loanPercent;
        this.loanAmount = loanAmount;
        this.loanBalance = loanBalance;
        this.currency = currency;
        this.loanDateOpen = loanDateOpen;
        this.loanDateClose = loanDateClose;
        this.loanDatePlanClose = loanDatePlanClose;
        this.loanDateProlong = loanDateProlong;
        this.loanOverdueBalance = loanOverdueBalance;
        this.loanType = loanType;
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

    public String getLoanAccountNumber() {
        return loanAccountNumber;
    }

    public void setLoanAccountNumber(String loanAccountNumber) {
        this.loanAccountNumber = loanAccountNumber;
    }

    public Integer getLoanSubAccountNumber() {
        return loanSubAccountNumber;
    }

    public void setLoanSubAccountNumber(Integer loanSubAccountNumber) {
        this.loanSubAccountNumber = loanSubAccountNumber;
    }

    public BigDecimal getLoanPercent() {
        return loanPercent;
    }

    public void setLoanPercent(BigDecimal loanPercent) {
        this.loanPercent = loanPercent;
    }

    public BigDecimal getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(BigDecimal loanAmount) {
        this.loanAmount = loanAmount;
    }

    public BigDecimal getLoanBalance() {
        return loanBalance;
    }

    public void setLoanBalance(BigDecimal loanBalance) {
        this.loanBalance = loanBalance;
    }

    public Integer getCurrency() {
        return currency;
    }

    public void setCurrency(Integer currency) {
        this.currency = currency;
    }

    public String getLoanDateOpen() {
        return loanDateOpen;
    }

    public void setLoanDateOpen(String loanDateOpen) {
        this.loanDateOpen = loanDateOpen;
    }

    public String getLoanDateClose() {
        return loanDateClose;
    }

    public void setLoanDateClose(String loanDateClose) {
        this.loanDateClose = loanDateClose;
    }

    public String getLoanDatePlanClose() {
        return loanDatePlanClose;
    }

    public void setLoanDatePlanClose(String loanDatePlanClose) {
        this.loanDatePlanClose = loanDatePlanClose;
    }

    public String getLoanDateProlong() {
        return loanDateProlong;
    }

    public void setLoanDateProlong(String loanDateProlong) {
        this.loanDateProlong = loanDateProlong;
    }

    public BigDecimal getLoanOverdueBalance() {
        return loanOverdueBalance;
    }

    public void setLoanOverdueBalance(BigDecimal loanOverdueBalance) {
        this.loanOverdueBalance = loanOverdueBalance;
    }

    public String getLoanType() {
        return loanType;
    }

    public void setLoanType(String loanType) {
        this.loanType = loanType;
    }
}
