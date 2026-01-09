package az.btb.mobilebanking.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MoneyTransferCountriesResponse {
    @SerializedName("responceInfo")
    @Expose
    private ResponseInfo responseInfo;
    @SerializedName("mtCountries")
    @Expose
    private List<MoneyTransferCountry> moneyTransferCountries;

    public MoneyTransferCountriesResponse(ResponseInfo responseInfo, List<MoneyTransferCountry> moneyTransferCountries) {
        this.responseInfo = responseInfo;
        this.moneyTransferCountries = moneyTransferCountries;
    }

    public ResponseInfo getResponseInfo() {
        return responseInfo;
    }

    public void setResponseInfo(ResponseInfo responseInfo) {
        this.responseInfo = responseInfo;
    }

    public List<MoneyTransferCountry> getMoneyTransferCountries() {
        return moneyTransferCountries;
    }

    public void setMoneyTransferCountries(List<MoneyTransferCountry> moneyTransferCountries) {
        this.moneyTransferCountries = moneyTransferCountries;
    }
}
