package az.btb.mobilebanking.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BudgetDestinationsResponse {
    @SerializedName("responceInfo")
    @Expose
    private ResponseInfo responseInfo;
    @SerializedName("budgetDestinations")
    @Expose
    private List<BudgetDestination> budgetDestinations;

    public ResponseInfo getResponseInfo() {
        return responseInfo;
    }

    public void setResponseInfo(ResponseInfo responseInfo) {
        this.responseInfo = responseInfo;
    }

    public List<BudgetDestination> getBudgetDestinations() {
        return budgetDestinations;
    }

    public void setBudgetDestinations(List<BudgetDestination> budgetDestinations) {
        this.budgetDestinations = budgetDestinations;
    }
}
