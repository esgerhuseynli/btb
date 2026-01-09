package az.btb.mobilebanking.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MoneyTransferCountriesRequest {
    @SerializedName("RequestInfo")
    @Expose
    private RequestInfo requestInfo;
    @SerializedName("MTUniqueName")
    @Expose
    private String moneyTransferUniqueName;

    public MoneyTransferCountriesRequest(RequestInfo requestInfo, String moneyTransferUniqueName) {
        this.requestInfo = requestInfo;
        this.moneyTransferUniqueName = moneyTransferUniqueName;
    }

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public void setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
    }

    public String getMoneyTransferUniqueName() {
        return moneyTransferUniqueName;
    }

    public void setMoneyTransferUniqueName(String moneyTransferUniqueName) {
        this.moneyTransferUniqueName = moneyTransferUniqueName;
    }
}
