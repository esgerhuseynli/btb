package az.btb.mobilebanking.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChangeKeystoreRequest {

    @SerializedName("RequestInfo")
    @Expose
    private RequestInfo requestInfo;
    @SerializedName("KeystoreType")
    @Expose
    private int keystoreType;
    @SerializedName("MobileDeviceSpecifications")
    @Expose
    private MobileDeviceSpecifications mobileDeviceSpecifications;

    public ChangeKeystoreRequest(RequestInfo requestInfo, int keystoreType, MobileDeviceSpecifications mobileDeviceSpecifications) {
        this.requestInfo = requestInfo;
        this.keystoreType = keystoreType;
        this.mobileDeviceSpecifications = mobileDeviceSpecifications;
    }

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public void setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
    }

    public int getKeystoreType() {
        return keystoreType;
    }

    public void setKeystoreType(int keystoreType) {
        this.keystoreType = keystoreType;
    }

    public MobileDeviceSpecifications getMobileDeviceSpecifications() {
        return mobileDeviceSpecifications;
    }

    public void setMobileDeviceSpecifications(MobileDeviceSpecifications mobileDeviceSpecifications) {
        this.mobileDeviceSpecifications = mobileDeviceSpecifications;
    }

}