package az.btb.mobilebanking.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
        import com.google.gson.annotations.SerializedName;

public class ChangeForgotPasswordRequest implements Parcelable {

    @SerializedName("RequestInfo")
    @Expose
    private RequestInfo requestInfo;
    @SerializedName("VerificationCode")
    @Expose
    private String verificationCode;
    @SerializedName("NewPasswordHash")
    @Expose
    private String newPasswordHash;

    public ChangeForgotPasswordRequest(RequestInfo requestInfo, String verificationCode, String newPasswordHash) {
        this.requestInfo = requestInfo;
        this.verificationCode = verificationCode;
        this.newPasswordHash = newPasswordHash;
    }

    protected ChangeForgotPasswordRequest(Parcel in) {
        verificationCode = in.readString();
        newPasswordHash = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(verificationCode);
        dest.writeString(newPasswordHash);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ChangeForgotPasswordRequest> CREATOR = new Creator<ChangeForgotPasswordRequest>() {
        @Override
        public ChangeForgotPasswordRequest createFromParcel(Parcel in) {
            return new ChangeForgotPasswordRequest(in);
        }

        @Override
        public ChangeForgotPasswordRequest[] newArray(int size) {
            return new ChangeForgotPasswordRequest[size];
        }
    };

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public void setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public String getNewPasswordHash() {
        return newPasswordHash;
    }

    public void setNewPasswordHash(String newPasswordHash) {
        this.newPasswordHash = newPasswordHash;
    }

}