package az.btb.mobilebanking.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EmbassyCountriesResponse {
    @SerializedName("responceInfo")
    @Expose
    private ResponseInfo responseInfo;
    @SerializedName("embasyCountries")
    @Expose
    private List<EmbassyCountry> embassyCountries = null;

    public ResponseInfo getResponseInfo() {
        return responseInfo;
    }

    public void setResponseInfo(ResponseInfo responseInfo) {
        this.responseInfo = responseInfo;
    }

    public List<EmbassyCountry> getEmbassyCountries() {
        return embassyCountries;
    }

    public void setEmbassyCountries(List<EmbassyCountry> embassyCountries) {
        this.embassyCountries = embassyCountries;
    }
}
