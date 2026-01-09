package az.btb.mobilebanking.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ForgotPasswordRequest implements Parcelable {

    @SerializedName("RequestInfo")
    @Expose
    private RequestInfo requestInfo;
    @SerializedName("RecoveryPasswordSecurityQuestion")
    @Expose
    private int recoveryPasswordSecurityQuestion;
    @SerializedName("RecoveryPasswordSecurityAnswer")
    @Expose
    private String recoveryPasswordSecurityAnswer;

    public ForgotPasswordRequest(RequestInfo requestInfo, int recoveryPasswordSecurityQuestion, String recoveryPasswordSecurityAnswer) {
        this.requestInfo = requestInfo;
        this.recoveryPasswordSecurityQuestion = recoveryPasswordSecurityQuestion;
        this.recoveryPasswordSecurityAnswer = recoveryPasswordSecurityAnswer;
    }

    protected ForgotPasswordRequest(Parcel in) {
        recoveryPasswordSecurityQuestion = in.readInt();
        recoveryPasswordSecurityAnswer = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(recoveryPasswordSecurityQuestion);
        dest.writeString(recoveryPasswordSecurityAnswer);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ForgotPasswordRequest> CREATOR = new Creator<ForgotPasswordRequest>() {
        @Override
        public ForgotPasswordRequest createFromParcel(Parcel in) {
            return new ForgotPasswordRequest(in);
        }

        @Override
        public ForgotPasswordRequest[] newArray(int size) {
            return new ForgotPasswordRequest[size];
        }
    };

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public void setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
    }

    public int getRecoveryPasswordSecurityQuestion() {
        return recoveryPasswordSecurityQuestion;
    }

    public void setRecoveryPasswordSecurityQuestion(int recoveryPasswordSecurityQuestion) {
        this.recoveryPasswordSecurityQuestion = recoveryPasswordSecurityQuestion;
    }

    public String getRecoveryPasswordSecurityAnswer() {
        return recoveryPasswordSecurityAnswer;
    }

    public void setRecoveryPasswordSecurityAnswer(String recoveryPasswordSecurityAnswer) {
        this.recoveryPasswordSecurityAnswer = recoveryPasswordSecurityAnswer;
    }

}