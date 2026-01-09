package az.btb.mobilebanking.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MobileDevicesListResponse {
    @SerializedName("responceInfo")
    @Expose
    private ResponseInfo responseInfo;
    @SerializedName("mobileDevices")
    @Expose
    private List<MobileDevice> mobileDevices;

    public MobileDevicesListResponse(ResponseInfo responseInfo, List<MobileDevice> mobileDevices) {
        this.responseInfo = responseInfo;
        this.mobileDevices = mobileDevices;
    }

    public ResponseInfo getResponseInfo() {
        return responseInfo;
    }

    public void setResponseInfo(ResponseInfo responseInfo) {
        this.responseInfo = responseInfo;
    }

    public List<MobileDevice> getMobileDevices() {
        return mobileDevices;
    }

    public void setMobileDevices(List<MobileDevice> mobileDevices) {
        this.mobileDevices = mobileDevices;
    }
}
