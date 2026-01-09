package az.btb.mobilebanking.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BudgetDestinationsRequest {
    @SerializedName("RequestInfo")
    @Expose
    private RequestInfo requestInfo;
    @SerializedName("BudgetDestinationCode")
    @Expose
    private String budgetDestinationCode;

    public BudgetDestinationsRequest(RequestInfo requestInfo, String budgetDestinationCode) {
        this.requestInfo = requestInfo;
        this.budgetDestinationCode = budgetDestinationCode;
    }

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public void setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
    }

    public String getBudgetDestinationCode() {
        return budgetDestinationCode;
    }

    public void setBudgetDestinationCode(String budgetDestinationCode) {
        this.budgetDestinationCode = budgetDestinationCode;
    }
}
