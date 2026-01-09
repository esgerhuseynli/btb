package az.btb.mobilebanking.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class BudgetPaymentInfo implements Serializable {

    @SerializedName("budgetPayment")
    @Expose
    private int budgetPayment;
    @SerializedName("budgetDestinationCode")
    @Expose
    private String budgetDestinationCode;
    @SerializedName("budgetLevelCode")
    @Expose
    private String budgetLevelCode;

    public BudgetPaymentInfo(int budgetPayment, String budgetDestinationCode, String budgetLevelCode) {
        this.budgetPayment = budgetPayment;
        this.budgetDestinationCode = budgetDestinationCode;
        this.budgetLevelCode = budgetLevelCode;
    }

    public int getBudgetPayment() {
        return budgetPayment;
    }

    public void setBudgetPayment(int budgetPayment) {
        this.budgetPayment = budgetPayment;
    }

    public String getBudgetDestinationCode() {
        return budgetDestinationCode;
    }

    public void setBudgetDestinationCode(String budgetDestinationCode) {
        this.budgetDestinationCode = budgetDestinationCode;
    }

    public String getBudgetLevelCode() {
        return budgetLevelCode;
    }

    public void setBudgetLevelCode(String budgetLevelCode) {
        this.budgetLevelCode = budgetLevelCode;
    }
}
