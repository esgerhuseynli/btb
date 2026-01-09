package az.btb.mobilebanking.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductFinancialReferenceOrderResponse {
    @SerializedName("responceInfo")
    @Expose
    private ResponseInfo responseInfo;
    @SerializedName("financialReferenceOrderStatus")
    @Expose
    private int financialReferenceOrderStatus;

    public ResponseInfo getResponseInfo() {
        return responseInfo;
    }

    public void setResponseInfo(ResponseInfo responseInfo) {
        this.responseInfo = responseInfo;
    }

    public int getFinancialReferenceOrderStatus() {
        return financialReferenceOrderStatus;
    }

    public void setFinancialReferenceOrderStatus(int financialReferenceOrderStatus) {
        this.financialReferenceOrderStatus = financialReferenceOrderStatus;
    }
}
