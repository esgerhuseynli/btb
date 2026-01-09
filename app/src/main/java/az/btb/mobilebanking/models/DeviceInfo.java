package az.btb.mobilebanking.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DeviceInfo {

    @SerializedName("DeviceID")
    @Expose
    private String deviceID;
    @SerializedName("Vendor")
    @Expose
    private String vendor;
    @SerializedName("Model")
    @Expose
    private String model;
    @SerializedName("OSName")
    @Expose
    private String osName;
    @SerializedName("OSVersion")
    @Expose
    private String osVersion;

    public DeviceInfo() {
    }

    public DeviceInfo(String deviceID, String vendor, String model, String osName, String osVersion) {
        this.deviceID = deviceID;
        this.vendor = vendor;
        this.model = model;
        this.osName = osName;
        this.osVersion = osVersion;
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

    public String getOSName() {
        return osName;
    }

    public void setOSName(String oSName) {
        this.osName = oSName;
    }

    public String getOSVersion() {
        return osVersion;
    }

    public void setOSVersion(String oSVersion) {
        this.osVersion = oSVersion;
    }

}
