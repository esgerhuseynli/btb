package az.btb.mobilebanking.utils;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

import az.btb.mobilebanking.models.CustomerSigningCertificates;

public class AsanImzaData implements Parcelable {

    public List<CustomerSigningCertificates> certificates;
    public String mobileNumberSecretCode;
    public String mobileNumber;
    public String fourDigitVerificationCode;
    public int citizenType; // 1 - for physic, 2 - non-physic
    public String pinCodeOrTaxNumber;

    public AsanImzaData() { }

    protected AsanImzaData(Parcel in) {
        certificates = in.createTypedArrayList(CustomerSigningCertificates.CREATOR);
        mobileNumberSecretCode = in.readString();
        mobileNumber = in.readString();
        fourDigitVerificationCode = in.readString();
        citizenType = in.readInt();
        pinCodeOrTaxNumber = in.readString();
    }

    public static final Creator<AsanImzaData> CREATOR = new Creator<AsanImzaData>() {
        @Override
        public AsanImzaData createFromParcel(Parcel in) {
            return new AsanImzaData(in);
        }

        @Override
        public AsanImzaData[] newArray(int size) {
            return new AsanImzaData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(certificates);
        dest.writeString(mobileNumberSecretCode);
        dest.writeString(mobileNumber);
        dest.writeString(fourDigitVerificationCode);
        dest.writeInt(citizenType);
        dest.writeString(pinCodeOrTaxNumber);
    }
}
