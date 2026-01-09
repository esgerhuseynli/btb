package az.btb.mobilebanking.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PaymentProviderGroup {
    @SerializedName("idPaymentProviderGroup")
    @Expose
    private int idPaymentProviderGroup;
    @SerializedName("idSortPaymentProviderGroup")
    @Expose
    private int idSortPaymentProviderGroup;
    @SerializedName("paymentProviderGroupName")
    @Expose
    private String paymentProviderGroupName;
    @SerializedName("paymentProviderGroupDescription")
    @Expose
    private String paymentProviderGroupDescription;
    @SerializedName("paymentProviderGroupImage")
    @Expose
    private String paymentProviderGroupImage;
    @SerializedName("paymentProviderGroupImageUrl")
    @Expose
    private String paymentProviderGroupImageUrl;
    @SerializedName("paymentGroupStatus")
    @Expose
    private int paymentGroupStatus;

    public PaymentProviderGroup(int idPaymentProviderGroup, int idSortPaymentProviderGroup, String paymentProviderGroupName, String paymentProviderGroupDescription, String paymentProviderGroupImage, String paymentProviderGroupImageUrl, int paymentGroupStatus) {
        this.idPaymentProviderGroup = idPaymentProviderGroup;
        this.idSortPaymentProviderGroup = idSortPaymentProviderGroup;
        this.paymentProviderGroupName = paymentProviderGroupName;
        this.paymentProviderGroupDescription = paymentProviderGroupDescription;
        this.paymentProviderGroupImage = paymentProviderGroupImage;
        this.paymentProviderGroupImageUrl = paymentProviderGroupImageUrl;
        this.paymentGroupStatus = paymentGroupStatus;
    }

    public int getIdPaymentProviderGroup() {
        return idPaymentProviderGroup;
    }

    public void setIdPaymentProviderGroup(int idPaymentProviderGroup) {
        this.idPaymentProviderGroup = idPaymentProviderGroup;
    }

    public int getIdSortPaymentProviderGroup() {
        return idSortPaymentProviderGroup;
    }

    public void setIdSortPaymentProviderGroup(int idSortPaymentProviderGroup) {
        this.idSortPaymentProviderGroup = idSortPaymentProviderGroup;
    }

    public String getPaymentProviderGroupName() {
        return paymentProviderGroupName;
    }

    public void setPaymentProviderGroupName(String paymentProviderGroupName) {
        this.paymentProviderGroupName = paymentProviderGroupName;
    }

    public String getPaymentProviderGroupDescription() {
        return paymentProviderGroupDescription;
    }

    public void setPaymentProviderGroupDescription(String paymentProviderGroupDescription) {
        this.paymentProviderGroupDescription = paymentProviderGroupDescription;
    }

    public String getPaymentProviderGroupImage() {
        return paymentProviderGroupImage;
    }

    public void setPaymentProviderGroupImage(String paymentProviderGroupImage) {
        this.paymentProviderGroupImage = paymentProviderGroupImage;
    }

    public int getPaymentGroupStatus() {
        return paymentGroupStatus;
    }

    public void setPaymentGroupStatus(int paymentGroupStatus) {
        this.paymentGroupStatus = paymentGroupStatus;
    }

    public String getPaymentProviderGroupImageUrl() {
        return paymentProviderGroupImageUrl;
    }

    public void setPaymentProviderGroupImageUrl(String paymentProviderGroupImageUrl) {
        this.paymentProviderGroupImageUrl = paymentProviderGroupImageUrl;
    }
}
