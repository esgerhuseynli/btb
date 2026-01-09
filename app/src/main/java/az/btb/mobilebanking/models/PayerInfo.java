package az.btb.mobilebanking.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import az.btb.mobilebanking.utils.Constants;

public class PayerInfo implements Serializable {
    @SerializedName("transferPaymentType")
    @Expose
    @Constants.MoneySourceTypes
    private int transferPaymentType;
    @SerializedName("fromIdCard")
    @Expose
    private String fromIdCard;
    @SerializedName("fromIbanAccount")
    @Expose
    private String fromIbanAccount;

    public PayerInfo(@Constants.MoneySourceTypes int transferPaymentType, String fromIdCard, String fromIbanAccount) {
        this.transferPaymentType = transferPaymentType;
        this.fromIdCard = fromIdCard;
        this.fromIbanAccount = fromIbanAccount;
    }

    public @Constants.MoneySourceTypes int getTransferPaymentType() {
        return transferPaymentType;
    }

    public void setTransferPaymentType(@Constants.MoneySourceTypes int transferPaymentType) {
        this.transferPaymentType = transferPaymentType;
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
}
