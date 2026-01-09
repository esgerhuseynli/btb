package az.btb.mobilebanking.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseInfo {

    @SerializedName("responseType")
    @Expose
    private Integer responseType;
    @SerializedName("responseMessage")
    @Expose
    private String responseMessage;
    @SerializedName("errorCode")
    @Expose
    private Integer errorCode;
    @SerializedName("errorMessage")
    @Expose
    private String errorMessage;
    @SerializedName("saltSignature")
    @Expose
    private String saltSignature;

    public Integer getResponseType() {
        return responseType;
    }

    public void setResponseType(Integer responseType) {
        this.responseType = responseType;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getSaltSignature() {
        return saltSignature;
    }

    public void setSaltSignature(String saltSignature) {
        this.saltSignature = saltSignature;
    }

}
