package az.btb.mobilebanking.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductPlasticCardOrderResponse {
    @SerializedName("responceInfo")
    @Expose
    private ResponseInfo responseInfo;
    @SerializedName("plasticCardOrderStatus")
    @Expose
    private int plasticCardOrderStatus;

    public ResponseInfo getResponseInfo() {
        return responseInfo;
    }

    public void setResponseInfo(ResponseInfo responseInfo) {
        this.responseInfo = responseInfo;
    }

    public int getPlasticCardOrderStatus() {
        return plasticCardOrderStatus;
    }

    public void setPlasticCardOrderStatus(int plasticCardOrderStatus) {
        this.plasticCardOrderStatus = plasticCardOrderStatus;
    }
}
