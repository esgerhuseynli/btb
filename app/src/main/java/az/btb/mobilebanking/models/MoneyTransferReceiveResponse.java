package az.btb.mobilebanking.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MoneyTransferReceiveResponse {
    @SerializedName("responceInfo")
    @Expose
    private ResponseInfo responseInfo;
    @SerializedName("transferReceiverInfo")
    @Expose
    private MoneyTransferReceiverInfo transferReceiverInfo;

    public MoneyTransferReceiveResponse(ResponseInfo responseInfo, MoneyTransferReceiverInfo transferReceiverInfo) {
        this.responseInfo = responseInfo;
        this.transferReceiverInfo = transferReceiverInfo;
    }

    public ResponseInfo getResponseInfo() {
        return responseInfo;
    }

    public void setResponseInfo(ResponseInfo responseInfo) {
        this.responseInfo = responseInfo;
    }

    public MoneyTransferReceiverInfo getTransferReceiverInfo() {
        return transferReceiverInfo;
    }

    public void setTransferReceiverInfo(MoneyTransferReceiverInfo transferReceiverInfo) {
        this.transferReceiverInfo = transferReceiverInfo;
    }

}
