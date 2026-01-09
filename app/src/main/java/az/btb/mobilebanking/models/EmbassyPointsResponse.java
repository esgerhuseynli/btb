package az.btb.mobilebanking.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EmbassyPointsResponse {
    @SerializedName("responceInfo")
    @Expose
    private ResponseInfo responseInfo;
    @SerializedName("embasyPoints")
    @Expose
    private List<EmbassyPoint> embassyPoints = null;

    public EmbassyPointsResponse(ResponseInfo responseInfo, List<EmbassyPoint> embassyPoints) {
        this.responseInfo = responseInfo;
        this.embassyPoints = embassyPoints;
    }

    public ResponseInfo getResponseInfo() {
        return responseInfo;
    }

    public void setResponseInfo(ResponseInfo responseInfo) {
        this.responseInfo = responseInfo;
    }

    public List<EmbassyPoint> getEmbassyPoints() {
        return embassyPoints;
    }

    public void setEmbasyPoints(List<EmbassyPoint> embassyPoints) {
        this.embassyPoints = embassyPoints;
    }
}
