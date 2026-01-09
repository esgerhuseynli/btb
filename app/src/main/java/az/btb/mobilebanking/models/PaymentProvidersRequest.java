package az.btb.mobilebanking.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PaymentProvidersRequest {
    @Expose
    @SerializedName("RequestInfo")
    private RequestInfo requestInfo;
    @Expose
    @SerializedName("idPaymentProviderGroup")
    private int paymentProviderGroupId;

    public PaymentProvidersRequest(RequestInfo requestInfo, int paymentProviderGroupId) {
        this.requestInfo = requestInfo;
        this.paymentProviderGroupId = paymentProviderGroupId;
    }

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public void setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
    }

    public int getPaymentProviderGroupId() {
        return paymentProviderGroupId;
    }

    public void setPaymentProviderGroupId(int paymentProviderGroupId) {
        this.paymentProviderGroupId = paymentProviderGroupId;
    }
}
