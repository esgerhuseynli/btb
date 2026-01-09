package az.btb.mobilebanking.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductFinancialReferenceOrderRequest {
    @SerializedName("RequestInfo")
    @Expose
    private RequestInfo requestInfo;
    @SerializedName("FinancialReferenceOrder")
    @Expose
    private OrderFinancialReferenceProduct financialReferenceOrder;

    public ProductFinancialReferenceOrderRequest(RequestInfo requestInfo, OrderFinancialReferenceProduct financialReferenceOrder) {
        this.requestInfo = requestInfo;
        this.financialReferenceOrder = financialReferenceOrder;
    }

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public void setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
    }

    public OrderFinancialReferenceProduct getFinancialReferenceOrder() {
        return financialReferenceOrder;
    }

    public void setFinancialReferenceOrder(OrderFinancialReferenceProduct financialReferenceOrder) {
        this.financialReferenceOrder = financialReferenceOrder;
    }
}
