package az.btb.mobilebanking.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ObsiyPaymentResponse {
    @SerializedName("responceInfo")
    @Expose
    private ResponseInfo responseInfo;
    @SerializedName("paymentProviderJSONParameters")
    @Expose
    private List<PaymentProviderJSONParameter> paymentProviderJSONParameters;

    public ObsiyPaymentResponse(ResponseInfo responseInfo, List<PaymentProviderJSONParameter> paymentProviderJSONParameters) {
        super();
        this.responseInfo = responseInfo;
        this.paymentProviderJSONParameters = paymentProviderJSONParameters;
    }

    public ResponseInfo getResponseInfo() {
        return responseInfo;
    }

    public void setResponseInfo(ResponseInfo responseInfo) {
        this.responseInfo = responseInfo;
    }

    public List<PaymentProviderJSONParameter> getPaymentProviderJSONParameters() {
        return paymentProviderJSONParameters;
    }

    public void setPaymentProviderJSONParameters(List<PaymentProviderJSONParameter> paymentProviderJSONParameters) {
        this.paymentProviderJSONParameters = paymentProviderJSONParameters;
    }
}
