package az.btb.mobilebanking.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;

public class LocalTransferRequest {
    @SerializedName("RequestInfo")
    @Expose
    private RequestInfo requestInfo;
    @SerializedName("PayerInfo")
    @Expose
    private PayerInfo payerInfo;
    @SerializedName("TransferNumber")
    @Expose
    private String transferNumber;
    @SerializedName("LocalReceiverInfo")
    @Expose
    private LocalReceiverInfo localReceiverInfo;
    @SerializedName("TransferAmount")
    @Expose
    private BigDecimal transferAmount;
    @SerializedName("OperationDescription")
    @Expose
    private String operationDescription;
    @SerializedName("BudgetPaymentInfo")
    @Expose
    private BudgetPaymentInfo budgetPaymentInfo;
    @SerializedName("IdAccountPaymentTemplate")
    @Expose
    private int idAccountPaymentTemplate;

    public LocalTransferRequest(RequestInfo requestInfo, PayerInfo payerInfo, String transferNumber, LocalReceiverInfo localReceiverInfo, BigDecimal transferAmount, String operationDescription, BudgetPaymentInfo budgetPaymentInfo, int idAccountPaymentTemplate) {
        this.requestInfo = requestInfo;
        this.payerInfo = payerInfo;
        this.transferNumber = transferNumber;
        this.localReceiverInfo = localReceiverInfo;
        this.transferAmount = transferAmount;
        this.operationDescription = operationDescription;
        this.budgetPaymentInfo = budgetPaymentInfo;
        this.idAccountPaymentTemplate = idAccountPaymentTemplate;
    }

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public void setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
    }

    public PayerInfo getPayerInfo() {
        return payerInfo;
    }

    public void setPayerInfo(PayerInfo payerInfo) {
        this.payerInfo = payerInfo;
    }

    public String getTransferNumber() {
        return transferNumber;
    }

    public void setTransferNumber(String transferNumber) {
        this.transferNumber = transferNumber;
    }

    public LocalReceiverInfo getLocalReceiverInfo() {
        return localReceiverInfo;
    }

    public void setLocalReceiverInfo(LocalReceiverInfo localReceiverInfo) {
        this.localReceiverInfo = localReceiverInfo;
    }

    public BigDecimal getTransferAmount() {
        return transferAmount;
    }

    public void setTransferAmount(BigDecimal transferAmount) {
        this.transferAmount = transferAmount;
    }

    public String getOperationDescription() {
        return operationDescription;
    }

    public void setOperationDescription(String operationDescription) {
        this.operationDescription = operationDescription;
    }

    public BudgetPaymentInfo getBudgetPaymentInfo() {
        return budgetPaymentInfo;
    }

    public void setBudgetPaymentInfo(BudgetPaymentInfo budgetPaymentInfo) {
        this.budgetPaymentInfo = budgetPaymentInfo;
    }

    public int getIdAccountPaymentTemplate() {
        return idAccountPaymentTemplate;
    }

    public void setIdAccountPaymentTemplate(int idAccountPaymentTemplate) {
        this.idAccountPaymentTemplate = idAccountPaymentTemplate;
    }
}
