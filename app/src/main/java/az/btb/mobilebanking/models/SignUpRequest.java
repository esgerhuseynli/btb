package az.btb.mobilebanking.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SignUpRequest {

    @SerializedName("RequestInfo")
    @Expose
    private RequestInfo requestInfo;
    @SerializedName("UsernameType")
    @Expose
    private int usernameType;
    @SerializedName("SignUpType")
    @Expose
    private int signUpType;
    @SerializedName("PAN")
    @Expose
    private String pAN;
    @SerializedName("CustomerNumber")
    @Expose
    private String customerNumber;
    @SerializedName("CustomerBirthdate")
    @Expose
    private String customerBirthdate;
    @SerializedName("VerificationCode")
    @Expose
    private String verificationCode;
    @SerializedName("MobileNumber")
    @Expose
    private String mobileNumber;
    @SerializedName("MobileNumberSecretCode")
    @Expose
    private String mobileNumberSecretNumberCode;

    public SignUpRequest(
        RequestInfo requestInfo, int usernameType, int signUpType, String pAN,
        String customerNumber, String customerBirthdate, String verificationCode
    ) {
        this.requestInfo = requestInfo;
        this.usernameType = usernameType;
        this.signUpType = signUpType;
        this.pAN = pAN;
        this.customerNumber = customerNumber;
        this.customerBirthdate = customerBirthdate;
        this.verificationCode = verificationCode;
    }

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public void setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
    }

    public int getSignUpType() {
        return signUpType;
    }

    public void setSignUpType(int signUpType) {
        this.signUpType = signUpType;
    }

    public String getPAN() {
        return pAN;
    }

    public void setPAN(String pAN) {
        this.pAN = pAN;
    }

    public String getCustomerNumber() {
        return customerNumber;
    }

    public void setCustomerNumber(String customerNumber) {
        this.customerNumber = customerNumber;
    }

    public String getCustomerBirthdate() {
        return customerBirthdate;
    }

    public void setCustomerBirthdate(String customerBirthdate) {
        this.customerBirthdate = customerBirthdate;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public int getUsernameType() {
        return usernameType;
    }

    public void setUsernameType(int usernameType) {
        this.usernameType = usernameType;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getMobileNumberSecretNumberCode() {
        return mobileNumberSecretNumberCode;
    }

    public void setMobileNumberSecretNumberCode(String mobileNumberSecretNumberCode) {
        this.mobileNumberSecretNumberCode = mobileNumberSecretNumberCode;
    }
}