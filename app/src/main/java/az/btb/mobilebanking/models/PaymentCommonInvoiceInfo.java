package az.btb.mobilebanking.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class PaymentCommonInvoiceInfo implements Serializable {
    @SerializedName("payerName")
    @Expose
    private String payerName;
    @SerializedName("commonPaymentAmount")
    @Expose
    private BigDecimal commonPaymentAmount;
    @SerializedName("invoices")
    @Expose
    private List<Invoice> invoices;

    public PaymentCommonInvoiceInfo(String payerName, BigDecimal commonPaymentAmount, List<Invoice> invoices) {
        this.payerName = payerName;
        this.commonPaymentAmount = commonPaymentAmount;
        this.invoices = invoices;
    }

    public String getPayerName() {
        return payerName;
    }

    public void setPayerName(String payerName) {
        this.payerName = payerName;
    }

    public BigDecimal getCommonPaymentAmount() {
        return commonPaymentAmount;
    }

    public void setCommonPaymentAmount(BigDecimal commonPaymentAmount) {
        this.commonPaymentAmount = commonPaymentAmount;
    }

    public List<Invoice> getInvoices() {
        return invoices;
    }

    public void setInvoices(List<Invoice> invoices) {
        this.invoices = invoices;
    }
}
