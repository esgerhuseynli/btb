package az.btb.mobilebanking.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TransferReceiverInfo {
    @SerializedName("Surname")
    @Expose
    private String surname;
    @SerializedName("FirstName")
    @Expose
    private String firstName;
    @SerializedName("MiddleName")
    @Expose
    private String middleName;
    @SerializedName("MobilePhoneNumber")
    @Expose
    private String mobilePhoneNumber;

    public TransferReceiverInfo(String surname, String firstName, String middleName, String mobilePhoneNumber) {
        this.surname = surname;
        this.firstName = firstName;
        this.middleName = middleName;
        this.mobilePhoneNumber = mobilePhoneNumber;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getMobilePhoneNumber() {
        return mobilePhoneNumber;
    }

    public void setMobilePhoneNumber(String mobilePhoneNumber) {
        this.mobilePhoneNumber = mobilePhoneNumber;
    }
}
