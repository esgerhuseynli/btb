package az.btb.mobilebanking.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PaymentValidationRequest {
    @SerializedName("RequestInfo")
    @Expose
    private RequestInfo requestInfo;
    @SerializedName("ValidatePayment")
    @Expose
    private ValidatePayment validatePayment;

    public PaymentValidationRequest(RequestInfo requestInfo, ValidatePayment validatePayment) {
        this.requestInfo = requestInfo;
        this.validatePayment = validatePayment;
    }

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public void setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
    }

    public ValidatePayment getValidatePayment() {
        return validatePayment;
    }

    public void setValidatePayment(ValidatePayment validatePayment) {
        this.validatePayment = validatePayment;
    }
}
