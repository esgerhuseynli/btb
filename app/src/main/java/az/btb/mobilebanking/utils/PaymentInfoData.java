package az.btb.mobilebanking.utils;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;

public class PaymentInfoData implements Parcelable {
    public String providerName;
    public String phoneNumber;
    public int paymentType;
    public String abonentKodu;
    public String nameSurname;
    public String fromCardIdOrAccountIban;
    public BigDecimal amount;
    
    public PaymentInfoData() {}
    protected PaymentInfoData(Parcel in) {
        providerName = in.readString();
        phoneNumber = in.readString();
        paymentType = in.readInt();
        abonentKodu = in.readString();
        nameSurname = in.readString();
        fromCardIdOrAccountIban = in.readString();
        amount = (BigDecimal) in.readSerializable();
    }
    
    public static final Creator<PaymentInfoData> CREATOR = new Creator<PaymentInfoData>() {
        @Override
        public PaymentInfoData createFromParcel(Parcel in) {
            return new PaymentInfoData(in);
        }
        
        @Override
        public PaymentInfoData[] newArray(int size) {
            return new PaymentInfoData[size];
        }
    };
    
    @Override
    public int describeContents() {
        return 0;
    }
    
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(providerName);
        dest.writeString(phoneNumber);
        dest.writeInt(paymentType);
        dest.writeString(abonentKodu);
        dest.writeString(nameSurname);
        dest.writeString(fromCardIdOrAccountIban);
        dest.writeSerializable(amount);
    }
}
