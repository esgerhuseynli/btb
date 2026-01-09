package az.btb.mobilebanking.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MoneyTransferPaymentPointsResponse {
    @SerializedName("responceInfo")
    @Expose
    private ResponseInfo responseInfo;
    @SerializedName("mtPointsType")
    @Expose
    private int mtPointsType;
    @SerializedName("mtPointCities")
    @Expose
    private List<MtPointCity> mtPointCities = null;
    @SerializedName("mtPoints")
    @Expose
    private List<MtPoint> mtPoints = null;

    public MoneyTransferPaymentPointsResponse(ResponseInfo responseInfo, int mtPointsType, List<MtPointCity> mtPointCities, List<MtPoint> mtPoints) {
        this.responseInfo = responseInfo;
        this.mtPointsType = mtPointsType;
        this.mtPointCities = mtPointCities;
        this.mtPoints = mtPoints;
    }

    public ResponseInfo getResponseInfo() {
        return responseInfo;
    }

    public void setResponseInfo(ResponseInfo responseInfo) {
        this.responseInfo = responseInfo;
    }

    public int getMtPointsType() {
        return mtPointsType;
    }

    public void setMtPointsType(int mtPointsType) {
        this.mtPointsType = mtPointsType;
    }

    public List<MtPointCity> getMtPointCities() {
        return mtPointCities;
    }

    public void setMtPointCities(List<MtPointCity> mtPointCities) {
        this.mtPointCities = mtPointCities;
    }

    public List<MtPoint> getMtPoints() {
        return mtPoints;
    }

    public void setMtPoints(List<MtPoint> mtPoints) {
        this.mtPoints = mtPoints;
    }
}
