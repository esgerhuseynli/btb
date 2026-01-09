package az.btb.mobilebanking.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RequestInfo {

    @SerializedName("MobileUser")
    @Expose
    private MobileUser mobileUser;
    @SerializedName("DeviceInfo")
    @Expose
    private DeviceInfo deviceInfo;
    @SerializedName("AppInfo")
    @Expose
    private AppInfo appInfo;
    @SerializedName("Language")
    @Expose
    private int language;

    public RequestInfo(MobileUser mobileUser, DeviceInfo deviceInfo, AppInfo appInfo, int language) {
        this.mobileUser = mobileUser;
        this.deviceInfo = deviceInfo;
        this.appInfo = appInfo;
        this.language = language;
    }

    public MobileUser getMobileUser() {
        return mobileUser;
    }

    public void setMobileUser(MobileUser mobileUser) {
        this.mobileUser = mobileUser;
    }

    public DeviceInfo getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(DeviceInfo deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    public AppInfo getAppInfo() {
        return appInfo;
    }

    public void setAppInfo(AppInfo appInfo) {
        this.appInfo = appInfo;
    }

    public int getLanguage() {
        return language;
    }

    public void setLanguage(int language) {
        this.language = language;
    }

}