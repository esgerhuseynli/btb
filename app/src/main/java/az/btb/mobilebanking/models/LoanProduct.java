package az.btb.mobilebanking.models;

import androidx.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;
import java.util.List;

public class LoanProduct {
    @SerializedName("productId")
    @Expose
    private int productId;
    @SerializedName("productType")
    @Expose
    private int productType;
    @SerializedName("productSubType")
    @Expose
    private int productSubType;
    @SerializedName("productName")
    @Expose
    private String productName;
    @SerializedName("productHeaderName")
    @Expose
    private String productHeaderName;
    @SerializedName("productInformation")
    @Expose
    private String productInformation;
    @SerializedName("productTarif")
    @Expose
    private String productTarif;
    @SerializedName("productCost")
    @Expose
    private BigDecimal productCost;
    @SerializedName("productCostCurrency")
    @Expose
    private int productCostCurrency;
    @SerializedName("onlinePayment")
    @Expose
    private int onlinePayment;
    @SerializedName("productLogoImage")
    @Expose
    private String productLogoImage;
    @SerializedName("productSubTypeId")
    @Expose
    private int productSubTypeId;
    @SerializedName("productSubTypeName")
    @Expose
    private String productSubTypeName;
    @SerializedName("productSubTypeImage")
    @Expose
    @Nullable
    private String productSubTypeImage;
    @SerializedName("productConditions")
    @Expose
    private List<ProductConditions> productConditions;
    @SerializedName("productAvailableCurrencies")
    @Expose
    private List<Integer> productAvailableCurrencies;

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getProductType() {
        return productType;
    }

    public void setProductType(int productType) {
        this.productType = productType;
    }

    public int getProductSubType() {
        return productSubType;
    }

    public void setProductSubType(int productSubType) {
        this.productSubType = productSubType;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductHeaderName() {
        return productHeaderName;
    }

    public void setProductHeaderName(String productHeaderName) {
        this.productHeaderName = productHeaderName;
    }

    public String getProductInformation() {
        return productInformation;
    }

    public void setProductInformation(String productInformation) {
        this.productInformation = productInformation;
    }

    public String getProductTarif() {
        return productTarif;
    }

    public void setProductTarif(String productTarif) {
        this.productTarif = productTarif;
    }

    public BigDecimal getProductCost() {
        return productCost;
    }

    public void setProductCost(BigDecimal productCost) {
        this.productCost = productCost;
    }

    public int getProductCostCurrency() {
        return productCostCurrency;
    }

    public void setProductCostCurrency(int productCostCurrency) {
        this.productCostCurrency = productCostCurrency;
    }

    public int getOnlinePayment() {
        return onlinePayment;
    }

    public void setOnlinePayment(int onlinePayment) {
        this.onlinePayment = onlinePayment;
    }

    public String getProductLogoImage() {
        return productLogoImage;
    }

    public void setProductLogoImage(String productLogoImage) {
        this.productLogoImage = productLogoImage;
    }

    public int getProductSubTypeId() {
        return productSubTypeId;
    }

    public void setProductSubTypeId(int productSubTypeId) {
        this.productSubTypeId = productSubTypeId;
    }

    public String getProductSubTypeName() {
        return productSubTypeName;
    }

    public void setProductSubTypeName(String productSubTypeName) {
        this.productSubTypeName = productSubTypeName;
    }

    public @Nullable String getProductSubTypeImage() {
        return productSubTypeImage;
    }

    public void setProductSubTypeImage(@Nullable String productSubTypeImage) {
        this.productSubTypeImage = productSubTypeImage;
    }
    
    public List<ProductConditions> getProductConditions() {
        return productConditions;
    }
    
    public void setProductConditions(List<ProductConditions> productConditions) {
        this.productConditions = productConditions;
    }
    
    public List<Integer> getProductAvailableCurrencies() {
        return productAvailableCurrencies;
    }
    
    public void setProductAvailableCurrencies(List<Integer> productAvailableCurrencies) {
        this.productAvailableCurrencies = productAvailableCurrencies;
    }
}
