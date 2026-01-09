package az.btb.mobilebanking.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChangeMobileUserDataRequest {
    @SerializedName("RequestInfo")
    @Expose
    private RequestInfo requestInfo;
    @SerializedName("NewMobileUserData")
    @Expose
    private NewMobileUserData newMobileUserData;

    public ChangeMobileUserDataRequest(RequestInfo requestInfo, NewMobileUserData newMobileUserData) {
        this.requestInfo = requestInfo;
        this.newMobileUserData = newMobileUserData;
    }

    public NewMobileUserData getNewMobileUserData() {
        return newMobileUserData;
    }

    public void setNewMobileUserData(NewMobileUserData newMobileUserData) {
        this.newMobileUserData = newMobileUserData;
    }

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public void setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
    }
}
