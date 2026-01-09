package az.btb.mobilebanking.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PaymentProvidersResponse {
    @SerializedName("responceInfo")
    @Expose
    private ResponseInfo responseInfo;
    @SerializedName("paymentProviders")
    @Expose
    private List<PaymentProvider> paymentProviders;

    public PaymentProvidersResponse(ResponseInfo responseInfo, List<PaymentProvider> paymentProviders) {
        this.responseInfo = responseInfo;
        this.paymentProviders = paymentProviders;
    }

    public ResponseInfo getResponseInfo() {
        return responseInfo;
    }

    public void setResponseInfo(ResponseInfo responseInfo) {
        this.responseInfo = responseInfo;
    }

    public List<PaymentProvider> getPaymentProviders() {
        return paymentProviders;
    }

    public void setPaymentProviders(List<PaymentProvider> paymentProviders) {
        this.paymentProviders = paymentProviders;
    }
}
