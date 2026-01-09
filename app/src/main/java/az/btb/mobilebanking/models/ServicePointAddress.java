package az.btb.mobilebanking.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ServicePointAddress {
    @SerializedName("cityName")
    @Expose
    private String cityName;
    @SerializedName("disctrictName")
    @Expose
    private String disctrictName;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("coordinateX")
    @Expose
    private String coordinateX;
    @SerializedName("coordinateY")
    @Expose
    private String coordinateY;

    public ServicePointAddress(String cityName, String disctrictName, String address, String coordinateX, String coordinateY) {
        this.cityName = cityName;
        this.disctrictName = disctrictName;
        this.address = address;
        this.coordinateX = coordinateX;
        this.coordinateY = coordinateY;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getDisctrictName() {
        return disctrictName;
    }

    public void setDisctrictName(String disctrictName) {
        this.disctrictName = disctrictName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCoordinateX() {
        return coordinateX;
    }

    public Double getX() {
        return Double.parseDouble(coordinateX.replace(",", ""));
    }

    public void setCoordinateX(String coordinateX) {
        this.coordinateX = coordinateX;
    }

    public String getCoordinateY() {
        return coordinateY;
    }

    public Double getY() {
        return Double.parseDouble(coordinateY.replace(",", ""));
    }

    public void setCoordinateY(String coordinateY) {
        this.coordinateY = coordinateY;
    }
}
