package az.btb.mobilebanking.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderEmbassyReferenceProduct {
    @SerializedName("PayerInfo")
    @Expose
    private ProductOrdererInfo payerInfo;
    @SerializedName("OrderType")
    @Expose
    private int orderType;
    @SerializedName("idEmbasyPoint")
    @Expose
    private int idEmbasyPoint;
    @SerializedName("ProductId")
    @Expose
    private int productId;
    @SerializedName("ReferenceTerm")
    @Expose
    private int referenceTerm;

    public OrderEmbassyReferenceProduct(ProductOrdererInfo payerInfo, int orderType, int idEmbasyPoint, int productId, int referenceTerm) {
        this.payerInfo = payerInfo;
        this.orderType = orderType;
        this.idEmbasyPoint = idEmbasyPoint;
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

    public int getIdEmbasyPoint() {
        return idEmbasyPoint;
    }

    public void setIdEmbasyPoint(int idEmbasyPoint) {
        this.idEmbasyPoint = idEmbasyPoint;
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
