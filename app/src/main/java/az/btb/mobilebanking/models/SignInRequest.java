package az.btb.mobilebanking.models;

import com.google.gson.annotations.Expose;
        import com.google.gson.annotations.SerializedName;

public class SignInRequest {

    @SerializedName("RequestInfo")
    @Expose
    private RequestInfo requestInfo;
    @SerializedName("KeystoreType")
    @Expose
    private int keystoreType;
    @SerializedName("SignInType")
    @Expose
    private int signInType;
    @SerializedName("MobileNumber")
    @Expose
    private String mobileNumber;
    @SerializedName("MobileNumberSecretCode")
    @Expose
    private String mobileNumberSecretCode;

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public void setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
    }

    public int getKeystoreType() {
        return keystoreType;
    }

    public void setKeystoreType(int keystoreType) {
        this.keystoreType = keystoreType;
    }

    public int getSignInType() {
        return signInType;
    }

    public void setSignInType(int signInType) {
        this.signInType = signInType;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getMobileNumberSecretCode() {
        return mobileNumberSecretCode;
    }

    public void setMobileNumberSecretCode(String mobileNumberSecretCode) {
        this.mobileNumberSecretCode = mobileNumberSecretCode;
    }

    public SignInRequest(RequestInfo requestInfo, int keystoreType, int signInType, String mobileNumber, String mobileNumberSecretCode) {
        this.requestInfo = requestInfo;
        this.keystoreType = keystoreType;
        this.signInType = signInType;
        this.mobileNumber = mobileNumber;
        this.mobileNumberSecretCode = mobileNumberSecretCode;
    }
}