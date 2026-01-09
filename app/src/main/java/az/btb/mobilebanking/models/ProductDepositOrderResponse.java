package az.btb.mobilebanking.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductDepositOrderResponse {
    @SerializedName("responceInfo")
    @Expose
    private ResponseInfo responseInfo;
    @SerializedName("depositReferenceOrderStatus")
    @Expose
    private int depositReferenceOrderStatus;

    public ResponseInfo getResponseInfo() {
        return responseInfo;
    }

    public void setResponseInfo(ResponseInfo responseInfo) {
        this.responseInfo = responseInfo;
    }

    public int getDepositReferenceOrderStatus() {
        return depositReferenceOrderStatus;
    }

    public void setDepositReferenceOrderStatus(int depositReferenceOrderStatus) {
        this.depositReferenceOrderStatus = depositReferenceOrderStatus;
    }
}
