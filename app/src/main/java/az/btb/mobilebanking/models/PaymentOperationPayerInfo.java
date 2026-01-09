package az.btb.mobilebanking.models;

import androidx.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PaymentOperationPayerInfo {
    @SerializedName("paymentFromType")
    @Expose
    private int paymentFromType;
    @SerializedName("fromIdCard")
    @Expose
    private String fromIdCard;
    @SerializedName("fromIbanAccount")
    @Expose
    private String fromIbanAccount;
    @SerializedName("fromIbanSubAccount")
    @Expose
    private String fromIbanSubAccount;
    @SerializedName("fromCardNumber")
    @Expose
    @Nullable
    private String fromCardNumber;

    public PaymentOperationPayerInfo(int paymentFromType, String fromIdCard, String fromIbanAccount, String fromIbanSubAccount, @Nullable String fromCardNumber) {
        this.paymentFromType = paymentFromType;
        this.fromIdCard = fromIdCard;
        this.fromIbanAccount = fromIbanAccount;
        this.fromIbanSubAccount = fromIbanSubAccount;
        this.fromCardNumber = fromCardNumber;
    }

    public int getPaymentFromType() {
        return paymentFromType;
    }

    public void setPaymentFromType(int paymentFromType) {
        this.paymentFromType = paymentFromType;
    }

    public String getFromIdCard() {
        return fromIdCard;
    }

    public void setFromIdCard(String fromIdCard) {
        this.fromIdCard = fromIdCard;
    }

    public String getFromIbanAccount() {
        return fromIbanAccount;
    }

    public void setFromIbanAccount(String fromIbanAccount) {
        this.fromIbanAccount = fromIbanAccount;
    }

    public String getFromIbanSubAccount() {
        return fromIbanSubAccount;
    }

    public void setFromIbanSubAccount(String fromIbanSubAccount) {
        this.fromIbanSubAccount = fromIbanSubAccount;
    }

    public String getFromCardNumber() {
        return fromCardNumber == null ? "" : fromCardNumber;
    }

    public void setFromCardNumber(@Nullable String fromCardNumber) {
        this.fromCardNumber = fromCardNumber;
    }
}
