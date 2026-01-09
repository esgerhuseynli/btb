package az.btb.mobilebanking.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AsanImzaSignUpResponse {
    @SerializedName("responceInfo")
    @Expose
    private ResponseInfo responseInfo;
    @SerializedName("mobileNumber")
    @Expose
    private String mobileNumber;
    @SerializedName("email")
    @Expose
    private String email;

    public AsanImzaSignUpResponse(ResponseInfo responseInfo, String mobileNumber, String email) {
        this.responseInfo = responseInfo;
        this.mobileNumber = mobileNumber;
        this.email = email;
    }

    public ResponseInfo getResponseInfo() {
        return responseInfo;
    }

    public void setResponseInfo(ResponseInfo responseInfo) {
        this.responseInfo = responseInfo;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
