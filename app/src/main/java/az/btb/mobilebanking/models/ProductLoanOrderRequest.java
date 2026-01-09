package az.btb.mobilebanking.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductLoanOrderRequest {
    @SerializedName("RequestInfo")
    @Expose
    private RequestInfo requestInfo;
    @SerializedName("OrderLoanProduct")
    @Expose
    private OrderLoanProduct orderLoanProduct;

    public ProductLoanOrderRequest(RequestInfo requestInfo, OrderLoanProduct orderLoanProduct) {
        this.requestInfo = requestInfo;
        this.orderLoanProduct = orderLoanProduct;
    }

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public void setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
    }

    public OrderLoanProduct getOrderLoanProduct() {
        return orderLoanProduct;
    }

    public void setOrderLoanProduct(OrderLoanProduct orderLoanProduct) {
        this.orderLoanProduct = orderLoanProduct;
    }
}
