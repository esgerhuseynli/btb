package az.btb.mobilebanking.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class InternationalTransfersHistoryResponse {
    @SerializedName("responceInfo")
    @Expose
    private ResponseInfo responseInfo;
    @SerializedName("foreignAccountTransfers")
    @Expose
    private List<ForeignAccountTransfer> foreignAccountTransfers;

    public ResponseInfo getResponseInfo() {
        return responseInfo;
    }

    public void setResponseInfo(ResponseInfo responseInfo) {
        this.responseInfo = responseInfo;
    }

    public List<ForeignAccountTransfer> getForeignAccountTransfers() {
        return foreignAccountTransfers;
    }

    public void setForeignAccountTransfers(List<ForeignAccountTransfer> foreignAccountTransfers) {
        this.foreignAccountTransfers = foreignAccountTransfers;
    }
}
