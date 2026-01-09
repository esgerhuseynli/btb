package az.btb.mobilebanking.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MtPointCity {

    @SerializedName("idCity")
    @Expose
    private String idCity;
    @SerializedName("cityName")
    @Expose
    private String cityName;
    @SerializedName("mtUniqueName")
    @Expose
    private String mtUniqueName;
    @SerializedName("mtSystemName")
    @Expose
    private String mtSystemName;
    @SerializedName("countryName")
    @Expose
    private String countryName;
    @SerializedName("countryISO3Code")
    @Expose
    private String countryISO3Code;

    public MtPointCity(String idCity, String cityName, String mtUniqueName, String mtSystemName, String countryName, String countryISO3Code) {
        this.idCity = idCity;
        this.cityName = cityName;
        this.mtUniqueName = mtUniqueName;
        this.mtSystemName = mtSystemName;
        this.countryName = countryName;
        this.countryISO3Code = countryISO3Code;
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
}
