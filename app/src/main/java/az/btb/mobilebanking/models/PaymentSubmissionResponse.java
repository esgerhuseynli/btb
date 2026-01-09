package az.btb.mobilebanking.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;
import java.util.List;

public class PaymentSubmissionResponse {
    @SerializedName("responceInfo")
    @Expose
    private ResponseInfo responseInfo;
    @SerializedName("idMobilePaymentRRN")
    @Expose
    private int idMobilePaymentRRN;
    @SerializedName("mobilePaymentStatus")
    @Expose
    private int mobilePaymentStatus;
    @SerializedName("mobilePaymentOperationDateTime")
    @Expose
    private String mobilePaymentOperationDateTime;
    @SerializedName("paidInvoices")
    @Expose
    private List<PaidInvoice> paidInvoices;
    @SerializedName("declinedPaymentAmount")
    @Expose
    private BigDecimal declinedPaymentAmount;

    public PaymentSubmissionResponse(ResponseInfo responseInfo, int idMobilePaymentRRN, int mobilePaymentStatus, String mobilePaymentOperationDateTime, List<PaidInvoice> paidInvoices, BigDecimal declinedPaymentAmount) {
        this.responseInfo = responseInfo;
        this.idMobilePaymentRRN = idMobilePaymentRRN;
        this.mobilePaymentStatus = mobilePaymentStatus;
        this.mobilePaymentOperationDateTime = mobilePaymentOperationDateTime;
        this.paidInvoices = paidInvoices;
        this.declinedPaymentAmount = declinedPaymentAmount;
    }

    public ResponseInfo getResponseInfo() {
        return responseInfo;
    }

    public void setResponseInfo(ResponseInfo responseInfo) {
        this.responseInfo = responseInfo;
    }

    public int getIdMobilePaymentRRN() {
        return idMobilePaymentRRN;
    }

    public void setIdMobilePaymentRRN(int idMobilePaymentRRN) {
        this.idMobilePaymentRRN = idMobilePaymentRRN;
    }

    public int getMobilePaymentStatus() {
        return mobilePaymentStatus;
    }

    public void setMobilePaymentStatus(int mobilePaymentStatus) {
        this.mobilePaymentStatus = mobilePaymentStatus;
    }

    public String getMobilePaymentOperationDateTime() {
        return mobilePaymentOperationDateTime;
    }

    public void setMobilePaymentOperationDateTime(String mobilePaymentOperationDateTime) {
        this.mobilePaymentOperationDateTime = mobilePaymentOperationDateTime;
    }

    public List<PaidInvoice> getPaidInvoices() {
        return paidInvoices;
    }

    public void setPaidInvoices(List<PaidInvoice> paidInvoices) {
        this.paidInvoices = paidInvoices;
    }

    public BigDecimal getDeclinedPaymentAmount() {
        return declinedPaymentAmount;
    }

    public void setDeclinedPaymentAmount(BigDecimal declinedPaymentAmount) {
        this.declinedPaymentAmount = declinedPaymentAmount;
    }
}
