package az.btb.mobilebanking.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MobileDeviceSpecifications {

    @SerializedName("NFC")
    @Expose
    private String nFC;
    @SerializedName("FaceID")
    @Expose
    private String faceID;
    @SerializedName("TouchID")
    @Expose
    private String touchID;

    public MobileDeviceSpecifications(String nFC, String faceID, String touchID) {
        this.nFC = nFC;
        this.faceID = faceID;
        this.touchID = touchID;
    }

    public String getNFC() {
        return nFC;
    }

    public void setNFC(String nFC) {
        this.nFC = nFC;
    }

    public String getFaceID() {
        return faceID;
    }

    public void setFaceID(String faceID) {
        this.faceID = faceID;
    }

    public String getTouchID() {
        return touchID;
    }

    public void setTouchID(String touchID) {
        this.touchID = touchID;
    }

}