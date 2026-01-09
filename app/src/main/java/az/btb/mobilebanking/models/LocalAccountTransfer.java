package az.btb.mobilebanking.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.math.BigDecimal;

public class LocalAccountTransfer implements Serializable {
    @SerializedName("operationDate")
    @Expose
    private String operationDate;
    @SerializedName("payerInfo")
    @Expose
    private PayerInfo payerInfo;
    @SerializedName("transferNumber")
    @Expose
    private String transferNumber;
    @SerializedName("localReceiverInfo")
    @Expose
    private LocalReceiverInfo localReceiverInfo;
    @SerializedName("transferAmount")
    @Expose
    private BigDecimal transferAmount;
    @SerializedName("operationDescription")
    @Expose
    private String operationDescription;
    @SerializedName("budgetPaymentInfo")
    @Expose
    private BudgetPaymentInfo budgetPaymentInfo;
    @SerializedName("localAccountTransferStatus")
    @Expose
    private int localAccountTransferStatus;
    @SerializedName("commentsBankOperator")
    @Expose
    private String commentsBankOperator;
    @SerializedName("idAccountPaymentTemplate")
    @Expose
    private int idAccountPaymentTemplate;

    public String getOperationDate() {
        return operationDate;
    }

    public void setOperationDate(String operationDate) {
        this.operationDate = operationDate;
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

    public int getLocalAccountTransferStatus() {
        return localAccountTransferStatus;
    }

    public void setLocalAccountTransferStatus(int localAccountTransferStatus) {
        this.localAccountTransferStatus = localAccountTransferStatus;
    }

    public String getCommentsBankOperator() {
        return commentsBankOperator;
    }

    public void setCommentsBankOperator(String commentsBankOperator) {
        this.commentsBankOperator = commentsBankOperator;
    }

    public int getIdAccountPaymentTemplate() {
        return idAccountPaymentTemplate;
    }

    public void setIdAccountPaymentTemplate(int idAccountPaymentTemplate) {
        this.idAccountPaymentTemplate = idAccountPaymentTemplate;
    }
}
