package az.btb.mobilebanking.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductDepositOrderRequest {
    @SerializedName("RequestInfo")
    @Expose
    private RequestInfo requestInfo;
    @SerializedName("OrderDepositProduct")
    @Expose
    private OrderDepositProduct orderDepositProduct;

    public ProductDepositOrderRequest(RequestInfo requestInfo, OrderDepositProduct orderDepositProduct) {
        this.requestInfo = requestInfo;
        this.orderDepositProduct = orderDepositProduct;
    }

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public void setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
    }

    public OrderDepositProduct getOrderDepositProduct() {
        return orderDepositProduct;
    }

    public void setOrderDepositProduct(OrderDepositProduct orderDepositProduct) {
        this.orderDepositProduct = orderDepositProduct;
    }
}
