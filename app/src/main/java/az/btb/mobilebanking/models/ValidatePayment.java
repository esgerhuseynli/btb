package az.btb.mobilebanking.models;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ValidatePayment {
    @SerializedName("ValidatePaymentMode")
    @Expose
    private int validatePaymentMode;
    @SerializedName("IdPaymentTemplate")
    @Expose
    private int idPaymentTemplate;
    @SerializedName("IdPaymentProvider")
    @Expose
    private int idPaymentProvider;
    @SerializedName("PaymentProviderRequestParameters")
    @Expose
    private List<PaymentProviderRequestParameter> paymentProviderRequestParameters;

    public ValidatePayment(int validatePaymentMode, int idPaymentTemplate, int idPaymentProvider, List<PaymentProviderRequestParameter> paymentProviderRequestParameters) {
        this.validatePaymentMode = validatePaymentMode;
        this.idPaymentTemplate = idPaymentTemplate;
        this.idPaymentProvider = idPaymentProvider;
        this.paymentProviderRequestParameters = paymentProviderRequestParameters;
    }

    public int getValidatePaymentMode() {
        return validatePaymentMode;
    }

    public void setValidatePaymentMode(int validatePaymentMode) {
        this.validatePaymentMode = validatePaymentMode;
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

    public List<PaymentProviderRequestParameter> getPaymentProviderRequestParameters() {
        return paymentProviderRequestParameters;
    }

    public void setPaymentProviderRequestParameters(List<PaymentProviderRequestParameter> paymentProviderRequestParameters) {
        this.paymentProviderRequestParameters = paymentProviderRequestParameters;
    }
}
