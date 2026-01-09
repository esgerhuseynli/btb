package az.btb.mobilebanking.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BankBranchesResponse {
    @SerializedName("responceInfo")
    @Expose
    private ResponseInfo responseInfo;
    @SerializedName("bankBranches")
    @Expose
    private List<BankBranch> bankBranches;

    public BankBranchesResponse(ResponseInfo ResponseInfo, List<BankBranch> bankBranches) {
        this.responseInfo = ResponseInfo;
        this.bankBranches = bankBranches;
    }

    public ResponseInfo getResponseInfo() {
        return responseInfo;
    }

    public void setResponseInfo(ResponseInfo ResponseInfo) {
        this.responseInfo = ResponseInfo;
    }

    public List<BankBranch> getBankBranches() {
        return bankBranches;
    }

    public void setBankBranches(List<BankBranch> bankBranches) {
        this.bankBranches = bankBranches;
    }
}
