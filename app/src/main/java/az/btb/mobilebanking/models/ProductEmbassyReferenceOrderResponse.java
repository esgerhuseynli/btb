package az.btb.mobilebanking.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductEmbassyReferenceOrderResponse {
    @SerializedName("responceInfo")
    @Expose
    private ResponseInfo responseInfo;
    @SerializedName("embasyReferenceOrderStatus")
    @Expose
    private int embassyReferenceOrderStatus;

    public ResponseInfo getResponseInfo() {
        return responseInfo;
    }

    public void setResponseInfo(ResponseInfo responseInfo) {
        this.responseInfo = responseInfo;
    }

    public int getEmbassyReferenceOrderStatus() {
        return embassyReferenceOrderStatus;
    }

    public void setEmbassyReferenceOrderStatus(int embassyReferenceOrderStatus) {
        this.embassyReferenceOrderStatus = embassyReferenceOrderStatus;
    }
}
