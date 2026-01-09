package az.btb.mobilebanking.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MoneyTransferOperationStatusCheckRequest {
    @SerializedName("RequestInfo")
    @Expose
    private RequestInfo requestInfo;
    @SerializedName("TransferNumber")
    @Expose
    private String moneyTransferNumber;

    public MoneyTransferOperationStatusCheckRequest(RequestInfo requestInfo, String moneyTransferNumber) {
        this.requestInfo = requestInfo;
        this.moneyTransferNumber = moneyTransferNumber;
    }

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public void setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
    }

    public String getMoneyTransferNumber() {
        return moneyTransferNumber;
    }

    public void setMoneyTransferNumber(String moneyTransferNumber) {
        this.moneyTransferNumber = moneyTransferNumber;
    }
}
