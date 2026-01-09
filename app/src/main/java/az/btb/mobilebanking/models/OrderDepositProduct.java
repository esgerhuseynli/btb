package az.btb.mobilebanking.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;

public class OrderDepositProduct {
    @SerializedName("ProductId")
    @Expose
    private int productId;
    @SerializedName("DepositAmount")
    @Expose
    private BigDecimal depositAmount;
    @SerializedName("DepositTerm")
    @Expose
    private int depositTerm;
    @SerializedName("DepositPercent")
    @Expose
    private float depositPercent;
    @SerializedName("Currency")
    @Expose
    private int selectedCurrency;

    public OrderDepositProduct(int productId, BigDecimal depositAmount, int depositTerm, float depositPercent, int selectedCurrency) {
        this.productId = productId;
        this.depositAmount = depositAmount;
        this.depositTerm = depositTerm;
        this.depositPercent = depositPercent;
        this.selectedCurrency = selectedCurrency;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public BigDecimal getDepositAmount() {
        return depositAmount;
    }

    public void setDepositAmount(BigDecimal depositAmount) {
        this.depositAmount = depositAmount;
    }

    public int getDepositTerm() {
        return depositTerm;
    }

    public void setDepositTerm(int depositTerm) {
        this.depositTerm = depositTerm;
    }

    public float getDepositPercent() {
        return depositPercent;
    }

    public void setDepositPercent(float depositPercent) {
        this.depositPercent = depositPercent;
    }

    public int getSelectedCurrency() {
        return selectedCurrency;
    }

    public void setSelectedCurrency(int selectedCurrency) {
        this.selectedCurrency = selectedCurrency;
    }
}
