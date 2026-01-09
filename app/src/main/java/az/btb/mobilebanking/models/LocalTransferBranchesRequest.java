package az.btb.mobilebanking.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LocalTransferBranchesRequest {
    @SerializedName("RequestInfo")
    @Expose
    private RequestInfo requestInfo;
    @SerializedName("BranchCode")
    @Expose
    private String code;
    @SerializedName("BranchName")
    @Expose
    private String name;
    @SerializedName("BranchTaxNumber")
    @Expose
    private String taxNumber;

    public LocalTransferBranchesRequest(RequestInfo requestInfo, String code, String name, String taxNumber) {
        this.requestInfo = requestInfo;
        this.code = code;
        this.name = name;
        this.taxNumber = taxNumber;
    }

    public void setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
    }

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getTaxNumber() {
        return taxNumber;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTaxNumber(String taxNumber) {
        this.taxNumber = taxNumber;
    }
}
