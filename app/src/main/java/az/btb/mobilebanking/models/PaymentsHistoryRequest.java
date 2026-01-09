package az.btb.mobilebanking.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PaymentsHistoryRequest {
    @SerializedName("RequestInfo")
    @Expose
    private RequestInfo requestInfo;
    @SerializedName("FromDate")
    @Expose
    private String fromDate;
    @SerializedName("ToDate")
    @Expose
    private String toDate;
    @SerializedName("IdPaymentTemplate")
    @Expose
    private int idPaymentTemplate;
    @SerializedName("idProviderGroup")
    @Expose
    private int idPaymentProviderGroup;
    @SerializedName("IdPaymentProvider")
    @Expose
    private int idPaymentProvider;
    @SerializedName("MobilePaymentStatus")
    @Expose
    private int mobilePaymentStatus;
    @SerializedName("FillingMethod")
    @Expose
    private int fillingMethod;
    @SerializedName("PayerInfo")
    @Expose
    private PaymentOperationPayerInfo payerInfo;

    public PaymentsHistoryRequest(RequestInfo requestInfo, String fromDate, String toDate, int idPaymentTemplate, int idPaymentProviderGroup, int idPaymentProvider, int mobilePaymentStatus, int fillingMethod, PaymentOperationPayerInfo payerInfo) {
        this.requestInfo = requestInfo;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.idPaymentTemplate = idPaymentTemplate;
        this.idPaymentProviderGroup = idPaymentProviderGroup;
        this.idPaymentProvider = idPaymentProvider;
        this.mobilePaymentStatus = mobilePaymentStatus;
        this.fillingMethod = fillingMethod;
        this.payerInfo = payerInfo;
    }

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public void setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public int getIdPaymentTemplate() {
        return idPaymentTemplate;
    }

    public void setIdPaymentTemplate(int idPaymentTemplate) {
        this.idPaymentTemplate = idPaymentTemplate;
    }
    
    public int getIdPaymentProviderGroup() {
        return idPaymentProviderGroup;
    }
    
    public void setIdPaymentProviderGroup(int idPaymentProviderGroup) {
        this.idPaymentProviderGroup = idPaymentProviderGroup;
    }
    
    public int getIdPaymentProvider() {
        return idPaymentProvider;
    }

    public void setIdPaymentProvider(int idPaymentProvider) {
        this.idPaymentProvider = idPaymentProvider;
    }

    public int getMobilePaymentStatus() {
        return mobilePaymentStatus;
    }

    public void setMobilePaymentStatus(int mobilePaymentStatus) {
        this.mobilePaymentStatus = mobilePaymentStatus;
    }

    public int getFillingMethod() {
        return fillingMethod;
    }

    public void setFillingMethod(int fillingMethod) {
        this.fillingMethod = fillingMethod;
    }

    public PaymentOperationPayerInfo getPayerInfo() {
        return payerInfo;
    }

    public void setPayerInfo(PaymentOperationPayerInfo payerInfo) {
        this.payerInfo = payerInfo;
    }
}
