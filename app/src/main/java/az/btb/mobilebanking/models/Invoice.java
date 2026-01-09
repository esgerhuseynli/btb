package az.btb.mobilebanking.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.math.BigDecimal;

public class Invoice implements Serializable {
    @SerializedName("idSort")
    @Expose
    private int idSort;
    @SerializedName("invoiceName")
    @Expose
    private String invoiceName;
    @SerializedName("invoicePaymentCode")
    @Expose
    private int invoicePaymentCode;
    @SerializedName("invoiceAmount")
    @Expose
    private BigDecimal invoiceAmount;
    @SerializedName("providerInvoicePaymentMode")
    @Expose
    private int providerInvoicePaymentMode; // Invoice payment mode. Enum type/values { None = 0, MustBePaid = 1, MayNotBePaid = 2 }
    @SerializedName("paymentTransactionNumber")
    @Expose
    private String paymentTransactionNumber;
    @SerializedName("invoiceCurrency")
    @Expose
    private int currency;
    @SerializedName("minPayableAmount")
    @Expose
    private BigDecimal minAmount;
    @SerializedName("maxPayableAmount")
    @Expose
    private BigDecimal maxAmount;
    @SerializedName("partialPayment")
    @Expose
    private int partialPayment; // Is payment could be done partially or not (Full invoice amount payment). Enum type/values { No = 0, Yes = 1}

    public Invoice(int idSort, String invoiceName, int invoicePaymentCode, BigDecimal invoiceAmount, int providerInvoicePaymentMode, String paymentTransactionNumber, int currency, BigDecimal minAmount, BigDecimal maxAmount, int partialPayment) {
        this.idSort = idSort;
        this.invoiceName = invoiceName;
        this.invoicePaymentCode = invoicePaymentCode;
        this.invoiceAmount = invoiceAmount;
        this.providerInvoicePaymentMode = providerInvoicePaymentMode;
        this.paymentTransactionNumber = paymentTransactionNumber;
        this.currency = currency;
        this.minAmount = minAmount;
        this.maxAmount = maxAmount;
        this.partialPayment = partialPayment;
    }

    public int getIdSort() {
        return idSort;
    }

    public void setIdSort(int idSort) {
        this.idSort = idSort;
    }

    public String getInvoiceName() {
        return invoiceName;
    }

    public void setInvoiceName(String invoiceName) {
        this.invoiceName = invoiceName;
    }

    public int getInvoicePaymentCode() {
        return invoicePaymentCode;
    }

    public void setInvoicePaymentCode(int invoicePaymentCode) {
        this.invoicePaymentCode = invoicePaymentCode;
    }

    public BigDecimal getInvoiceAmount() {
        return invoiceAmount;
    }

    public void setInvoiceAmount(BigDecimal invoiceAmount) {
        this.invoiceAmount = invoiceAmount;
    }

    public int getProviderInvoicePaymentMode() {
        return providerInvoicePaymentMode;
    }

    public void setProviderInvoicePaymentMode(int providerInvoicePaymentMode) {
        this.providerInvoicePaymentMode = providerInvoicePaymentMode;
    }

    public String getPaymentTransactionNumber() {
        return paymentTransactionNumber;
    }

    public void setPaymentTransactionNumber(String paymentTransactionNumber) {
        this.paymentTransactionNumber = paymentTransactionNumber;
    }
    
    public int getCurrency() {
        return currency;
    }
    
    public void setCurrency(int currency) {
        this.currency = currency;
    }
    
    public BigDecimal getMaxAmount() {
        return maxAmount;
    }
    
    public void setMaxAmount(BigDecimal maxAmount) {
        this.maxAmount = maxAmount;
    }
    
    public BigDecimal getMinAmount() {
        return minAmount;
    }
    
    public void setMinAmount(BigDecimal minAmount) {
        this.minAmount = minAmount;
    }
    
    public int getPartialPayment() {
        return partialPayment;
    }
    
    public void setPartialPayment(int partialPayment) {
        this.partialPayment = partialPayment;
    }
}
