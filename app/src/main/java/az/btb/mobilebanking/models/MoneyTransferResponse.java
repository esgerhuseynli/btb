package az.btb.mobilebanking.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MoneyTransferResponse {
    @SerializedName("responceInfo")
    @Expose
    private ResponseInfo responseInfo;
    @SerializedName("sendTransferInfo")
    @Expose
    private SendTransferInfo sendTransferInfo;

    public MoneyTransferResponse(ResponseInfo responseInfo, SendTransferInfo sendTransferInfo) {
        this.responseInfo = responseInfo;
        this.sendTransferInfo = sendTransferInfo;
    }

    public ResponseInfo getResponseInfo() {
        return responseInfo;
    }

    public void setResponseInfo(ResponseInfo responseInfo) {
        this.responseInfo = responseInfo;
    }

    public SendTransferInfo getSendTransferInfo() {
        return sendTransferInfo;
    }

    public void setSendTransferInfo(SendTransferInfo sendTransferInfo) {
        this.sendTransferInfo = sendTransferInfo;
    }
}
