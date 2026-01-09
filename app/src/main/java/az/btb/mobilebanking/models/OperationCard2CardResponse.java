package az.btb.mobilebanking.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OperationCard2CardResponse {
    @SerializedName("responceInfo")
    @Expose
    private ResponseInfo responseInfo;
    @SerializedName("bankCardOperationResult")
    @Expose
    private BankCardOperationResult bankCardOperationResult;

    public OperationCard2CardResponse(ResponseInfo responseInfo, BankCardOperationResult bankCardOperationResult) {
        this.responseInfo = responseInfo;
        this.bankCardOperationResult = bankCardOperationResult;
    }

    public ResponseInfo getResponseInfo() {
        return responseInfo;
    }

    public void setResponseInfo(ResponseInfo responseInfo) {
        this.responseInfo = responseInfo;
    }

    public BankCardOperationResult getBankCardOperationResult() {
        return bankCardOperationResult;
    }

    public void setBankCardOperationResult(BankCardOperationResult bankCardOperationResult) {
        this.bankCardOperationResult = bankCardOperationResult;
    }
}
