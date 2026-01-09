package az.btb.mobilebanking.models;

import java.math.BigDecimal;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import az.btb.mobilebanking.utils.Constants;

public class PaymentInfo {
    @SerializedName("PaymentMode")
    @Expose
    private int paymentMode;
    @SerializedName("IdPaymentTemplate")
    @Expose
    private int idPaymentTemplate;
    @SerializedName("IdPaymentProvider")
    @Expose
    private int idPaymentProvider;
    @SerializedName("PaymentAmount")
    @Expose
    private BigDecimal paymentAmount;
    @SerializedName("PaymentInvoices")
    @Expose
    private List<Invoice> paymentInvoices;
    @SerializedName("PayerInfo")
    @Expose
    private PaymentOperationPayerInfo payerInfo;
    @SerializedName("FillingMethod")
    @Expose
    @Constants.PaymentDataFillingMethod
    private int fillingMethod;
    @SerializedName("QRCodeValue")
    @Expose
    private String qrCodeValue;

    public PaymentInfo(int paymentMode, int idPaymentTemplate, int idPaymentProvider, BigDecimal paymentAmount, List<Invoice> paymentInvoices, PaymentOperationPayerInfo payerInfo, @Constants.PaymentDataFillingMethod int fillingMethod, String qrCodeValue) {
        this.paymentMode = paymentMode;
        this.idPaymentTemplate = idPaymentTemplate;
        this.idPaymentProvider = idPaymentProvider;
        this.paymentAmount = paymentAmount;
        this.paymentInvoices = paymentInvoices;
        this.payerInfo = payerInfo;
        this.fillingMethod = fillingMethod;
        this.qrCodeValue = qrCodeValue;
    }

    public int getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(int paymentMode) {
        this.paymentMode = paymentMode;
    }

    public int getIdPaymentTemplate() {
        return idPaymentTemplate;
    }

    public void setIdPaymentTemplate(int idPaymentTemplate) {
        this.idPaymentTemplate = idPaymentTemplate;
    }

    public int getIdPaymentProvider() {
        return idPaymentProvider;
    }

    public void setIdPaymentProvider(int idPaymentProvider) {
        this.idPaymentProvider = idPaymentProvider;
    }

    public BigDecimal getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(BigDecimal paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public List<Invoice> getPaymentInvoices() {
        return paymentInvoices;
    }

    public void setPaymentInvoices(List<Invoice> paymentInvoices) {
        this.paymentInvoices = paymentInvoices;
    }

    public PaymentOperationPayerInfo getPayerInfo() {
        return payerInfo;
    }

    public void setPayerInfo(PaymentOperationPayerInfo payerInfo) {
        this.payerInfo = payerInfo;
    }

    public @Constants.PaymentDataFillingMethod int getFillingMethod() {
        return fillingMethod;
    }

    public void setFillingMethod(@Constants.PaymentDataFillingMethod int fillingMethod) {
        this.fillingMethod = fillingMethod;
    }

    public String getQRCodeValue() {
        return qrCodeValue;
    }

    public void setQRCodeValue(String qrCodeValue) {
        this.qrCodeValue = qrCodeValue;
    }
}
