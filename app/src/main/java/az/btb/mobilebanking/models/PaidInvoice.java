package az.btb.mobilebanking.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;

public class PaidInvoice {
    @SerializedName("paidInvoiceStatus")
    @Expose
    private int paidInvoiceStatus;
    @SerializedName("paidInvoicePaymentTransactionNumber")
    @Expose
    private String paidInvoicePaymentTransactionNumber;
    @SerializedName("paidInvoiceOrderNumber")
    @Expose
    private int paidInvoiceOrderNumber;
    @SerializedName("paidInvoiceOperationDateTime")
    @Expose
    private String paidInvoiceOperationDateTime;
    @SerializedName("paidInvoicePaymentAmount")
    @Expose
    private BigDecimal paidInvoicePaymentAmount;

    public PaidInvoice(int paidInvoiceStatus, String paidInvoicePaymentTransactionNumber, int paidInvoiceOrderNumber, String paidInvoiceOperationDateTime, BigDecimal paidInvoicePaymentAmount) {
        this.paidInvoiceStatus = paidInvoiceStatus;
        this.paidInvoicePaymentTransactionNumber = paidInvoicePaymentTransactionNumber;
        this.paidInvoiceOrderNumber = paidInvoiceOrderNumber;
        this.paidInvoiceOperationDateTime = paidInvoiceOperationDateTime;
        this.paidInvoicePaymentAmount = paidInvoicePaymentAmount;
    }

    public int getPaidInvoiceStatus() {
        return paidInvoiceStatus;
    }

    public void setPaidInvoiceStatus(int paidInvoiceStatus) {
        this.paidInvoiceStatus = paidInvoiceStatus;
    }

    public String getPaidInvoicePaymentTransactionNumber() {
        return paidInvoicePaymentTransactionNumber;
    }

    public void setPaidInvoicePaymentTransactionNumber(String paidInvoicePaymentTransactionNumber) {
        this.paidInvoicePaymentTransactionNumber = paidInvoicePaymentTransactionNumber;
    }

    public int getPaidInvoiceOrderNumber() {
        return paidInvoiceOrderNumber;
    }

    public void setPaidInvoiceOrderNumber(int paidInvoiceOrderNumber) {
        this.paidInvoiceOrderNumber = paidInvoiceOrderNumber;
    }

    public String getPaidInvoiceOperationDateTime() {
        return paidInvoiceOperationDateTime;
    }

    public void setPaidInvoiceOperationDateTime(String paidInvoiceOperationDateTime) {
        this.paidInvoiceOperationDateTime = paidInvoiceOperationDateTime;
    }

    public BigDecimal getPaidInvoicePaymentAmount() {
        return paidInvoicePaymentAmount;
    }

    public void setPaidInvoicePaymentAmount(BigDecimal paidInvoicePaymentAmount) {
        this.paidInvoicePaymentAmount = paidInvoicePaymentAmount;
    }
}
