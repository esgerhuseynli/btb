package az.btb.mobilebanking.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;

public class BankCardOperation {

    @SerializedName("idOperation")
    @Expose
    private Integer idOperation;
    @SerializedName("operationDate")
    @Expose
    private String operationDate;
    @SerializedName("bankCardToCardOperationType")
    @Expose
    private Integer bankCardToCardOperationType;
    @SerializedName("fromIdCard")
    @Expose
    private String fromIdCard;
    @SerializedName("toIdCard")
    @Expose
    private String toIdCard;
    @SerializedName("toCardNumber")
    @Expose
    private String toCardNumber;
    @SerializedName("amount")
    @Expose
    private BigDecimal amount;
    @SerializedName("currency")
    @Expose
    private Integer currency;
    @SerializedName("operationDescription")
    @Expose
    private String operationDescription;
    @SerializedName("bankCardOperationStatus")
    @Expose
    private Integer bankCardOperationStatus;
    @SerializedName("processedDate")
    @Expose
    private String processedDate;

    private String formattedFromCardNumber;

    public BankCardOperation(Integer idOperation, String operationDate, Integer bankCardToCardOperationType, String fromIdCard, String toIdCard, String toCardNumber, BigDecimal amount, Integer currency, String operationDescription, Integer bankCardOperationStatus, String processedDate) {
        this.idOperation = idOperation;
        this.operationDate = operationDate;
        this.bankCardToCardOperationType = bankCardToCardOperationType;
        this.fromIdCard = fromIdCard;
        this.toIdCard = toIdCard;
        this.toCardNumber = toCardNumber;
        this.amount = amount;
        this.currency = currency;
        this.operationDescription = operationDescription;
        this.bankCardOperationStatus = bankCardOperationStatus;
        this.processedDate = processedDate;
    }

    public Integer getIdOperation() {
        return idOperation;
    }

    public void setIdOperation(Integer idOperation) {
        this.idOperation = idOperation;
    }

    public String getOperationDate() {
        return operationDate;
    }

    public void setOperationDate(String operationDate) {
        this.operationDate = operationDate;
    }

    public Integer getBankCardToCardOperationType() {
        return bankCardToCardOperationType;
    }

    public void setBankCardToCardOperationType(Integer bankCardToCardOperationType) {
        this.bankCardToCardOperationType = bankCardToCardOperationType;
    }

    public String getFromIdCard() {
        return fromIdCard;
    }

    public void setFromIdCard(String fromIdCard) {
        this.fromIdCard = fromIdCard;
    }

    public String getToIdCard() {
        return toIdCard;
    }

    public void setToIdCard(String toIdCard) {
        this.toIdCard = toIdCard;
    }

    public String getToCardNumber() {
        return toCardNumber;
    }

    public void setToCardNumber(String toCardNumber) {
        this.toCardNumber = toCardNumber;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Integer getCurrency() {
        return currency;
    }

    public void setCurrency(Integer currency) {
        this.currency = currency;
    }

    public String getOperationDescription() {
        return operationDescription;
    }

    public void setOperationDescription(String operationDescription) {
        this.operationDescription = operationDescription;
    }

    public Integer getBankCardOperationStatus() {
        return bankCardOperationStatus;
    }

    public void setBankCardOperationStatus(Integer bankCardOperationStatus) {
        this.bankCardOperationStatus = bankCardOperationStatus;
    }

    public String getProcessedDate() {
        return processedDate;
    }

    public void setProcessedDate(String processedDate) {
        this.processedDate = processedDate;
    }

    public String getFormattedFromCardNumber() {
        return formattedFromCardNumber;
    }

    public void setFormattedFromCardNumber(String formattedFromCardNumber) {
        this.formattedFromCardNumber = formattedFromCardNumber;
    }
}
