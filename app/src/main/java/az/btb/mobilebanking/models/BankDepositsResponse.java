package az.btb.mobilebanking.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BankDepositsResponse {
    @SerializedName("responceInfo")
    @Expose
    private ResponseInfo responseInfo;
    @SerializedName("bankDeposits")
    @Expose
    private List<BankDeposit> bankDeposits;

    public BankDepositsResponse(ResponseInfo responseInfo, List<BankDeposit> bankDeposits) {
        this.responseInfo = responseInfo;
        this.bankDeposits = bankDeposits;
    }

    public ResponseInfo getResponseInfo() {
        return responseInfo;
    }

    public void setResponseInfo(ResponseInfo responseInfo) {
        this.responseInfo = responseInfo;
    }

    public List<BankDeposit> getBankDeposits() {
        return bankDeposits;
    }

    public void setBankDeposits(List<BankDeposit> bankDeposits) {
        this.bankDeposits = bankDeposits;
    }
}
