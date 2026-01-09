package az.btb.mobilebanking.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PaymentProviderJSONParameter {
    @SerializedName("idPaymentProviderJSONParameters")
    @Expose
    private int idPaymentProviderJSONParameters;
    @SerializedName("idPaymentProvider")
    @Expose
    private int idPaymentProvider;
    @SerializedName("paymentProviderName")
    @Expose
    private String paymentProviderName;
    @SerializedName("paymentUIJasonParameters")
    @Expose
    private List<PaymentUIJasonParameter> paymentUIJasonParameters;

    public int getIdPaymentProviderJSONParameters() {
        return idPaymentProviderJSONParameters;
    }

    public void setIdPaymentProviderJSONParameters(int idPaymentProviderJSONParameters) {
        this.idPaymentProviderJSONParameters = idPaymentProviderJSONParameters;
    }

    public int getIdPaymentProvider() {
        return idPaymentProvider;
    }

    public void setIdPaymentProvider(int idPaymentProvider) {
        this.idPaymentProvider = idPaymentProvider;
    }

    public String getPaymentProviderName() {
        return paymentProviderName;
    }

    public void setPaymentProviderName(String paymentProviderName) {
        this.paymentProviderName = paymentProviderName;
    }

    public List<PaymentUIJasonParameter> getPaymentUIJasonParameters() {
        return paymentUIJasonParameters;
    }

    public void setPaymentUIJasonParameters(List<PaymentUIJasonParameter> paymentUIJasonParameters) {
        this.paymentUIJasonParameters = paymentUIJasonParameters;
    }
}
