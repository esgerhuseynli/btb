package az.btb.mobilebanking.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MoneyTransferOperationStatusCheckResponse {
    @SerializedName("responceInfo")
    @Expose
    private ResponseInfo responseInfo;
    @SerializedName("transferStatusInfo")
    @Expose
    private TransferStatusInfo transferStatusInfo;

    public MoneyTransferOperationStatusCheckResponse(ResponseInfo responseInfo, TransferStatusInfo transferStatusInfo) {
        this.responseInfo = responseInfo;
        this.transferStatusInfo = transferStatusInfo;
    }

    public ResponseInfo getResponseInfo() {
        return responseInfo;
    }

    public void setResponseInfo(ResponseInfo responseInfo) {
        this.responseInfo = responseInfo;
    }

    public TransferStatusInfo getTransferStatusInfo() {
        return transferStatusInfo;
    }

    public void setTransferStatusInfo(TransferStatusInfo transferStatusInfo) {
        this.transferStatusInfo = transferStatusInfo;
    }
}
