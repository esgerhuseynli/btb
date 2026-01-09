package az.btb.mobilebanking.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class QrCodeValidationRequest {
    @SerializedName("RequestInfo")
    @Expose
    private RequestInfo requestInfo;
    @SerializedName("QRCodeValue")
    @Expose
    private String qrCodeValue;

    public QrCodeValidationRequest(RequestInfo requestInfo, String validatePayment) {
        this.requestInfo = requestInfo;
        this.qrCodeValue = validatePayment;
    }

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public void setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
    }

    public String getValidatePayment() {
        return qrCodeValue;
    }

    public void setValidatePayment(String qrCodeValue) {
        this.qrCodeValue = qrCodeValue;
    }
}
