package az.btb.mobilebanking.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChangeDeviceSettingsRequest {
    @SerializedName("RequestInfo")
    @Expose
    private RequestInfo requestInfo;
    @SerializedName("ChangeDeviceID")
    @Expose
    private String ChangeDeviceID;
    @SerializedName("DeviceChangeType")
    @Expose
    private int DeviceChangeType;
    @SerializedName("DeviceStatus")
    @Expose
    private int DeviceStatus;

    public ChangeDeviceSettingsRequest(RequestInfo requestInfo, String changeDeviceID, int deviceChangeType, int deviceStatus) {
        this.requestInfo = requestInfo;
        ChangeDeviceID = changeDeviceID;
        DeviceChangeType = deviceChangeType;
        DeviceStatus = deviceStatus;
    }

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public void setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
    }

    public void setChangeDeviceID(String changeDeviceID) {
        ChangeDeviceID = changeDeviceID;
    }

    public String getChangeDeviceID() {
        return ChangeDeviceID;
    }
    
    public void setDeviceChangeType(int deviceChangeType) {
        DeviceChangeType = deviceChangeType;
    }

    public int getDeviceChangeType() {
        return DeviceChangeType;
    }

    public void setDeviceStatus(int deviceStatus) {
        DeviceStatus = deviceStatus;
    }

    public int getDeviceStatus() {
        return DeviceStatus;
    }
}
