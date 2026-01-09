package az.btb.mobilebanking.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.math.BigDecimal;

public class ProductOrder implements Serializable {
    @SerializedName("idOrder")
    @Expose
    private int idOrder;
    @SerializedName("orderDate")
    @Expose
    private String orderDate;
    @SerializedName("orderStatus")
    @Expose
    private int orderStatus;
    @SerializedName("idProduct")
    @Expose
    private int idProduct;
    @SerializedName("productType")
    @Expose
    private int productType;
    @SerializedName("productName")
    @Expose
    private String productName;
    @SerializedName("productHeaderName")
    @Expose
    private String productHeaderName;
    @SerializedName("loanAmount")
    @Expose
    private BigDecimal loanAmount;
    @SerializedName("loanPercent")
    @Expose
    private float loanPercent;
    @SerializedName("loanTarget")
    @Expose
    private String loanTarget;
    @SerializedName("loanTerm")
    @Expose
    private int loanTerm;
    @SerializedName("depositAmount")
    @Expose
    private BigDecimal depositAmount;
    @SerializedName("depositPercent")
    @Expose
    private float depositPercent;
    @SerializedName("depositTerm")
    @Expose
    private int depositTerm;
    @SerializedName("plasticCardCurrency")
    @Expose
    private int plasticCardCurrency;
    @SerializedName("plasticCardTerm")
    @Expose
    private int plasticCardTerm;
    @SerializedName("embasyReferenceTerm")
    @Expose
    private int embassyReferenceTerm;
    @SerializedName("financialReferenceTerm")
    @Expose
    private int financialReferenceTerm;
    @SerializedName("orderType")
    @Expose
    private int orderType;
    @SerializedName("payerInfo")
    @Expose
    private ProductOrderPayerInfo payerInfo;

    public int getIdOrder() {
        return idOrder;
    }

    public void setIdOrder(int idOrder) {
        this.idOrder = idOrder;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }

    public int getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(int idProduct) {
        this.idProduct = idProduct;
    }

    public int getProductType() {
        return productType;
    }

    public void setProductType(int productType) {
        this.productType = productType;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductHeaderName() {
        return productHeaderName;
    }

    public void setProductHeaderName(String productHeaderName) {
        this.productHeaderName = productHeaderName;
    }

    public BigDecimal getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(BigDecimal loanAmount) {
        this.loanAmount = loanAmount;
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

    public int getLoanTerm() {
        return loanTerm;
    }

    public void setLoanTerm(int loanTerm) {
        this.loanTerm = loanTerm;
    }

    public BigDecimal getDepositAmount() {
        return depositAmount;
    }

    public void setDepositAmount(BigDecimal depositAmount) {
        this.depositAmount = depositAmount;
    }

    public float getDepositPercent() {
        return depositPercent;
    }

    public void setDepositPercent(float depositPercent) {
        this.depositPercent = depositPercent;
    }

    public int getDepositTerm() {
        return depositTerm;
    }

    public void setDepositTerm(int depositTerm) {
        this.depositTerm = depositTerm;
    }

    public int getPlasticCardCurrency() {
        return plasticCardCurrency;
    }

    public void setPlasticCardCurrency(int plasticCardCurrency) {
        this.plasticCardCurrency = plasticCardCurrency;
    }

    public int getPlasticCardTerm() {
        return plasticCardTerm;
    }

    public void setPlasticCardTerm(int plasticCardTerm) {
        this.plasticCardTerm = plasticCardTerm;
    }

    public int getEmbassyReferenceTerm() {
        return embassyReferenceTerm;
    }

    public void setEmbassyReferenceTerm(int embassyReferenceTerm) {
        this.embassyReferenceTerm = embassyReferenceTerm;
    }

    public int getFinancialReferenceTerm() {
        return financialReferenceTerm;
    }

    public void setFinancialReferenceTerm(int financialReferenceTerm) {
        this.financialReferenceTerm = financialReferenceTerm;
    }

    public int getOrderType() {
        return orderType;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }

    public ProductOrderPayerInfo getPayerInfo() {
        return payerInfo;
    }

    public void setPayerInfo(ProductOrderPayerInfo payerInfo) {
        this.payerInfo = payerInfo;
    }
}
