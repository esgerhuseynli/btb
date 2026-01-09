package az.btb.mobilebanking.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;

public class OperationCard2Card {

    @SerializedName("BankCardToCardOperationType")
    @Expose
    private int bankCardToCardOperationType;
    @SerializedName("FromIdCard")
    @Expose
    private String fromIdCard;
    @SerializedName("ToIdCard")
    @Expose
    private String toIdCard;
    @SerializedName("ToCardNumber")
    @Expose
    private String toCardNumber;
    @SerializedName("Amount")
    @Expose
    private BigDecimal amount;
    @SerializedName("OperationDescription")
    @Expose
    private String operationDescription;

    public OperationCard2Card(int bankCardToCardOperationType, String fromIdCard, String toIdCard, String toCardNumber, BigDecimal amount, String operationDescription) {
        this.bankCardToCardOperationType = bankCardToCardOperationType;
        this.fromIdCard = fromIdCard;
        this.toIdCard = toIdCard;
        this.toCardNumber = toCardNumber;
        this.amount = amount;
        this.operationDescription = operationDescription;
    }

    public int getBankCardToCardOperationType() {
        return bankCardToCardOperationType;
    }

    public void setBankCardToCardOperationType(int bankCardToCardOperationType) {
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

    public String getOperationDescription() {
        return operationDescription;
    }

    public void setOperationDescription(String operationDescription) {
        this.operationDescription = operationDescription;
    }
}
