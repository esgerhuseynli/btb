package az.btb.mobilebanking.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import az.btb.mobilebanking.utils.Constants;

public class ProductOrdererInfo {
    @SerializedName("ProductPaymentType")
    @Expose
    private int productPaymentType;
    @SerializedName("FromIdCard")
    @Expose
    private String fromIdCard = "";
    @SerializedName("FromIbanAccount")
    @Expose
    private String fromIbanAccount = "";
    @SerializedName("FromIbanSubAccount")
    @Expose
    private String fromIbanSubAccount = "";
    @SerializedName("FromCardNumber")
    @Expose
    private String fromCardNumber = "";

    public @Constants.MoneySourceTypes int getProductPaymentType() {
        return productPaymentType;
    }

    public void setProductPaymentType(@Constants.MoneySourceTypes int productPaymentType) {
        this.productPaymentType = productPaymentType;
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
        return fromCardNumber;
    }

    public void setFromCardNumber(String fromCardNumber) {
        this.fromCardNumber = fromCardNumber;
    }
}
