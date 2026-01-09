package az.btb.mobilebanking.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class LocalReceiverInfo implements Serializable {

    @SerializedName("branchCode")
    @Expose
    private String branchCode;
    @SerializedName("customerName")
    @Expose
    private String customerName;
    @SerializedName("ibanAccount")
    @Expose
    private String ibanAccount;
    @SerializedName("ibanSubAccount")
    @Expose
    private String ibanSubAccount;
    @SerializedName("taxNumber")
    @Expose
    private String taxNumber;

    public LocalReceiverInfo(String branchCode, String customerName, String ibanAccount, String ibanSubAccount, String taxNumber) {
        this.branchCode = branchCode;
        this.customerName = customerName;
        this.ibanAccount = ibanAccount;
        this.ibanSubAccount = ibanSubAccount;
        this.taxNumber = taxNumber;
    }

    public String getBranchCode() {
        return branchCode;
    }

    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getIbanAccount() {
        return ibanAccount;
    }

    public void setIbanAccount(String ibanAccount) {
        this.ibanAccount = ibanAccount;
    }

    public String getIbanSubAccount() {
        return ibanSubAccount;
    }

    public void setIbanSubAccount(String ibanSubAccount) {
        this.ibanSubAccount = ibanSubAccount;
    }

    public String getTaxNumber() {
        return taxNumber;
    }

    public void setTaxNumber(String taxNumber) {
        this.taxNumber = taxNumber;
    }
}
