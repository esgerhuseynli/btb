package az.btb.mobilebanking.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MoneyTransferPaymentPointsRequest {
    @SerializedName("RequestInfo")
    @Expose
    private RequestInfo requestInfo;
    @SerializedName("MTPointsType")
    @Expose
    private int pointType;
    @SerializedName("MTUniqueName")
    @Expose
    private String moneyTransferUniqueName;
    @SerializedName("CountryISO3Code")
    @Expose
    private String countryIso;

    public MoneyTransferPaymentPointsRequest(RequestInfo requestInfo, int pointType, String moneyTransferUniqueName, String countryIso) {
        this.requestInfo = requestInfo;
        this.pointType = pointType;
        this.moneyTransferUniqueName = moneyTransferUniqueName;
        this.countryIso = countryIso;
    }

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public void setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
    }

    public Integer getPointType() {
        return pointType;
    }

    public void setPointType(Integer mTPointsType) {
        pointType = mTPointsType;
    }

    public String getMoneyTransferUniqueName() {
        return moneyTransferUniqueName;
    }

    public void setMoneyTransferUniqueName(String mTUniqueName) {
        moneyTransferUniqueName = mTUniqueName;
    }

    public String getCountryISO() {
        return countryIso;
    }

    public void setCountryISO(String countryISO3Code) {
        countryIso = countryISO3Code;
    }
}
