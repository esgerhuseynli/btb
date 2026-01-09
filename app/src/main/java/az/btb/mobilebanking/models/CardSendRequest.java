package az.btb.mobilebanking.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CardSendRequest implements Parcelable {

    @SerializedName("RequestInfo")
    @Expose
    private RequestInfo requestInfo;
    @SerializedName("SignUpType")
    @Expose
    private int signUpType;
    @SerializedName("Pan")
    @Expose
    private String Pan;
    @SerializedName("CustomerNumber")
    @Expose
    private String customerNumber;
    @SerializedName("CustomerBirthdate")
    @Expose
    private String customerBirthdate;
    @SerializedName("MobileNumber")
    @Expose
    private String mobileNumber;
    @SerializedName("MobileNumberSecretCode")
    @Expose
    private String mobileNumberSecretCode;

    public CardSendRequest(RequestInfo requestInfo, int signUpType, String Pan, String customerNumber, String customerBirthdate, String mobileNumber, String mobileNumberSecretCode) {
        this.requestInfo = requestInfo;
        this.signUpType = signUpType;
        this.Pan = Pan;
        this.customerNumber = customerNumber;
        this.customerBirthdate = customerBirthdate;
        this.mobileNumber = mobileNumber;
        this.mobileNumberSecretCode = mobileNumberSecretCode;
    }

    public CardSendRequest() {
    }

    protected CardSendRequest(Parcel in) {
        signUpType = in.readInt();
        Pan = in.readString();
        customerNumber = in.readString();
        customerBirthdate = in.readString();
        mobileNumber = in.readString();
        mobileNumberSecretCode = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(signUpType);
        dest.writeString(Pan);
        dest.writeString(customerNumber);
        dest.writeString(customerBirthdate);
        dest.writeString(mobileNumber);
        dest.writeString(mobileNumberSecretCode);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CardSendRequest> CREATOR = new Creator<CardSendRequest>() {
        @Override
        public CardSendRequest createFromParcel(Parcel in) {
            return new CardSendRequest(in);
        }

        @Override
        public CardSendRequest[] newArray(int size) {
            return new CardSendRequest[size];
        }
    };

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public void setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
    }

    public int getSignUpType() {
        return signUpType;
    }

    public void setSignUpType(int signUpType) {
        this.signUpType = signUpType;
    }

    public String getPan() {
        return Pan;
    }

    public void setPan(String Pan) {
        this.Pan = Pan;
    }

    public String getCustomerNumber() {
        return customerNumber;
    }

    public void setCustomerNumber(String customerNumber) {
        this.customerNumber = customerNumber;
    }

    public String getCustomerBirthdate() {
        return customerBirthdate;
    }

    public void setCustomerBirthdate(String customerBirthdate) {
        this.customerBirthdate = customerBirthdate;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getMobileNumberSecretCode() {
        return mobileNumberSecretCode;
    }

    public void setMobileNumberSecretCode(String mobileNumberSecretCode) {
        this.mobileNumberSecretCode = mobileNumberSecretCode;
    }

}
