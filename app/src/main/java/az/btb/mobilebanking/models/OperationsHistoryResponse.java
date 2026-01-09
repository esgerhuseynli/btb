package az.btb.mobilebanking.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OperationsHistoryResponse {

    @SerializedName("responceInfo")
    @Expose
    private ResponseInfo responseInfo;
    @SerializedName("bankCardOperations")
    @Expose
    private List<BankCardOperation> bankCardOperations;

    public OperationsHistoryResponse(ResponseInfo responseInfo, List<BankCardOperation> bankCardOperations) {
        this.responseInfo = responseInfo;
        this.bankCardOperations = bankCardOperations;
    }

    public ResponseInfo getResponseInfo() {
        return responseInfo;
    }

    public void setResponseInfo(ResponseInfo responseInfo) {
        this.responseInfo = responseInfo;
    }

    public List<BankCardOperation> getBankCardOperations() {
        return bankCardOperations;
    }

    public void setBankCardOperations(List<BankCardOperation> bankCardOperations) {
        this.bankCardOperations = bankCardOperations;
    }

}
