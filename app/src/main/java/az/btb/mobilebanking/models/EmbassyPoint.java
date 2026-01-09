package az.btb.mobilebanking.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EmbassyPoint {
    @SerializedName("idEmbasy")
    @Expose
    private int idEmbassy;
    @SerializedName("idCountry")
    @Expose
    private int idCountry;
    @SerializedName("countryName")
    @Expose
    private String countryName;
    @SerializedName("embasyName")
    @Expose
    private String embassyName;
    @SerializedName("embasyAddress")
    @Expose
    private String embassyAddress;

    public int getIdEmbassy() {
        return idEmbassy;
    }

    public void setIdEmbassy(int idEmbassy) {
        this.idEmbassy = idEmbassy;
    }

    public int getIdCountry() {
        return idCountry;
    }

    public void setIdCountry(int idCountry) {
        this.idCountry = idCountry;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getEmbassyName() {
        return embassyName;
    }

    public void setEmbasyName(String embassyName) {
        this.embassyName = embassyName;
    }

    public String getEmbassyAddress() {
        return embassyAddress;
    }

    public void setEmbasyAddress(String embassyAddress) {
        this.embassyAddress = embassyAddress;
    }
}
