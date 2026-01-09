package az.btb.mobilebanking.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class KeystoreIncidentRequest {
    @SerializedName("RequestInfo")
    @Expose
    private RequestInfo requestInfo;
    @SerializedName("KeystoreSecurityIncidentType")
    @Expose
    private int KeystoreSecurityIncidentType;
    @SerializedName("KeystoreSecurityIncidentCount")
    @Expose
    private int KeystoreSecurityIncidentCount;

    public KeystoreIncidentRequest(RequestInfo requestInfo, int keystoreSecurityIncidentType, int keystoreSecurityIncidentCount) {
        this.requestInfo = requestInfo;
        KeystoreSecurityIncidentType = keystoreSecurityIncidentType;
        KeystoreSecurityIncidentCount = keystoreSecurityIncidentCount;
    }

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public void setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
    }

    public int getKeystoreSecurityIncidentType() {
        return KeystoreSecurityIncidentType;
    }

    public void setKeystoreSecurityIncidentType(int keystoreSecurityIncidentType) {
        KeystoreSecurityIncidentType = keystoreSecurityIncidentType;
    }

    public int getKeystoreSecurityIncidentCount() {
        return KeystoreSecurityIncidentCount;
    }

    public void setKeystoreSecurityIncidentCount(int keystoreSecurityIncidentCount) {
        KeystoreSecurityIncidentCount = keystoreSecurityIncidentCount;
    }
}
