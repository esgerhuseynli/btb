package az.btb.mobilebanking.models;

import androidx.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MobileUserData {
    @SerializedName("changeMobileUserDataMode")
    @Expose
    private int changeMobileUserDataMode;
    @SerializedName("userName")
    @Expose
    private String userName;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("mobileNumber")
    @Expose
    private String mobileNumber;
    @SerializedName("oldPassword")
    @Expose
    private String oldPassword;
    @SerializedName("newPassword")
    @Expose
    private String newPassword;
    @SerializedName("customerCIFNumber")
    @Expose
    private String customerCIFNumber;
    @Nullable
    @SerializedName("customerName")
    @Expose
    private String customerName;

    public String getCustomerCIFNumber() {
        return customerCIFNumber;
    }

    public void setCustomerCIFNumber(String customerCIFNumber) {
        this.customerCIFNumber = customerCIFNumber;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getChangeMobileUserDataMode() {
        return changeMobileUserDataMode;
    }

    public void setChangeMobileUserDataMode(int changeMobileUserDataMode) {
        this.changeMobileUserDataMode = changeMobileUserDataMode;
    }

    @Nullable
    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(@Nullable String customerName) {
        this.customerName = customerName;
    }
}
