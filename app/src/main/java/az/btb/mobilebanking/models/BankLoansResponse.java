package az.btb.mobilebanking.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BankLoansResponse {
    @SerializedName("responceInfo")
    @Expose
    private ResponseInfo responseInfo;
    @SerializedName("bankLoans")
    @Expose
    private List<BankLoan> bankLoans;

    public BankLoansResponse(ResponseInfo responseInfo, List<BankLoan> bankLoans) {
        this.responseInfo = responseInfo;
        this.bankLoans = bankLoans;
    }

    public ResponseInfo getResponseInfo() {
        return responseInfo;
    }

    public void setResponseInfo(ResponseInfo responseInfo) {
        this.responseInfo = responseInfo;
    }

    public List<BankLoan> getBankLoans() {
        return bankLoans;
    }

    public void setBankLoans(List<BankLoan> bankLoans) {
        this.bankLoans = bankLoans;
    }
}
