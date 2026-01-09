package az.btb.mobilebanking.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BankATM {
    @SerializedName("atmName")
    @Expose
    private String atmName;
    @SerializedName("bankATMAddress")
    @Expose
    private ServicePointAddress bankAtmAddress;
    @SerializedName("workingDays")
    @Expose
    private WorkingDays workingDays;
    @SerializedName("workingHours")
    @Expose
    private WorkingHours workingHours;
    @SerializedName("atmStatus")
    @Expose
    private Integer atmStatus;

    public BankATM(String atmName, ServicePointAddress bankAtmAddress, WorkingDays workingDays, WorkingHours workingHours, Integer atmStatus) {
        this.atmName = atmName;
        this.bankAtmAddress = bankAtmAddress;
        this.workingDays = workingDays;
        this.workingHours = workingHours;
        this.atmStatus = atmStatus;
    }

    public String getAtmName() {
        return atmName;
    }

    public void setAtmName(String atmName) {
        this.atmName = atmName;
    }

    public ServicePointAddress getBankAtmAddress() {
        return bankAtmAddress;
    }

    public void setBankAtmAddress(ServicePointAddress bankAtmAddress) {
        this.bankAtmAddress = bankAtmAddress;
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

    public Integer getAtmStatus() {
        return atmStatus;
    }

    public void setAtmStatus(Integer atmStatus) {
        this.atmStatus = atmStatus;
    }
}
