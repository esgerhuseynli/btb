package az.btb.mobilebanking.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class BankBranch implements Serializable {
    @SerializedName("branchCode")
    @Expose
    private Integer branchCode;
    @SerializedName("branchName")
    @Expose
    private String branchName;
    @SerializedName("bankBranchAddress")
    @Expose
    private ServicePointAddress bankBranchAddress;
    @SerializedName("phones")
    @Expose
    private List<String> phones = null;
    @SerializedName("faxes")
    @Expose
    private List<String> faxes = null;
    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("correspondentAccount")
    @Expose
    private String correspondentAccount;
    @SerializedName("swiftBIC")
    @Expose
    private String swiftBIC;
    @SerializedName("workingDays")
    @Expose
    private WorkingDays workingDays;
    @SerializedName("workingHours")
    @Expose
    private WorkingHours workingHours;
    @SerializedName("branchStatus")
    @Expose
    private Integer branchStatus;

    /**
     * No args constructor for use in serialization
     *
     */
    public BankBranch() {
    }

    public BankBranch(Integer branchCode, String branchName, ServicePointAddress bankBranchAddress, List<String> phones, List<String> faxes, String code, String correspondentAccount, String swiftBIC, WorkingDays workingDays, WorkingHours workingHours, Integer branchStatus) {
        this.branchCode = branchCode;
        this.branchName = branchName;
        this.bankBranchAddress = bankBranchAddress;
        this.phones = phones;
        this.faxes = faxes;
        this.code = code;
        this.correspondentAccount = correspondentAccount;
        this.swiftBIC = swiftBIC;
        this.workingDays = workingDays;
        this.workingHours = workingHours;
        this.branchStatus = branchStatus;
    }

    public Integer getBranchCode() {
        return branchCode;
    }

    public void setBranchCode(Integer branchCode) {
        this.branchCode = branchCode;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public ServicePointAddress getBankBranchAddress() {
        return bankBranchAddress;
    }

    public void setBankBranchAddress(ServicePointAddress bankBranchAddress) {
        this.bankBranchAddress = bankBranchAddress;
    }

    public List<String> getPhones() {
        return phones;
    }

    public void setPhones(List<String> phones) {
        this.phones = phones;
    }

    public List<String> getFaxes() {
        return faxes;
    }

    public void setFaxes(List<String> faxes) {
        this.faxes = faxes;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCorrespondentAccount() {
        return correspondentAccount;
    }

    public void setCorrespondentAccount(String correspondentAccount) {
        this.correspondentAccount = correspondentAccount;
    }

    public String getSwiftBIC() {
        return swiftBIC;
    }

    public void setSwiftBIC(String swiftBIC) {
        this.swiftBIC = swiftBIC;
    }

    public WorkingDays getWorkingDays() {
        return workingDays;
    }

    public void setWorkingDays(WorkingDays workingDays) {
        this.workingDays = workingDays;
    }

    public WorkingHours getWorkingHours() {
        return workingHours;
    }

    public void setWorkingHours(WorkingHours workingHours) {
        this.workingHours = workingHours;
    }

    public Integer getBranchStatus() {
        return branchStatus;
    }

    public void setBranchStatus(Integer branchStatus) {
        this.branchStatus = branchStatus;
    }

}
