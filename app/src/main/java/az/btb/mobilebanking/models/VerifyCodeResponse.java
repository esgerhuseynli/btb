package az.btb.mobilebanking.models;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VerifyCodeResponse {

    @SerializedName("responceInfo")
    @Expose
    private ResponseInfo responseInfo;
    @SerializedName("verificationCodeResult")
    @Expose
    private Integer verificationCodeResult;
    @SerializedName("mobileUserSignUpStatus")
    @Expose
    private Integer mobileUserSignUpStatus;

    public ResponseInfo getResponseInfo() {
        return responseInfo;
    }

    public void setResponseInfo(ResponseInfo responseInfo) {
        this.responseInfo = responseInfo;
    }

    public Integer getVerificationCodeResult() {
        return verificationCodeResult;
    }

    public void setVerificationCodeResult(Integer verificationCodeResult) {
        this.verificationCodeResult = verificationCodeResult;
    }

    public Integer getMobileUserSignUpStatus() {
        return mobileUserSignUpStatus;
    }

    public void setMobileUserSignUpStatus(Integer mobileUserSignUpStatus) {
        this.mobileUserSignUpStatus = mobileUserSignUpStatus;
    }

}