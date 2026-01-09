package az.btb.mobilebanking.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductLoanOrderResponse {
    @SerializedName("responceInfo")
    @Expose
    private ResponseInfo responseInfo;
    @SerializedName("loanReferenceOrderStatus")
    @Expose
    private int loanReferenceOrderStatus;

    public ResponseInfo getResponseInfo() {
        return responseInfo;
    }

    public void setResponseInfo(ResponseInfo responseInfo) {
        this.responseInfo = responseInfo;
    }

    public int getLoanReferenceOrderStatus() {
        return loanReferenceOrderStatus;
    }

    public void setLoanReferenceOrderStatus(int loanReferenceOrderStatus) {
        this.loanReferenceOrderStatus = loanReferenceOrderStatus;
    }
}
