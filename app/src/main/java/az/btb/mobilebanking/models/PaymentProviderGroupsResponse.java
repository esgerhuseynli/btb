package az.btb.mobilebanking.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PaymentProviderGroupsResponse {
    @SerializedName("responceInfo")
    @Expose
    private ResponseInfo responseInfo;
    @SerializedName("paymentProviderGroups")
    @Expose
    private List<PaymentProviderGroup> paymentProviderGroups;

    public PaymentProviderGroupsResponse(ResponseInfo responseInfo, List<PaymentProviderGroup> paymentProviderGroups) {
        this.responseInfo = responseInfo;
        this.paymentProviderGroups = paymentProviderGroups;
    }

    public ResponseInfo getResponseInfo() {
        return responseInfo;
    }

    public void setResponseInfo(ResponseInfo responseInfo) {
        this.responseInfo = responseInfo;
    }

    public List<PaymentProviderGroup> getPaymentProviderGroups() {
        return paymentProviderGroups;
    }

    public void setPaymentProviderGroups(List<PaymentProviderGroup> paymentProviderGroups) {
        this.paymentProviderGroups = paymentProviderGroups;
    }
}
