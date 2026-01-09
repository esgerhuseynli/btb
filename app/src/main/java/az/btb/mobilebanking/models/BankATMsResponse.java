package az.btb.mobilebanking.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BankATMsResponse {
    @SerializedName("responceInfo")
    @Expose
    private ResponseInfo responseInfo;
    @SerializedName("bankATMS")
    @Expose
    private List<BankATM> bankATMs;

    public BankATMsResponse(ResponseInfo responseInfo, List<BankATM> bankATMs) {
        this.responseInfo = responseInfo;
        this.bankATMs = bankATMs;
    }

    public ResponseInfo getResponseInfo() {
        return responseInfo;
    }

    public void setResponseInfo(ResponseInfo responseInfo) {
        this.responseInfo = responseInfo;
    }

    public List<BankATM> getBankATMs() {
        return bankATMs;
    }

    public void setBankATMs(List<BankATM> bankATMs) {
        this.bankATMs = bankATMs;
    }
}
