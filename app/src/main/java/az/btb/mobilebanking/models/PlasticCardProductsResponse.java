package az.btb.mobilebanking.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PlasticCardProductsResponse {
    @SerializedName("responceInfo")
    @Expose
    private ResponseInfo responseInfo;
    @SerializedName("plasticCardProducts")
    @Expose
    private List<PlasticCardProduct> products;

    public ResponseInfo getResponseInfo() {
        return responseInfo;
    }

    public void setResponseInfo(ResponseInfo responseInfo) {
        this.responseInfo = responseInfo;
    }

    public List<PlasticCardProduct> getProducts() {
        return products;
    }

    public void setProducts(List<PlasticCardProduct> plasticCardProducts) {
        this.products = plasticCardProducts;
    }
}
