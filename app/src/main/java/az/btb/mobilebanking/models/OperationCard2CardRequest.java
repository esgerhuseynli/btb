package az.btb.mobilebanking.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OperationCard2CardRequest {
    @SerializedName("RequestInfo")
    @Expose
    private RequestInfo requestInfo;
    @SerializedName("OperationCardToCard")
    @Expose
    private OperationCard2Card operationCard2Card;

    public OperationCard2CardRequest(RequestInfo requestInfo, OperationCard2Card operationCard2Card) {
        this.requestInfo = requestInfo;
        this.operationCard2Card = operationCard2Card;
    }

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public void setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
    }

    public OperationCard2Card getOperationCard2Card() {
        return operationCard2Card;
    }

    public void setOperationCard2Card(OperationCard2Card operationCard2Card) {
        this.operationCard2Card = operationCard2Card;
    }
}
