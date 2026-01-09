package az.btb.mobilebanking.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AppInfo {

    @SerializedName("AppFor")
    @Expose
    private int appFor;
    @SerializedName("AppName")
    @Expose
    private String appName;
    @SerializedName("AppVersion")
    @Expose
    private String appVersion;
    @SerializedName("AppHash")
    @Expose
    private String apiHash;

    public AppInfo() {
    }

    public AppInfo(int appFor, String appName, String appVersion) {
        this.appFor = appFor;
        this.appName = appName;
        this.appVersion = appVersion;
    }

    public int getAppFor() {
        return appFor;
    }

    public void setAppFor(int appFor) {
        this.appFor = appFor;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getApiHash() {
        return apiHash;
    }

    public void setApiHash(String apiHash) {
        this.apiHash = apiHash;
    }

}