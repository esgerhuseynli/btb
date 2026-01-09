package az.btb.mobilebanking.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CustomerSigningCertificates implements Parcelable {
    @SerializedName("customerName")
    @Expose
    private String customerName;
    @SerializedName("customerTaxNumber")
    @Expose
    private String customerTaxNumber;
    @SerializedName("customerPinNumber")
    @Expose
    private String customerPinNumber;

    public final static Parcelable.Creator<CustomerSigningCertificates> CREATOR = new Creator<CustomerSigningCertificates>() {
        public CustomerSigningCertificates createFromParcel(Parcel in) {
            return new CustomerSigningCertificates(in);
        }

        public CustomerSigningCertificates[] newArray(int size) {
            return new CustomerSigningCertificates[size];
        }
    };

    protected CustomerSigningCertificates(Parcel in) {
        this.customerName = ((String) in.readValue((String.class.getClassLoader())));
        this.customerTaxNumber = ((String) in.readValue((String.class.getClassLoader())));
        this.customerPinNumber = ((String) in.readValue((String.class.getClassLoader())));
    }

    public CustomerSigningCertificates() { }

    public CustomerSigningCertificates(String customerName, String customerTaxNumber, String customerPinNumber) {
        super();
        this.customerName = customerName;
        this.customerTaxNumber = customerTaxNumber;
        this.customerPinNumber = customerPinNumber;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerTaxNumber() {
        return customerTaxNumber;
    }

    public void setCustomerTaxNumber(String customerTaxNumber) {
        this.customerTaxNumber = customerTaxNumber;
    }

    public String getCustomerPinNumber() {
        return customerPinNumber;
    }

    public void setCustomerPinNumber(String customerPinNumber) {
        this.customerPinNumber = customerPinNumber;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(customerName);
        dest.writeValue(customerTaxNumber);
        dest.writeValue(customerPinNumber);
    }

    public int describeContents() {
        return 0;
    }
}
