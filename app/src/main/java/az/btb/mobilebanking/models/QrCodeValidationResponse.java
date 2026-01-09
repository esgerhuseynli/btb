package az.btb.mobilebanking.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class QrCodeValidationResponse {
    @SerializedName("responceInfo")
    @Expose
    private ResponseInfo responseInfo;
    @SerializedName("qrCodeValidationInfo")
    @Expose
    private QrCodeValidationInfo qrCodeValidationInfo;

    public QrCodeValidationResponse(ResponseInfo responseInfo, QrCodeValidationInfo qrCodeValidationInfo) {
        this.responseInfo = responseInfo;
        this.qrCodeValidationInfo = qrCodeValidationInfo;
    }

    public ResponseInfo getResponseInfo() {
        return responseInfo;
    }

    public void setResponseInfo(ResponseInfo responseInfo) {
        this.responseInfo = responseInfo;
    }

    public QrCodeValidationInfo getQrCodeValidationInfo() {
        return qrCodeValidationInfo;
    }

    public void setQrCodeValidationInfo(QrCodeValidationInfo qrCodeValidationInfo) {
        this.qrCodeValidationInfo = qrCodeValidationInfo;
    }

}
