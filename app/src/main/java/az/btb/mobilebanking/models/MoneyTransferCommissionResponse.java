package az.btb.mobilebanking.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MoneyTransferCommissionResponse {
    @SerializedName("responceInfo")
    @Expose
    private ResponseInfo responseInfo;
    @SerializedName("sendTransferCustomerCommission")
    @Expose
    private SendTransferCustomerCommission sendTransferCustomerCommission;

    public MoneyTransferCommissionResponse(ResponseInfo responseInfo, SendTransferCustomerCommission sendTransferCustomerCommission) {
        this.responseInfo = responseInfo;
        this.sendTransferCustomerCommission = sendTransferCustomerCommission;
    }

    public ResponseInfo getResponseInfo() {
        return responseInfo;
    }

    public void setResponseInfo(ResponseInfo responseInfo) {
        this.responseInfo = responseInfo;
    }

    public SendTransferCustomerCommission getSendTransferCustomerCommission() {
        return sendTransferCustomerCommission;
    }

    public void setSendTransferCustomerCommission(SendTransferCustomerCommission sendTransferCustomerCommission) {
        this.sendTransferCustomerCommission = sendTransferCustomerCommission;
    }
}
