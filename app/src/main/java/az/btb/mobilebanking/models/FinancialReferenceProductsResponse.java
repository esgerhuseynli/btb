package az.btb.mobilebanking.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FinancialReferenceProductsResponse {
    @SerializedName("responceInfo")
    @Expose
    private ResponseInfo responseInfo;
    @SerializedName("financialReferenceProducts")
    @Expose
    private List<ReferenceProduct> products;

    public ResponseInfo getResponseInfo() {
        return responseInfo;
    }

    public void setResponseInfo(ResponseInfo responseInfo) {
        this.responseInfo = responseInfo;
    }

    public List<ReferenceProduct> getProducts() {
        return products;
    }

    public void setProducts(List<ReferenceProduct> products) {
        this.products = products;
    }
}
