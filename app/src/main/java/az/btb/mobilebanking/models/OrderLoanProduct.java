package az.btb.mobilebanking.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;

public class OrderLoanProduct {
    @SerializedName("ProductId")
    @Expose
    private int productId;
    @SerializedName("LoanAmount")
    @Expose
    private BigDecimal loanAmount;
    @SerializedName("LoanTerm")
    @Expose
    private int loanTerm;
    @SerializedName("LoanPercent")
    @Expose
    private float loanPercent;
    @SerializedName("LoanTarget")
    @Expose
    private String loanTarget;
    @SerializedName("Currency")
    @Expose
    private int selectedCurrency;

    public OrderLoanProduct(int productId, BigDecimal loanAmount, int loanTerm, float loanPercent, String loanTarget, int selectedCurrency) {
        this.productId = productId;
        this.loanAmount = loanAmount;
        this.loanTerm = loanTerm;
        this.loanPercent = loanPercent;
        this.loanTarget = loanTarget;
        this.selectedCurrency = selectedCurrency;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public BigDecimal getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(BigDecimal loanAmount) {
        this.loanAmount = loanAmount;
    }

    public int getLoanTerm() {
        return loanTerm;
    }

    public void setLoanTerm(int loanTerm) {
        this.loanTerm = loanTerm;
    }

    public float getLoanPercent() {
        return loanPercent;
    }

    public void setLoanPercent(float loanPercent) {
        this.loanPercent = loanPercent;
    }

    public String getLoanTarget() {
        return loanTarget;
    }

    public void setLoanTarget(String loanTarget) {
        this.loanTarget = loanTarget;
    }

    public int getSelectedCurrency() {
        return selectedCurrency;
    }

    public void setSelectedCurrency(int selectedCurrency) {
        this.selectedCurrency = selectedCurrency;
    }
}
