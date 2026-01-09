package az.btb.mobilebanking.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderFinancialReferenceProduct {
    @SerializedName("PayerInfo")
    @Expose
    private ProductOrdererInfo payerInfo;
    @SerializedName("OrderType")
    @Expose
    private int orderType;
    @SerializedName("ProductId")
    @Expose
    private int productId;
    @SerializedName("ReferenceTerm")
    @Expose
    private int referenceTerm;

    public OrderFinancialReferenceProduct(ProductOrdererInfo payerInfo, int orderType, int productId, int referenceTerm) {
        this.payerInfo = payerInfo;
        this.orderType = orderType;
        this.productId = productId;
        this.referenceTerm = referenceTerm;
    }

    public ProductOrdererInfo getPayerInfo() {
        return payerInfo;
    }

    public void setPayerInfo(ProductOrdererInfo payerInfo) {
        this.payerInfo = payerInfo;
    }

    public int getOrderType() {
        return orderType;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getReferenceTerm() {
        return referenceTerm;
    }

    public void setReferenceTerm(int referenceTerm) {
        this.referenceTerm = referenceTerm;
    }
}
