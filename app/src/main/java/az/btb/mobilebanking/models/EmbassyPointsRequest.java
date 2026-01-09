package az.btb.mobilebanking.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EmbassyPointsRequest {
    @SerializedName("RequestInfo")
    @Expose
    private RequestInfo requestInfo;
    @SerializedName("IdEmbasyCountry")
    @Expose
    private int idEmbassyCountry;

    public EmbassyPointsRequest(RequestInfo requestInfo, int idEmbassyCountry) {
        super();
        this.requestInfo = requestInfo;
        this.idEmbassyCountry = idEmbassyCountry;
    }

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public void setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
    }

    public int getIdEmbassyCountry() {
        return idEmbassyCountry;
    }

    public void setIdEmbassyCountry(int idEmbassyCountry) {
        this.idEmbassyCountry = idEmbassyCountry;
    }
}
