package az.btb.mobilebanking.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BankNewsRequest {
    @SerializedName("RequestInfo")
    @Expose
    private RequestInfo requestInfo;
    @SerializedName("FromDate")
    @Expose
    private String fromDate;
    @SerializedName("ToDate")
    @Expose
    private String toDate;
    @SerializedName("ForPhysic")
    @Expose
    private int forPhysic;
    @SerializedName("ForJuric")
    @Expose
    private int forJuric;
    @SerializedName("ForEnter")
    @Expose
    private int forEnter;

    public BankNewsRequest(RequestInfo requestInfo, String fromDate, String toDate, int forPhysic, int forJuric, int forEnter) {
        this.requestInfo = requestInfo;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.forPhysic = forPhysic;
        this.forJuric = forJuric;
        this.forEnter = forEnter;
    }

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public void setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
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

    public int getForEnter() {
        return forEnter;
    }

    public int getForPhysic() {
        return forPhysic;
    }

    public void setForPhysic(int forPhysic) {
        this.forPhysic = forPhysic;
    }

    public int getForJuric() {
        return forJuric;
    }

    public void setForJuric(int forJuric) {
        this.forJuric = forJuric;
    }

    public void setForEnter(int forEnter) {
        this.forEnter = forEnter;
    }
}
