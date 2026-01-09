package az.btb.mobilebanking.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MobileUserDataResponse {
    @SerializedName("responceInfo")
    @Expose
    private ResponseInfo responseInfo;
    @SerializedName("mobileUserData")
    @Expose
    private MobileUserData mobileUserData;

    public MobileUserDataResponse(ResponseInfo responseInfo, MobileUserData mobileUserData) {
        this.responseInfo = responseInfo;
        this.mobileUserData = mobileUserData;
    }

    public ResponseInfo getResponseInfo() {
        return responseInfo;
    }

    public void setResponseInfo(ResponseInfo responseInfo) {
        this.responseInfo = responseInfo;
    }

    public MobileUserData getMobileUserData() {
        return mobileUserData;
    }

    public void setMobileUserData(MobileUserData mobileUserData) {
        this.mobileUserData = mobileUserData;
    }
}
