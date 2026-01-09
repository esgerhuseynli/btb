package az.btb.mobilebanking.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OperationsHistoryRequest {

    @SerializedName("RequestInfo")
    @Expose
    private RequestInfo requestInfo;
    @SerializedName("FromIdCard")
    @Expose
    private String fromIdCard;
    @SerializedName("BankCardToCardOperationType")
    @Expose
    private String bankCardToCardOperationType;
    @SerializedName("FromDate")
    @Expose
    private String fromDate;
    @SerializedName("ToDate")
    @Expose
    private String toDate;

    public OperationsHistoryRequest(RequestInfo requestInfo, String fromIdCard, String bankCardToCardOperationType, String fromDate, String toDate) {
        this.requestInfo = requestInfo;
        this.fromIdCard = fromIdCard;
        this.bankCardToCardOperationType = bankCardToCardOperationType;
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public void setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
    }

    public String getFromIdCard() {
        return fromIdCard;
    }

    public void setFromIdCard(String fromIdCard) {
        this.fromIdCard = fromIdCard;
    }

    public String getBankCardToCardOperationType() {
        return bankCardToCardOperationType;
    }

    public void setBankCardToCardOperationType(String bankCardToCardOperationType) {
        this.bankCardToCardOperationType = bankCardToCardOperationType;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }
}
