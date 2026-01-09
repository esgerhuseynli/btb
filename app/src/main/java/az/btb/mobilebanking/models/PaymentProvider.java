package az.btb.mobilebanking.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;

public class PaymentProvider {
    @SerializedName("idPaymentProvider")
    @Expose
    private int idPaymentProvider;
    @SerializedName("idSortPaymentProvider")
    @Expose
    private int idSortPaymentProvider;
    @SerializedName("idPaymentProviderGroup")
    @Expose
    private int idPaymentProviderGroup;
    @SerializedName("paymentProviderGroupName")
    @Expose
    private String paymentProviderGroupName;
    @SerializedName("paymentProviderName")
    @Expose
    private String paymentProviderName;
    @SerializedName("paymentProviderDescription")
    @Expose
    private String paymentProviderDescription;
    @SerializedName("paymentProviderImage")
    @Expose
    private String paymentProviderImage;
    @SerializedName("paymentProviderImageUrl")
    @Expose
    private String paymentProviderImageUrl;
    @SerializedName("paymentProviderStatus")
    @Expose
    private int paymentProviderStatus;
    @SerializedName("idProviderType")
    @Expose
    private int idProviderType;
    @SerializedName("providerTypeName")
    @Expose
    private String providerTypeName;
    @SerializedName("debtNotice")
    @Expose
    private int debtNotice;
    @SerializedName("performAutomatically")
    @Expose
    private int performAutomatically;
    @SerializedName("currency")
    @Expose
    private int currency;
    @SerializedName("minPaymentAmount")
    @Expose
    private BigDecimal minPaymentAmount;
    @SerializedName("maxPaymentAmount")
    @Expose
    private BigDecimal maxPaymentAmount;

    public PaymentProvider(int idPaymentProvider, int idSortPaymentProvider, int idPaymentProviderGroup, String paymentProviderGroupName, String paymentProviderName, String paymentProviderDescription, String paymentProviderImage, String paymentProviderImageUrl, int paymentProviderStatus, int idProviderType, String providerTypeName, int debtNotice, int performAutomatically, int currency, BigDecimal minPaymentAmount, BigDecimal maxPaymentAmount) {
        this.idPaymentProvider = idPaymentProvider;
        this.idSortPaymentProvider = idSortPaymentProvider;
        this.idPaymentProviderGroup = idPaymentProviderGroup;
        this.paymentProviderGroupName = paymentProviderGroupName;
        this.paymentProviderName = paymentProviderName;
        this.paymentProviderDescription = paymentProviderDescription;
        this.paymentProviderImage = paymentProviderImage;
        this.paymentProviderImageUrl = paymentProviderImageUrl;
        this.paymentProviderStatus = paymentProviderStatus;
        this.idProviderType = idProviderType;
        this.providerTypeName = providerTypeName;
        this.debtNotice = debtNotice;
        this.performAutomatically = performAutomatically;
        this.currency = currency;
        this.minPaymentAmount = minPaymentAmount;
        this.maxPaymentAmount = maxPaymentAmount;
    }

    public int getIdPaymentProvider() {
        return idPaymentProvider;
    }

    public void setIdPaymentProvider(int idPaymentProvider) {
        this.idPaymentProvider = idPaymentProvider;
    }

    public int getIdSortPaymentProvider() {
        return idSortPaymentProvider;
    }

    public void setIdSortPaymentProvider(int idSortPaymentProvider) {
        this.idSortPaymentProvider = idSortPaymentProvider;
    }

    public int getIdPaymentProviderGroup() {
        return idPaymentProviderGroup;
    }

    public void setIdPaymentProviderGroup(int idPaymentProviderGroup) {
        this.idPaymentProviderGroup = idPaymentProviderGroup;
    }

    public String getPaymentProviderGroupName() {
        return paymentProviderGroupName;
    }

    public void setPaymentProviderGroupName(String paymentProviderGroupName) {
        this.paymentProviderGroupName = paymentProviderGroupName;
    }

    public String getPaymentProviderName() {
        return paymentProviderName;
    }

    public void setPaymentProviderName(String paymentProviderName) {
        this.paymentProviderName = paymentProviderName;
    }

    public String getPaymentProviderDescription() {
        return paymentProviderDescription;
    }

    public void setPaymentProviderDescription(String paymentProviderDescription) {
        this.paymentProviderDescription = paymentProviderDescription;
    }

    public String getPaymentProviderImage() {
        return paymentProviderImage;
    }

    public void setPaymentProviderImage(String paymentProviderImage) {
        this.paymentProviderImage = paymentProviderImage;
    }

    public int getPaymentProviderStatus() {
        return paymentProviderStatus;
    }

    public void setPaymentProviderStatus(int paymentProviderStatus) {
        this.paymentProviderStatus = paymentProviderStatus;
    }

    public int getIdProviderType() {
        return idProviderType;
    }

    public void setIdProviderType(int idProviderType) {
        this.idProviderType = idProviderType;
    }

    public String getProviderTypeName() {
        return providerTypeName;
    }

    public void setProviderTypeName(String providerTypeName) {
        this.providerTypeName = providerTypeName;
    }

    public int getDebtNotice() {
        return debtNotice;
    }

    public void setDebtNotice(int debtNotice) {
        this.debtNotice = debtNotice;
    }

    public int getPerformAutomatically() {
        return performAutomatically;
    }

    public void setPerformAutomatically(int performAutomatically) {
        this.performAutomatically = performAutomatically;
    }

    public int getCurrency() {
        return currency;
    }

    public void setCurrency(int currency) {
        this.currency = currency;
    }

    public BigDecimal getMinPaymentAmount() {
        return minPaymentAmount;
    }

    public void setMinPaymentAmount(BigDecimal minPaymentAmount) {
        this.minPaymentAmount = minPaymentAmount;
    }

    public BigDecimal getMaxPaymentAmount() {
        return maxPaymentAmount;
    }

    public void setMaxPaymentAmount(BigDecimal maxPaymentAmount) {
        this.maxPaymentAmount = maxPaymentAmount;
    }

    public String getPaymentProviderImageUrl() {
        return paymentProviderImageUrl;
    }

    public void setPaymentProviderImageUrl(String paymentProviderImageUrl) {
        this.paymentProviderImageUrl = paymentProviderImageUrl;
    }
}
