package az.btb.mobilebanking.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InternationalTransfersHistoryRequest {
    @SerializedName("RequestInfo")
    @Expose
    private RequestInfo requestInfo;
    @SerializedName("IdCard")
    @Expose
    private String idCard;
    @SerializedName("IbanAccount")
    @Expose
    private String ibanAccount;
    @SerializedName("FromDate")
    @Expose
    private String fromDate;
    @SerializedName("ToDate")
    @Expose
    private String toDate;

    public InternationalTransfersHistoryRequest(RequestInfo requestInfo, String idCard, String ibanAccount, String fromDate, String toDate) {
        this.requestInfo = requestInfo;
        this.idCard = idCard;
        this.ibanAccount = ibanAccount;
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public void setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getIbanAccount() {
        return ibanAccount;
    }

    public void setIbanAccount(String ibanAccount) {
        this.ibanAccount = ibanAccount;
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
