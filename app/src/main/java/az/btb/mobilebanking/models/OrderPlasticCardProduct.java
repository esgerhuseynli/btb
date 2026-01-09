package az.btb.mobilebanking.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderPlasticCardProduct {
    @SerializedName("PayerInfo")
    @Expose
    private ProductOrdererInfo payerInfo;
    @SerializedName("OrderType")
    @Expose
    private int orderType;
    @SerializedName("ProductId")
    @Expose
    private int productId;
    @SerializedName("Currency")
    @Expose
    private int currency;
    @SerializedName("CardTerm")
    @Expose
    private int cardTerm;

    public OrderPlasticCardProduct(ProductOrdererInfo payerInfo, int orderType, int productId, int currency, int cardTerm) {
        this.payerInfo = payerInfo;
        this.orderType = orderType;
        this.productId = productId;
        this.currency = currency;
        this.cardTerm = cardTerm;
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

    public int getCurrency() {
        return currency;
    }

    public void setCurrency(int currency) {
        this.currency = currency;
    }

    public int getCardTerm() {
        return cardTerm;
    }

    public void setCardTerm(int cardTerm) {
        this.cardTerm = cardTerm;
    }
}
