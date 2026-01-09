package az.btb.mobilebanking.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BudgetLevelsRequest {
    @SerializedName("RequestInfo")
    @Expose
    private RequestInfo requestInfo;
    @SerializedName("BudgetLevelCode")
    @Expose
    private String budgetLevelCode;

    public BudgetLevelsRequest(RequestInfo requestInfo, String budgetLevelCode) {
        this.requestInfo = requestInfo;
        this.budgetLevelCode = budgetLevelCode;
    }

    public String getBudgetLevelCode() {
        return budgetLevelCode;
    }

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public void setBudgetLevelCode(String budgetLevelCode) {
        this.budgetLevelCode = budgetLevelCode;
    }
    public void setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
    }
}
