package az.btb.mobilebanking.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CardSendResponse implements Parcelable {
    @SerializedName("responceInfo")
    @Expose
    private ResponseInfo responseInfo;
    @SerializedName("mobileNumber")
    @Expose
    private String mobileNumber;
    @SerializedName("maskedMobileNumber")
    @Expose
    private String maskedMobileNumber;
    @SerializedName("email")
    @Expose
    private String email;

    public CardSendResponse(){}
    protected CardSendResponse(Parcel in) {
        mobileNumber = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mobileNumber);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CardSendResponse> CREATOR = new Creator<CardSendResponse>() {
        @Override
        public CardSendResponse createFromParcel(Parcel in) {
            return new CardSendResponse(in);
        }

        @Override
        public CardSendResponse[] newArray(int size) {
            return new CardSendResponse[size];
        }
    };

    public ResponseInfo getResponseInfo() {
        return responseInfo;
    }

    public void setResponseInfo(ResponseInfo responseInfo) {
        this.responseInfo = responseInfo;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getMaskedMobileNumber() {
        return maskedMobileNumber;
    }

    public void setMaskedMobileNumber(String maskedMobileNumber) {
        this.maskedMobileNumber = maskedMobileNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}