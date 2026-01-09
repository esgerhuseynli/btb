package az.btb.mobilebanking.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BankCardStatementsResponse {

    @SerializedName("responceInfo")
    @Expose
    private ResponseInfo responseInfo;
    @SerializedName("bankCardStatement")
    @Expose
    private List<BankCardStatement> bankCardStatement = null;

    public BankCardStatementsResponse(ResponseInfo responseInfo, List<BankCardStatement> bankCardStatement) {
        this.responseInfo = responseInfo;
        this.bankCardStatement = bankCardStatement;
    }

    public ResponseInfo getResponseInfo() {
        return responseInfo;
    }

    public void setResponseInfo(ResponseInfo responseInfo) {
        this.responseInfo = responseInfo;
    }

    public List<BankCardStatement> getBankCardStatement() {
        return bankCardStatement;
    }

    public void setBankCardStatement(List<BankCardStatement> bankCardStatement) {
        this.bankCardStatement = bankCardStatement;
    }
}
