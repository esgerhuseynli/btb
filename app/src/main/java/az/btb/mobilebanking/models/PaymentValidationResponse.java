package az.btb.mobilebanking.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PaymentValidationResponse {
    @SerializedName("responceInfo")
    @Expose
    private ResponseInfo responseInfo;
    @SerializedName("validatePaymentTemplateStatus")
    @Expose
    private int validatePaymentTemplateStatus;
    @SerializedName("paymentCommonInvoiceInfo")
    @Expose
    private PaymentCommonInvoiceInfo paymentCommonInvoiceInfo;

    public PaymentValidationResponse(ResponseInfo responseInfo, int validatePaymentTemplateStatus, PaymentCommonInvoiceInfo paymentCommonInvoiceInfo) {
        this.responseInfo = responseInfo;
        this.validatePaymentTemplateStatus = validatePaymentTemplateStatus;
        this.paymentCommonInvoiceInfo = paymentCommonInvoiceInfo;
    }

    public ResponseInfo getResponseInfo() {
        return responseInfo;
    }

    public void setResponseInfo(ResponseInfo responseInfo) {
        this.responseInfo = responseInfo;
    }

    public int getValidatePaymentTemplateStatus() {
        return validatePaymentTemplateStatus;
    }

    public void setValidatePaymentTemplateStatus(int validatePaymentTemplateStatus) {
        this.validatePaymentTemplateStatus = validatePaymentTemplateStatus;
    }

    public PaymentCommonInvoiceInfo getPaymentCommonInvoiceInfo() {
        return paymentCommonInvoiceInfo;
    }

    public void setPaymentCommonInvoiceInfo(PaymentCommonInvoiceInfo paymentCommonInvoiceInfo) {
        this.paymentCommonInvoiceInfo = paymentCommonInvoiceInfo;
    }
}
