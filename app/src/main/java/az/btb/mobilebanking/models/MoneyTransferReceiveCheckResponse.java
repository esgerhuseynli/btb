package az.btb.mobilebanking.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MoneyTransferReceiveCheckResponse {
    @SerializedName("responceInfo")
    @Expose
    private ResponseInfo responseInfo;
    @SerializedName("checkTransferBeforeReceiveInfo")
    @Expose
    private CheckTransferBeforeReceiveInfo checkTransferBeforeReceiveInfo;

    public MoneyTransferReceiveCheckResponse(ResponseInfo responseInfo, CheckTransferBeforeReceiveInfo checkTransferBeforeReceiveInfo) {
        this.responseInfo = responseInfo;
        this.checkTransferBeforeReceiveInfo = checkTransferBeforeReceiveInfo;
    }

    public ResponseInfo getResponseInfo() {
        return responseInfo;
    }

    public void setResponseInfo(ResponseInfo responseInfo) {
        this.responseInfo = responseInfo;
    }

    public CheckTransferBeforeReceiveInfo getCheckTransferBeforeReceiveInfo() {
        return checkTransferBeforeReceiveInfo;
    }

    public void setCheckTransferBeforeReceiveInfo(CheckTransferBeforeReceiveInfo checkTransferBeforeReceiveInfo) {
        this.checkTransferBeforeReceiveInfo = checkTransferBeforeReceiveInfo;
    }
}
