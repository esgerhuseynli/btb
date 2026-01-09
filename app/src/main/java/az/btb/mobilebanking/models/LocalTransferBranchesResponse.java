package az.btb.mobilebanking.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LocalTransferBranchesResponse {
    @SerializedName("responceInfo")
    @Expose
    private ResponseInfo responseInfo;
    @SerializedName("localBranches")
    @Expose
    private List<LocalBankBranch> localBranches;

    public ResponseInfo getResponseInfo() {
        return responseInfo;
    }

    public void setResponseInfo(ResponseInfo responseInfo) {
        this.responseInfo = responseInfo;
    }

    public List<LocalBankBranch> getLocalBranches() {
        return localBranches;
    }

    public void setLocalBranches(List<LocalBankBranch> localBranches) {
        this.localBranches = localBranches;
    }
}
