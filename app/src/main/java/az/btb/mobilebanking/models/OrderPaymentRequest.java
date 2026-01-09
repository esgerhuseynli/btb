package az.btb.mobilebanking.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderPaymentRequest {
    @Expose
    @SerializedName("RequestInfo")
    private RequestInfo requestInfo;
    @Expose
    @SerializedName("PayerInfo")
    private ProductOrdererInfo payerInfo;
    @Expose
    @SerializedName("IdOrder")
    private int orderId;

    public OrderPaymentRequest(RequestInfo requestInfo, ProductOrdererInfo payerInfo, int orderId) {
        this.requestInfo = requestInfo;
        this.payerInfo = payerInfo;
        this.orderId = orderId;
    }

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public void setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
    }

    public ProductOrdererInfo getPayerInfo() {
        return payerInfo;
    }

    public  void setPayerInfo(ProductOrdererInfo payerInfo) {
        this.payerInfo = payerInfo;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }
}
