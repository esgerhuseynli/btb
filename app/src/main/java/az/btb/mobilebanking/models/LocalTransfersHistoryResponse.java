package az.btb.mobilebanking.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LocalTransfersHistoryResponse {
    @SerializedName("responceInfo")
    @Expose
    private ResponseInfo responseInfo;
    @SerializedName("localAccountTransfers")
    @Expose
    private List<LocalAccountTransfer> localAccountTransfers;

    public ResponseInfo getResponseInfo() {
        return responseInfo;
    }

    public void setResponseInfo(ResponseInfo responseInfo) {
        this.responseInfo = responseInfo;
    }

    public List<LocalAccountTransfer> getLocalAccountTransfers() {
        return localAccountTransfers;
    }

    public void setLocalAccountTransfers(List<LocalAccountTransfer> localAccountTransfers) {
        this.localAccountTransfers = localAccountTransfers;
    }
}
