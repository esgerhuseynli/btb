package az.btb.mobilebanking.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MobileUserDataRequest {
    @SerializedName("RequestInfo")
    @Expose
    private RequestInfo requestInfo;
    @SerializedName("SignInType")
    @Expose
    private int signInType;
    @SerializedName("MobileNumber")
    @Expose
    private String mobileNumber;
    @SerializedName("MobileNumberSecretCode")
    @Expose
    private String mobileNumberSecretCode;

    public MobileUserDataRequest(RequestInfo requestInfo, int signInType, String mobileNumber, String mobileNumberSecretCode) {
        this.requestInfo = requestInfo;
        this.signInType = signInType;
        this.mobileNumber = mobileNumber;
        this.mobileNumberSecretCode = mobileNumberSecretCode;
    }

    public String getMobileNumberSecretCode() {
        return mobileNumberSecretCode;
    }

    public void setMobileNumberSecretCode(String mobileNumberSecretCode) {
        this.mobileNumberSecretCode = mobileNumberSecretCode;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public int getSignInType() {
        return signInType;
    }

    public void setSignInType(int signInType) {
        this.signInType = signInType;
    }

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public void setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
    }
}
