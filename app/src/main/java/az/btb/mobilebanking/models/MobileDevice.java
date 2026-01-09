package az.btb.mobilebanking.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MobileDevice {
    @SerializedName("deviceID")
    @Expose
    private String deviceID;
    @SerializedName("vendor")
    @Expose
    private String vendor;
    @SerializedName("model")
    @Expose
    private String model;
    @SerializedName("osName")
    @Expose
    private String osName;
    @SerializedName("osVersion")
    @Expose
    private String osVersion;
    @SerializedName("deviceConnectDate")
    @Expose
    private String deviceConnectDate;
    @SerializedName("deviceStatus")
    @Expose
    private int deviceStatus;
    @SerializedName("keystoreType")
    @Expose
    private int keystoreType;
    @SerializedName("lastKeystoreChangeDate")
    @Expose
    private String lastKeystoreChangeDate;
    @SerializedName("lastKeystoreFaultDate")
    @Expose
    private String lastKeystoreFaultDate;
    @SerializedName("lastKeystoreFaultCount")
    @Expose
    private int lastKeystoreFaultCount;
    @SerializedName("lastKeystoreSecurityIncidentType")
    @Expose
    private int lastKeystoreSecurityIncidentType;
    @SerializedName("currentDevice")
    @Expose
    private int currentDevice;
    @SerializedName("deviceSessionState")
    @Expose
    private int deviceSessionState;

    public MobileDevice(String deviceID, String vendor, String model, String osName, String osVersion, String deviceConnectDate, int deviceStatus, int keystoreType, String lastKeystoreChangeDate, String lastKeystoreFaultDate, int lastKeystoreFaultCount, int lastKeystoreSecurityIncidentType, int currentDevice, int deviceSessionState) {
        this.deviceID = deviceID;
        this.vendor = vendor;
        this.model = model;
        this.osName = osName;
        this.osVersion = osVersion;
        this.deviceConnectDate = deviceConnectDate;
        this.deviceStatus = deviceStatus;
        this.keystoreType = keystoreType;
        this.lastKeystoreChangeDate = lastKeystoreChangeDate;
        this.lastKeystoreFaultDate = lastKeystoreFaultDate;
        this.lastKeystoreFaultCount = lastKeystoreFaultCount;
        this.lastKeystoreSecurityIncidentType = lastKeystoreSecurityIncidentType;
        this.currentDevice = currentDevice;
        this.deviceSessionState = deviceSessionState;
    }

    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getOsName() {
        return osName;
    }

    public void setOsName(String osName) {
        this.osName = osName;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public String getDeviceConnectDate() {
        return deviceConnectDate;
    }

    public void setDeviceConnectDate(String deviceConnectDate) {
        this.deviceConnectDate = deviceConnectDate;
    }

    public int getDeviceStatus() {
        return deviceStatus;
    }

    public void setDeviceStatus(int deviceStatus) {
        this.deviceStatus = deviceStatus;
    }

    public int getKeystoreType() {
        return keystoreType;
    }

    public void setKeystoreType(int keystoreType) {
        this.keystoreType = keystoreType;
    }

    public String getLastKeystoreChangeDate() {
        return lastKeystoreChangeDate;
    }

    public void setLastKeystoreChangeDate(String lastKeystoreChangeDate) {
        this.lastKeystoreChangeDate = lastKeystoreChangeDate;
    }

    public String getLastKeystoreFaultDate() {
        return lastKeystoreFaultDate;
    }

    public void setLastKeystoreFaultDate(String lastKeystoreFaultDate) {
        this.lastKeystoreFaultDate = lastKeystoreFaultDate;
    }

    public int getLastKeystoreFaultCount() {
        return lastKeystoreFaultCount;
    }

    public void setLastKeystoreFaultCount(int lastKeystoreFaultCount) {
        this.lastKeystoreFaultCount = lastKeystoreFaultCount;
    }

    public int getLastKeystoreSecurityIncidentType() {
        return lastKeystoreSecurityIncidentType;
    }

    public void setLastKeystoreSecurityIncidentType(int lastKeystoreSecurityIncidentType) {
        this.lastKeystoreSecurityIncidentType = lastKeystoreSecurityIncidentType;
    }

    public int getCurrentDevice() {
        return currentDevice;
    }

    public void setCurrentDevice(int currentDevice) {
        this.currentDevice = currentDevice;
    }

    public int getDeviceSessionState() {
        return deviceSessionState;
    }

    public void setDeviceSessionState(int deviceSessionState) {
        this.deviceSessionState = deviceSessionState;
    }

    public String getUserFriendlyName() {
        return vendor + " " + model;
    }
}
