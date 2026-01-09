package az.btb.mobilebanking.models;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class QrCodeValidationInfo implements Serializable {
    @SerializedName("qrCodeValidationResultType")
    @Expose
    private int qrCodeValidationResultType;
    @SerializedName("paymentType")
    @Expose
    private int paymentType;
    @SerializedName("paymentMode")
    @Expose
    private int paymentMode;
    @SerializedName("paymentProviderName")
    @Expose
    private String paymentProviderName;
    @SerializedName("paymentProviderTemplateName")
    @Expose
    private String paymentProviderTemplateName;
    @SerializedName("idPaymentProvider")
    @Expose
    private int idPaymentProvider;
    @SerializedName("idPaymentProviderTemplate")
    @Expose
    private int idPaymentProviderTemplate;
    @SerializedName("paymentProviderRequestParameters")
    @Expose
    private List<PaymentProviderRequestParameter> paymentProviderRequestParameters;

    public QrCodeValidationInfo(int qrCodeValidationResultType, int paymentType, int paymentMode, String paymentProviderName, String paymentProviderTemplateName, int idPaymentProvider, int idPaymentProviderTemplate, List<PaymentProviderRequestParameter> paymentProviderRequestParameters) {
        this.qrCodeValidationResultType = qrCodeValidationResultType;
        this.paymentType = paymentType;
        this.paymentMode = paymentMode;
        this.paymentProviderName = paymentProviderName;
        this.paymentProviderTemplateName = paymentProviderTemplateName;
        this.idPaymentProvider = idPaymentProvider;
        this.idPaymentProviderTemplate = idPaymentProviderTemplate;
        this.paymentProviderRequestParameters = paymentProviderRequestParameters;
    }

    public int getQrCodeValidationResultType() {
        return qrCodeValidationResultType;
    }

    public void setQrCodeValidationResultType(int qrCodeValidationResultType) {
        this.qrCodeValidationResultType = qrCodeValidationResultType;
    }

    public int getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(int paymentType) {
        this.paymentType = paymentType;
    }

    public int getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(int paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getPaymentProviderName() {
        return paymentProviderName;
    }

    public void setPaymentProviderName(String paymentProviderName) {
        this.paymentProviderName = paymentProviderName;
    }

    public String getPaymentProviderTemplateName() {
        return paymentProviderTemplateName;
    }

    public void setPaymentProviderTemplateName(String paymentProviderTemplateName) {
        this.paymentProviderTemplateName = paymentProviderTemplateName;
    }

    public int getIdPaymentProvider() {
        return idPaymentProvider;
    }

    public void setIdPaymentProvider(int idPaymentProvider) {
        this.idPaymentProvider = idPaymentProvider;
    }

    public int getIdPaymentProviderTemplate() {
        return idPaymentProviderTemplate;
    }

    public void setIdPaymentProviderTemplate(int idPaymentProviderTemplate) {
        this.idPaymentProviderTemplate = idPaymentProviderTemplate;
    }

    public List<PaymentProviderRequestParameter> getPaymentProviderRequestParameters() {
        return paymentProviderRequestParameters;
    }

    public void setPaymentProviderRequestParameters(List<PaymentProviderRequestParameter> paymentProviderRequestParameters) {
        this.paymentProviderRequestParameters = paymentProviderRequestParameters;
    }
}
