package az.btb.mobilebanking.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AsanImzaSignUpRequest {
    @SerializedName("RequestInfo")
    @Expose
    private RequestInfo requestInfo;
    @SerializedName("SignUpAsanImzaCustomertype")
    @Expose
    private int customerType;
    @SerializedName("CustomerPinCode")
    @Expose
    private String pinCode;
    @SerializedName("CustomerTaxNumber")
    @Expose
    private String taxNumber;

    public AsanImzaSignUpRequest(RequestInfo requestInfo, int customerType, String pinCode, String taxNumber) {
        this.requestInfo = requestInfo;
        this.customerType = customerType;
        this.pinCode = pinCode;
        this.taxNumber = taxNumber;
    }

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public void setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
    }

    public void setCustomerType(int Customertype) {
        customerType = Customertype;
    }

    public void setPinCode(String PinCode) {
        pinCode = PinCode;
    }

    public void setTaxNumber(String TaxNumber) {
        taxNumber = TaxNumber;
    }

    public int getCustomerType() {
        return customerType;
    }

    public String getPinCode() {
        return pinCode;
    }

    public String getTaxNumber() {
        return taxNumber;
    }
}
