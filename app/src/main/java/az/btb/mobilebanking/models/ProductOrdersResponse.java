package az.btb.mobilebanking.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProductOrdersResponse {
    @SerializedName("responceInfo")
    @Expose
    private ResponseInfo responseInfo;
    @SerializedName("mobileUserProductOrders")
    @Expose
    private List<ProductOrder> productOrders;

    public ResponseInfo getResponseInfo() {
        return responseInfo;
    }

    public void setResponseInfo(ResponseInfo responseInfo) {
        this.responseInfo = responseInfo;
    }

    public List<ProductOrder> getProductOrders() {
        return productOrders;
    }

    public void setMobileUserProductOrders(List<ProductOrder> productOrders) {
        this.productOrders = productOrders;
    }
}
