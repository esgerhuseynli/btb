package az.btb.mobilebanking.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VerifyCodeRequest {

    @SerializedName("RequestInfo")
    @Expose
    private RequestInfo requestInfo;
    @SerializedName("SignUpType")
    @Expose
    private int signUpType;
    @SerializedName("PAN")
    @Expose
    private String PAN;
    @SerializedName("CustomerNumber")
    @Expose
    private String customerNumber;
    @SerializedName("CustomerBirthdate")
    @Expose
    private String customerBirthdate;
    @SerializedName("VerificationCode")
    @Expose
    private String verificationCode;

    public VerifyCodeRequest(RequestInfo requestInfo, int signUpType, String PAN, String customerNumber, String customerBirthdate, String verificationCode) {
        this.requestInfo = requestInfo;
        this.signUpType = signUpType;
        this.PAN = PAN;
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
        return PAN;
    }

    public void setPAN(String PAN) {
        this.PAN = PAN;
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

}