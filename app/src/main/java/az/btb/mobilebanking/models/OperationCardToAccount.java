package az.btb.mobilebanking.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;

public class OperationCardToAccount {

    @SerializedName("BankCardToAccountOperationType")
    @Expose
    private int bankCardToAccountOperationType;
    @SerializedName("FromIdCard")
    @Expose
    private String fromIdCard;
    @SerializedName("ToIdCard")
    @Expose
    private String toIdCard;
    @SerializedName("ToCardNumber")
    @Expose
    private String toCardNumber;
    @SerializedName("FromIbanAccount")
    @Expose
    private String fromIbanAccount;
    @SerializedName("ToIbanAccount")
    @Expose
    private String toIbanAccount;
    @SerializedName("Amount")
    @Expose
    private BigDecimal amount;
    @SerializedName("OperationDescription")
    @Expose
    private String operationDescription;

    public OperationCardToAccount(int bankCardToAccountOperationType, String fromIdCard, String toIdCard, String toCardNumber, String fromIbanAccount, String toIbanAccount, BigDecimal amount, String operationDescription) {
        this.bankCardToAccountOperationType = bankCardToAccountOperationType;
        this.fromIdCard = fromIdCard;
        this.toIdCard = toIdCard;
        this.toCardNumber = toCardNumber;
        this.fromIbanAccount = fromIbanAccount;
        this.toIbanAccount = toIbanAccount;
        this.amount = amount;
        this.operationDescription = operationDescription;
    }

    public int getBankCardToAccountOperationType() {
        return bankCardToAccountOperationType;
    }

    public void setBankCardToAccountOperationType(int bankCardToAccountOperationType) {
        this.bankCardToAccountOperationType = bankCardToAccountOperationType;
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

    public String getFromIbanAccount() {
        return fromIbanAccount;
    }

    public void setFromIbanAccount(String fromIbanAccount) {
        this.fromIbanAccount = fromIbanAccount;
    }

    public String getToIbanAccount() {
        return toIbanAccount;
    }

    public void setToIbanAccount(String toIbanAccount) {
        this.toIbanAccount = toIbanAccount;
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
