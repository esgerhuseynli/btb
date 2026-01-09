package az.btb.mobilebanking.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductOrdersRequest {
    @SerializedName("RequestInfo")
    @Expose
    private RequestInfo requestInfo;
    @SerializedName("IdOrder")
    @Expose
    private int idOrder;
    @SerializedName("ProductType")
    @Expose
    private int productType;
    @SerializedName("OrderStatus")
    @Expose
    private int orderStatus;

    public ProductOrdersRequest(RequestInfo requestInfo, int idOrder, int productType, int orderStatus) {
        super();
        this.requestInfo = requestInfo;
        this.idOrder = idOrder;
        this.productType = productType;
        this.orderStatus = orderStatus;
    }

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public void setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
    }

    public int getIdOrder() {
        return idOrder;
    }

    public void setIdOrder(int idOrder) {
        this.idOrder = idOrder;
    }

    public int getProductType() {
        return productType;
    }

    public void setProductType(int productType) {
        this.productType = productType;
    }

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }
}
