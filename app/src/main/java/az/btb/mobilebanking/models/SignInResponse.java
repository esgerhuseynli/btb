package az.btb.mobilebanking.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SignInResponse {

    @SerializedName("responceInfo")
    @Expose
    private ResponseInfo responseInfo;
    @SerializedName("sessionKey")
    @Expose
    private String sessionKey;
    @SerializedName("signInActionCode")
    @Expose
    private int signInActionCode;

    public ResponseInfo getResponseInfo() {
        return responseInfo;
    }

    public void setResponseInfo(ResponseInfo responseInfo) {
        this.responseInfo = responseInfo;
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }

    public int getSignInActionCode() {
        return signInActionCode;
    }

    public void setSignInActionCode(int signInActionCode) {
        this.signInActionCode = signInActionCode;
    }
}