package az.btb.mobilebanking.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ObsiyPaymentRequest {
    @SerializedName("RequestInfo")
    @Expose
    private RequestInfo requestInfo;
    @SerializedName("idPaymentProvider")
    @Expose
    private int idPaymentProvider;

    public ObsiyPaymentRequest(RequestInfo requestInfo, int idPaymentProvider) {
        super();
        this.requestInfo = requestInfo;
        this.idPaymentProvider = idPaymentProvider;
    }

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public void setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
    }

    public int getIdPaymentProvider() {
        return idPaymentProvider;
    }

    public void setIdPaymentProvider(int idPaymentProvider) {
        this.idPaymentProvider = idPaymentProvider;
    }
}
