package az.btb.mobilebanking.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductEmbassyReferenceOrderRequest {
    @SerializedName("RequestInfo")
    @Expose
    private RequestInfo requestInfo;
    @SerializedName("EmbasyReferenceOrder")
    @Expose
    private OrderEmbassyReferenceProduct embassyReferenceOrder;

    public ProductEmbassyReferenceOrderRequest(RequestInfo requestInfo, OrderEmbassyReferenceProduct embassyReferenceOrder) {
        this.requestInfo = requestInfo;
        this.embassyReferenceOrder = embassyReferenceOrder;
    }

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public void setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
    }

    public OrderEmbassyReferenceProduct getEmbassyReferenceOrder() {
        return embassyReferenceOrder;
    }

    public void setEmbassyReferenceOrder(OrderEmbassyReferenceProduct embasyReferenceOrder) {
        this.embassyReferenceOrder = embasyReferenceOrder;
    }
}
