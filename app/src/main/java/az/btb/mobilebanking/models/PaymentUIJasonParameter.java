package az.btb.mobilebanking.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PaymentUIJasonParameter {
    @SerializedName("parameterName")
    @Expose
    private String parameterName;
    @SerializedName("caption")
    @Expose
    private String caption;
    @SerializedName("type")
    @Expose
    private int type;
    @SerializedName("mask")
    @Expose
    private String mask;
    @SerializedName("length")
    @Expose
    private int length;
    @SerializedName("minLength")
    @Expose
    private int minLength;
    @SerializedName("maxLength")
    @Expose
    private int maxLength;
    @SerializedName("position")
    @Expose
    private int position;
    @SerializedName("value")
    @Expose
    private String value;
    @SerializedName("valueType")
    @Expose
    private int valueType;
    @SerializedName("itemValueType")
    @Expose
    private int itemValueType;
    @SerializedName("items")
    @Expose
    private List<Item> items;

    public String getParameterName() {
        return parameterName;
    }

    public void setParameterName(String parameterName) {
        this.parameterName = parameterName;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getMask() {
        return mask;
    }

    public void setMask(String mask) {
        this.mask = mask;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getMinLength() {
        return minLength;
    }

    public void setMinLength(int minLength) {
        this.minLength = minLength;
    }

    public int getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getValueType() {
        return valueType;
    }

    public void setValueType(int valueType) {
        this.valueType = valueType;
    }

    public int getItemValueType() {
        return itemValueType;
    }

    public void setItemValueType(int itemValueType) {
        this.itemValueType = itemValueType;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }
}
