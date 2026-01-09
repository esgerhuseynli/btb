package az.btb.mobilebanking.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BankNewsResponse {
    @SerializedName("responceInfo")
    @Expose
    private ResponseInfo responseInfo;
    @SerializedName("bankNews")
    @Expose
    private List<BankNews> bankNews;

    public BankNewsResponse(ResponseInfo responseInfo, List<BankNews> bankNews) {
        this.responseInfo = responseInfo;
        this.bankNews = bankNews;
    }

    public ResponseInfo getResponseInfo() {
        return responseInfo;
    }

    public void setResponseInfo(ResponseInfo responseInfo) {
        this.responseInfo = responseInfo;
    }

    public List<BankNews> getBankNews() {
        return bankNews;
    }

    public void setBankNews(List<BankNews> bankNews) {
        this.bankNews = bankNews;
    }
}
