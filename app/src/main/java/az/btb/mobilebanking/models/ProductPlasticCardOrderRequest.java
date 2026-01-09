package az.btb.mobilebanking.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductPlasticCardOrderRequest {

    @SerializedName("RequestInfo")
    @Expose
    private RequestInfo requestInfo;
    @SerializedName("OrderPlasticCardProduct")
    @Expose
    private OrderPlasticCardProduct orderPlasticCardProduct;

    public ProductPlasticCardOrderRequest(RequestInfo requestInfo, OrderPlasticCardProduct orderPlasticCardProduct) {
        this.requestInfo = requestInfo;
        this.orderPlasticCardProduct = orderPlasticCardProduct;
    }

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public void setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
    }

    public OrderPlasticCardProduct getOrderPlasticCardProduct() {
        return orderPlasticCardProduct;
    }

    public void setOrderPlasticCardProduct(OrderPlasticCardProduct orderPlasticCardProduct) {
        this.orderPlasticCardProduct = orderPlasticCardProduct;
    }
}
