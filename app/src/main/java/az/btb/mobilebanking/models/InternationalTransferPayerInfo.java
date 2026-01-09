package az.btb.mobilebanking.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InternationalTransferPayerInfo {
    @SerializedName("TransferPaymentType")
    @Expose
    private int transferPaymentType;
    @SerializedName("FromIdCard")
    @Expose
    private String fromIdCard;
    @SerializedName("FromIbanAccount")
    @Expose
    private String fromIbanAccount;
    @SerializedName("FromIbanSubAccount")
    @Expose
    private String fromIbanSubAccount;

    public InternationalTransferPayerInfo(int transferPaymentType, String fromIdCard, String fromIbanAccount, String fromIbanSubAccount) {
        this.transferPaymentType = transferPaymentType;
        this.fromIdCard = fromIdCard;
        this.fromIbanAccount = fromIbanAccount;
        this.fromIbanSubAccount = fromIbanSubAccount;
    }

    public int getTransferPaymentType() {
        return transferPaymentType;
    }

    public void setTransferPaymentType(int transferPaymentType) {
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

    public String getFromIbanSubAccount() {
        return fromIbanSubAccount;
    }

    public void setFromIbanSubAccount(String fromIbanSubAccount) {
        this.fromIbanSubAccount = fromIbanSubAccount;
    }
}
