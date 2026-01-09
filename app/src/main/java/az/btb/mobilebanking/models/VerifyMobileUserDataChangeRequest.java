package az.btb.mobilebanking.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VerifyMobileUserDataChangeRequest {
    @SerializedName("RequestInfo")
    @Expose
    private RequestInfo requestInfo;
    @SerializedName("ChangeMobileUserDataMode")
    @Expose
    private int ChangeMobileUserDataMode;
    @SerializedName("VerifyCode")
    @Expose
    private String VerifyCode;

    public VerifyMobileUserDataChangeRequest(RequestInfo requestInfo, int changeMobileUserDataMode, String verifyCode) {
        this.requestInfo = requestInfo;
        ChangeMobileUserDataMode = changeMobileUserDataMode;
        VerifyCode = verifyCode;
    }

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public void setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
    }

    public int getChangeMobileUserDataMode() {
        return ChangeMobileUserDataMode;
    }

    public void setChangeMobileUserDataMode(int changeMobileUserDataMode) {
        ChangeMobileUserDataMode = changeMobileUserDataMode;
    }

    public String getVerifyCode() {
        return VerifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        VerifyCode = verifyCode;
    }
}
