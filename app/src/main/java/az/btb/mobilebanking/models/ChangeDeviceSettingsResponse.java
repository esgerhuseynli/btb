package az.btb.mobilebanking.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChangeDeviceSettingsResponse {
    @SerializedName("responceInfo")
    @Expose
    private ResponseInfo responseInfo;
    @SerializedName("changeDeviceID")
    @Expose
    private String changeDeviceID;
    @SerializedName("deviceStatus")
    @Expose
    private Integer deviceStatus;

    public ResponseInfo getResponseInfo() {
        return responseInfo;
    }

    public void setResponseInfo(ResponseInfo responseInfo) {
        this.responseInfo = responseInfo;
    }

    public String getChangeDeviceID() {
        return changeDeviceID;
    }

    public void setChangeDeviceID(String changeDeviceID) {
        this.changeDeviceID = changeDeviceID;
    }

    public Integer getDeviceStatus() {
        return deviceStatus;
    }

    public void setDeviceStatus(Integer deviceStatus) {
        this.deviceStatus = deviceStatus;
    }
}
