package az.btb.mobilebanking.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ExchangeRatesResponse {
    @SerializedName("responceInfo")
    @Expose
    private ResponseInfo responseInfo;
    @SerializedName("bankExchangeRates")
    @Expose
    private List<BankExchangeRate> bankExchangeRates;

    public ExchangeRatesResponse(ResponseInfo responseInfo, List<BankExchangeRate> bankExchangeRates) {
        this.responseInfo = responseInfo;
        this.bankExchangeRates = bankExchangeRates;
    }

    public ResponseInfo getResponseInfo() {
        return responseInfo;
    }

    public void setResponseInfo(ResponseInfo responseInfo) {
        this.responseInfo = responseInfo;
    }

    public List<BankExchangeRate> getBankExchangeRates() {
        return bankExchangeRates;
    }

    public void setBankExchangeRates(List<BankExchangeRate> bankExchangeRates) {
        this.bankExchangeRates = bankExchangeRates;
    }
}
