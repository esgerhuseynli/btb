package az.btb.mobilebanking.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class LocalBankBranch implements Serializable {
    @SerializedName("branchCode")
    @Expose
    private String branchCode;
    @SerializedName("branchName")
    @Expose
    private String branchName;
    @SerializedName("branchTaxNumber")
    @Expose
    private String branchTaxNumber;
    @SerializedName("branchHOHKSCode")
    @Expose
    private String branchHOHKSCode;
    @SerializedName("branchSWIFTCode")
    @Expose
    private String branchSWIFTCode;
    @SerializedName("branchAddress")
    @Expose
    private String branchAddress;

    public String getBranchCode() {
        return branchCode;
    }

    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getBranchTaxNumber() {
        return branchTaxNumber;
    }

    public void setBranchTaxNumber(String branchTaxNumber) {
        this.branchTaxNumber = branchTaxNumber;
    }

    public String getBranchHOHKSCode() {
        return branchHOHKSCode;
    }

    public void setBranchHOHKSCode(String branchHOHKSCode) {
        this.branchHOHKSCode = branchHOHKSCode;
    }

    public String getBranchSWIFTCode() {
        return branchSWIFTCode;
    }

    public void setBranchSWIFTCode(String branchSWIFTCode) {
        this.branchSWIFTCode = branchSWIFTCode;
    }

    public String getBranchAddress() {
        return branchAddress;
    }

    public void setBranchAddress(String branchAddress) {
        this.branchAddress = branchAddress;
    }
}
