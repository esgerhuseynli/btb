package az.btb.mobilebanking.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UpdateAccountSettingsRequest {
    @SerializedName("RequestInfo")
    @Expose
    private RequestInfo requestInfo;
    @SerializedName("AccountNumber")
    @Expose
    private String accountNumber;
    @SerializedName("AccountAltName")
    @Expose
    private String accountNewAltName;
    @SerializedName("AccountColor")
    @Expose
    private int accountNewColor;

    public UpdateAccountSettingsRequest(RequestInfo requestInfo, String accountNumber, String accountNewAltName, int accountNewColor) {
        this.requestInfo = requestInfo;
        this.accountNumber = accountNumber;
        this.accountNewAltName = accountNewAltName;
        this.accountNewColor = accountNewColor;
    }

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public void setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountNewAltName() {
        return accountNewAltName;
    }

    public void setAccountNewAltName(String accountNewAltName) {
        this.accountNewAltName = accountNewAltName;
    }

    public int getAccountNewColor() {
        return accountNewColor;
    }

    public void setAccountNewColor(int accountNewColor) {
        this.accountNewColor = accountNewColor;
    }
}
