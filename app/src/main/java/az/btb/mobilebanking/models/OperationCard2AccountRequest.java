package az.btb.mobilebanking.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OperationCard2AccountRequest {
    @SerializedName("RequestInfo")
    @Expose
    private RequestInfo requestInfo;
    @SerializedName("OperationCardToAccount")
    @Expose
    private OperationCardToAccount operationCardToAccount;

    public OperationCard2AccountRequest(RequestInfo requestInfo, OperationCardToAccount operationCardToAccount) {
        super();
        this.requestInfo = requestInfo;
        this.operationCardToAccount = operationCardToAccount;
    }

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public void setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
    }

    public OperationCardToAccount getOperationCardToAccount() {
        return operationCardToAccount;
    }

    public void setOperationCardToAccount(OperationCardToAccount operationCardToAccount) {
        this.operationCardToAccount = operationCardToAccount;
    }
}
