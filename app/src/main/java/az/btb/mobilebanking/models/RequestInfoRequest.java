package az.btb.mobilebanking.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RequestInfoRequest {
    @SerializedName("RequestInfo")
    @Expose
    private RequestInfo requestInfo;

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public void setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
    }

    public RequestInfoRequest(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
    }
}
