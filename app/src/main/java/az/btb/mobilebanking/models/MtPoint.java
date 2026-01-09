package az.btb.mobilebanking.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MtPoint {

    @SerializedName("idPoint")
    @Expose
    private String idPoint;
    @SerializedName("idCity")
    @Expose
    private String idCity;
    @SerializedName("cityName")
    @Expose
    private String cityName;
    @SerializedName("countryName")
    @Expose
    private String countryName;
    @SerializedName("countryISO3Code")
    @Expose
    private String countryISO3Code;
    @SerializedName("mtUniqueName")
    @Expose
    private String mtUniqueName;
    @SerializedName("mtSystemName")
    @Expose
    private String mtSystemName;
    @SerializedName("pointAddress")
    @Expose
    private String pointAddress;
    @SerializedName("pointAvailability")
    @Expose
    private String pointAvailability;

    public MtPoint(String idPoint, String idCity, String cityName, String countryName, String countryISO3Code, String mtUniqueName, String mtSystemName, String pointAddress, String pointAvailability) {
        this.idPoint = idPoint;
        this.idCity = idCity;
        this.cityName = cityName;
        this.countryName = countryName;
        this.countryISO3Code = countryISO3Code;
        this.mtUniqueName = mtUniqueName;
        this.mtSystemName = mtSystemName;
        this.pointAddress = pointAddress;
        this.pointAvailability = pointAvailability;
    }

    public String getIdPoint() {
        return idPoint;
    }

    public void setIdPoint(String idPoint) {
        this.idPoint = idPoint;
    }

    public String getIdCity() {
        return idCity;
    }

    public void setIdCity(String idCity) {
        this.idCity = idCity;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCountryISO3Code() {
        return countryISO3Code;
    }

    public void setCountryISO3Code(String countryISO3Code) {
        this.countryISO3Code = countryISO3Code;
    }

    public String getMtUniqueName() {
        return mtUniqueName;
    }

    public void setMtUniqueName(String mtUniqueName) {
        this.mtUniqueName = mtUniqueName;
    }

    public String getMtSystemName() {
        return mtSystemName;
    }

    public void setMtSystemName(String mtSystemName) {
        this.mtSystemName = mtSystemName;
    }

    public String getPointAddress() {
        return pointAddress;
    }

    public void setPointAddress(String pointAddress) {
        this.pointAddress = pointAddress;
    }

    public String getPointAvailability() {
        return pointAvailability;
    }

    public void setPointAvailability(String pointAvailability) {
        this.pointAvailability = pointAvailability;
    }
}
