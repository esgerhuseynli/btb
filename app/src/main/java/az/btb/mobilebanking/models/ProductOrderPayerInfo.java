package az.btb.mobilebanking.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ProductOrderPayerInfo implements Serializable {
    @SerializedName("productPaymentType")
    @Expose
    private int productPaymentType;
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
    private String fromCardNumber;

    public int getProductPaymentType() {
        return productPaymentType;
    }

    public void setProductPaymentType(int productPaymentType) {
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
