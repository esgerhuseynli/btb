package az.btb.mobilebanking.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PaymentsHistoryResponse {
    @SerializedName("responceInfo")
    @Expose
    private ResponseInfo responseInfo;
    @SerializedName("mobilePayments")
    @Expose
    private List<MobilePayment> mobilePayments;

    public ResponseInfo getResponseInfo() {
        return responseInfo;
    }

    public void setResponseInfo(ResponseInfo responseInfo) {
        this.responseInfo = responseInfo;
    }

    public List<MobilePayment> getMobilePayments() {
        return mobilePayments;
    }

    public void setMobilePayments(List<MobilePayment> mobilePayments) {
        this.mobilePayments = mobilePayments;
    }
}
