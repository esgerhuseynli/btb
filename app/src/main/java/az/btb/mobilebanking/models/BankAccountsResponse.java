package az.btb.mobilebanking.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BankAccountsResponse {

    @SerializedName("responceInfo")
    @Expose
    private ResponseInfo responseInfo;
    @SerializedName("bankAccounts")
    @Expose
    private List<BankAccount> bankAccounts;

    public ResponseInfo getResponseInfo() {
        return responseInfo;
    }

    public void setResponseInfo(ResponseInfo responseInfo) {
        this.responseInfo = responseInfo;
    }

    public List<BankAccount> getBankAccounts() {
        return bankAccounts;
    }

    public void setBankAccounts(List<BankAccount> bankCards) {
        this.bankAccounts = bankCards;
    }

    public BankAccountsResponse(ResponseInfo responseInfo, List<BankAccount> bankAccounts) {
        this.responseInfo = responseInfo;
        this.bankAccounts = bankAccounts;
    }
}