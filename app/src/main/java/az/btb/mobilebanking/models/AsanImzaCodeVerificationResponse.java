package az.btb.mobilebanking.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AsanImzaCodeVerificationResponse {
    @SerializedName("responceInfo")
    @Expose
    private ResponseInfo responseInfo;
    @SerializedName("authenticateStatusType")
    @Expose
    private int authenticateStatusType;
    @SerializedName("customerSigningCertificates")
    @Expose
    private List<CustomerSigningCertificates> customerSigningCertificates;
    @SerializedName("mobileNumber")
    @Expose
    private String mobileNumber;

    public ResponseInfo getResponseInfo() {
        return responseInfo;
    }

    public void setResponseInfo(ResponseInfo responseInfo) {
        this.responseInfo = responseInfo;
    }

    public int getAuthenticateStatusType() {
        return authenticateStatusType;
    }

    public void setAuthenticateStatusType(int authenticateStatusType) {
        this.authenticateStatusType = authenticateStatusType;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public List<CustomerSigningCertificates> getCustomerSigningCertificates() {
        return customerSigningCertificates;
    }

    public void setCustomerSigningCertificates(List<CustomerSigningCertificates> customerSigningCertificates) {
        this.customerSigningCertificates = customerSigningCertificates;
    }
}
