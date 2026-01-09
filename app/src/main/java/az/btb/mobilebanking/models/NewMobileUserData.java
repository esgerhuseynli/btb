package az.btb.mobilebanking.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NewMobileUserData implements Parcelable {
    @SerializedName("ChangeMobileUserDataMode")
    @Expose
    private int ChangeMobileUserDataMode;
    @SerializedName("UserName")
    @Expose
    private String UserName;
    @SerializedName("Email")
    @Expose
    private String Email;
    @SerializedName("MobileNumber")
    @Expose
    private String MobileNumber;
    @SerializedName("OldPassword")
    @Expose
    private String OldPassword;
    @SerializedName("NewPassword")
    @Expose
    private String NewPassword;
    @SerializedName("customerCIFNumber")
    @Expose
    private String customerCIFNumber;

    public NewMobileUserData(int changeMobileUserDataMode, String userName, String email, String mobileNumber, String oldPassword, String newPassword, String customerCIFNumber) {
        ChangeMobileUserDataMode = changeMobileUserDataMode;
        UserName = userName;
        Email = email;
        MobileNumber = mobileNumber;
        OldPassword = oldPassword;
        NewPassword = newPassword;
        this.customerCIFNumber = customerCIFNumber;
    }

    public NewMobileUserData() {

    }

    protected NewMobileUserData(Parcel in) {
        ChangeMobileUserDataMode = in.readInt();
        UserName = in.readString();
        Email = in.readString();
        MobileNumber = in.readString();
        OldPassword = in.readString();
        NewPassword = in.readString();
        customerCIFNumber = in.readString();
    }

    public static final Creator<NewMobileUserData> CREATOR = new Creator<NewMobileUserData>() {
        @Override
        public NewMobileUserData createFromParcel(Parcel in) {
            return new NewMobileUserData(in);
        }

        @Override
        public NewMobileUserData[] newArray(int size) {
            return new NewMobileUserData[size];
        }
    };

    public String getCustomerCIFNumber() {
        return customerCIFNumber;
    }

    public void setCustomerCIFNumber(String customerCIFNumber) {
        this.customerCIFNumber = customerCIFNumber;
    }

    public String getNewPassword() {
        return NewPassword;
    }

    public void setNewPassword(String newPassword) {
        NewPassword = newPassword;
    }

    public String getOldPassword() {
        return OldPassword;
    }

    public void setOldPassword(String oldPassword) {
        OldPassword = oldPassword;
    }

    public String getMobileNumber() {
        return MobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        MobileNumber = mobileNumber;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public int getChangeMobileUserDataMode() {
        return ChangeMobileUserDataMode;
    }

    public void setChangeMobileUserDataMode(int changeMobileUserDataMode) {
        ChangeMobileUserDataMode = changeMobileUserDataMode;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(ChangeMobileUserDataMode);
        dest.writeString(UserName);
        dest.writeString(Email);
        dest.writeString(MobileNumber);
        dest.writeString(OldPassword);
        dest.writeString(NewPassword);
        dest.writeString(customerCIFNumber);
    }
}
