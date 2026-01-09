package az.btb.mobilebanking.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LocalAndInternationalTransferResponse {
    @SerializedName("responceInfo")
    @Expose
    private ResponseInfo responseInfo;
    @SerializedName("transferNumber")
    @Expose
    private String transferNumber;
    @SerializedName("transferRRN")
    @Expose
    private String transferRRN;

    public ResponseInfo getResponseInfo() {
        return responseInfo;
    }

    public void setResponseInfo(ResponseInfo responseInfo) {
        this.responseInfo = responseInfo;
    }

    public String getTransferNumber() {
        return transferNumber;
    }

    public String getTransferRRN() {
        return transferRRN;
    }

    public void setTransferNumber(String transferNumber) {
        this.transferNumber = transferNumber;
    }

    public void setTransferRRN(String transferRRN) {
        this.transferRRN = transferRRN;
    }
}
