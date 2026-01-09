package az.btb.mobilebanking.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BudgetDestinationLevelsResponse {
    @SerializedName("responceInfo")
    @Expose
    private ResponseInfo responseInfo;
    @SerializedName("budgetLevels")
    @Expose
    private List<BudgetLevel> budgetLevels;

    public ResponseInfo getResponseInfo() {
        return responseInfo;
    }

    public void setResponseInfo(ResponseInfo responseInfo) {
        this.responseInfo = responseInfo;
    }

    public List<BudgetLevel> getBudgetLevels() {
        return budgetLevels;
    }

    public void setBudgetLevels(List<BudgetLevel> budgetLevels) {
        this.budgetLevels = budgetLevels;
    }
}
