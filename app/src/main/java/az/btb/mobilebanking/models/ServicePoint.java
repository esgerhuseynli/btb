package az.btb.mobilebanking.models;

import java.io.Serializable;
import java.util.List;

public class ServicePoint implements Serializable {
    private int servicePointType;

    private String name;
    private ServicePointAddress address;
    private WorkingDays workingDays;
    private WorkingHours workingHours;
    private int status;

    private String branchCode;
    private List<String> branchPhones;
    private List<String> branchFaxes;
    private String branchCorrespondentAccount;
    private String branchSwift;

    public ServicePoint(int servicePointType) {
        this.servicePointType = servicePointType;
    }

    public int getServicePointType() {
        return servicePointType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ServicePointAddress getAddress() {
        return address;
    }

    public void setAddress(ServicePointAddress address) {
        this.address = address;
    }

    public List<String> getBranchPhones() {
        return branchPhones;
    }

    public void setBranchPhones(List<String> branchPhones) {
        this.branchPhones = branchPhones;
    }

    public List<String> getBranchFaxes() {
        return branchFaxes;
    }

    public void setBranchFaxes(List<String> branchFaxes) {
        this.branchFaxes = branchFaxes;
    }

    public String getBranchCode() {
        return branchCode;
    }

    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    public String getBranchCorrespondentAccount() {
        return branchCorrespondentAccount;
    }

    public void setBranchCorrespondentAccount(String branchCorrespondentAccount) {
        this.branchCorrespondentAccount = branchCorrespondentAccount;
    }

    public String getBranchSwift() {
        return branchSwift;
    }

    public void setBranchSwift(String branchSwift) {
        this.branchSwift = branchSwift;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
