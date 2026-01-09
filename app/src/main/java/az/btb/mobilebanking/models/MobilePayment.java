package az.btb.mobilebanking.models;

import androidx.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;

public class MobilePayment {
    @SerializedName("idMobilePayment")
    @Expose
    private int idMobilePayment;
    @SerializedName("idPaymentProvider")
    @Expose
    private int idPaymentProvider;
    @SerializedName("idPaymentTemplate")
    @Expose
    private int idPaymentTemplate;
    @SerializedName("paymentDateTime")
    @Expose
    private String paymentDateTime;
    @SerializedName("paymentDeclineDateTime")
    @Expose
    private String paymentDeclineDateTime;
    @SerializedName("paymentDeclineReason")
    @Expose
    private String paymentDeclineReason;
    @SerializedName("mobilePaymentStatus")
    @Expose
    private int mobilePaymentStatus;
    @SerializedName("payerInfo")
    @Expose
    private PaymentOperationPayerInfo payerInfo;
    @SerializedName("paymentAmount")
    @Expose
    private BigDecimal paymentAmount;
    @SerializedName("commissionAmount")
    @Expose
    private BigDecimal commissionAmount;
    @SerializedName("paymentCurrency")
    @Expose
    private int paymentCurrency;
    @SerializedName("fillingMethod")
    @Expose
    private int fillingMethod;
    @SerializedName("paymentProviderName")
    @Expose
    private String paymentProviderName;
    @SerializedName("idPaymentProviderType")
    @Expose
    private int idPaymentProviderType;
    @SerializedName("paymentProviderTypeName")
    @Expose
    private String paymentProviderTypeName;
    @SerializedName("paymentProviderImage")
    @Expose
    @Nullable
    private String paymentProviderImage;

    public int getIdMobilePayment() {
        return idMobilePayment;
    }

    public void setIdMobilePayment(int idMobilePayment) {
        this.idMobilePayment = idMobilePayment;
    }

    public int getIdPaymentProvider() {
        return idPaymentProvider;
    }

    public void setIdPaymentProvider(int idPaymentProvider) {
        this.idPaymentProvider = idPaymentProvider;
    }

    public int getIdPaymentTemplate() {
        return idPaymentTemplate;
    }

    public void setIdPaymentTemplate(int idPaymentTemplate) {
        this.idPaymentTemplate = idPaymentTemplate;
    }

    public String getPaymentDateTime() {
        return paymentDateTime;
    }

    public void setPaymentDateTime(String paymentDateTime) {
        this.paymentDateTime = paymentDateTime;
    }

    public String getPaymentDeclineDateTime() {
        return paymentDeclineDateTime;
    }

    public void setPaymentDeclineDateTime(String paymentDeclineDateTime) {
        this.paymentDeclineDateTime = paymentDeclineDateTime;
    }

    public String getPaymentDeclineReason() {
        return paymentDeclineReason;
    }

    public void setPaymentDeclineReason(String paymentDeclineReason) {
        this.paymentDeclineReason = paymentDeclineReason;
    }

    public int getMobilePaymentStatus() {
        return mobilePaymentStatus;
    }

    public void setMobilePaymentStatus(int mobilePaymentStatus) {
        this.mobilePaymentStatus = mobilePaymentStatus;
    }

    public PaymentOperationPayerInfo getPayerInfo() {
        return payerInfo;
    }

    public void setPayerInfo(PaymentOperationPayerInfo payerInfo) {
        this.payerInfo = payerInfo;
    }

    public BigDecimal getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(BigDecimal paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public BigDecimal getCommissionAmount() {
        return commissionAmount;
    }

    public void setCommissionAmount(BigDecimal commissionAmount) {
        this.commissionAmount = commissionAmount;
    }

    public int getPaymentCurrency() {
        return paymentCurrency;
    }

    public void setPaymentCurrency(int paymentCurrency) {
        this.paymentCurrency = paymentCurrency;
    }

    public int getFillingMethod() {
        return fillingMethod;
    }

    public void setFillingMethod(int fillingMethod) {
        this.fillingMethod = fillingMethod;
    }

    public String getPaymentProviderName() {
        return paymentProviderName;
    }

    public void setPaymentProviderName(String paymentProviderName) {
        this.paymentProviderName = paymentProviderName;
    }

    public int getIdPaymentProviderType() {
        return idPaymentProviderType;
    }

    public void setIdPaymentProviderType(int idPaymentProviderType) {
        this.idPaymentProviderType = idPaymentProviderType;
    }

    public String getPaymentProviderTypeName() {
        return paymentProviderTypeName;
    }

    public void setPaymentProviderTypeName(String paymentProviderTypeName) {
        this.paymentProviderTypeName = paymentProviderTypeName;
    }

    @Nullable
    public String getPaymentProviderImage() {
        return paymentProviderImage;
    }

    public void setPaymentProviderImage(@Nullable String paymentProviderImage) {
        this.paymentProviderImage = paymentProviderImage;
    }
}
