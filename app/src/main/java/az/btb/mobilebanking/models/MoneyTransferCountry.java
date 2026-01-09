package az.btb.mobilebanking.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MoneyTransferCountry {
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
    @SerializedName("mtPointsType")
    @Expose
    private int mtPointsType;
    @SerializedName("nonLatinSymbols")
    @Expose
    private int nonLatinSymbols;

    public MoneyTransferCountry(String mtUniqueName, String mtSystemName, String countryName, String countryISO3Code, int mtPointsType, int nonLatinSymbols) {
        this.mtUniqueName = mtUniqueName;
        this.mtSystemName = mtSystemName;
        this.countryName = countryName;
        this.countryISO3Code = countryISO3Code;
        this.mtPointsType = mtPointsType;
        this.nonLatinSymbols = nonLatinSymbols;
    }

    public void setMtUniqueName(String mtUniqueName) {
        this.mtUniqueName = mtUniqueName;
    }

    public void setMtSystemName(String mtSystemName) {
        this.mtSystemName = mtSystemName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public void setCountryISO3Code(String countryISO3Code) {
        this.countryISO3Code = countryISO3Code;
    }

    public void setMtPointsType(int mtPointsType) {
        this.mtPointsType = mtPointsType;
    }

    public void setNonLatinSymbols(int nonLatinSymbols) {
        this.nonLatinSymbols = nonLatinSymbols;
    }

    public String getMtUniqueName() {
        return mtUniqueName;
    }

    public String getMtSystemName() {
        return mtSystemName;
    }

    public String getCountryName() {
        return countryName;
    }

    public String getCountryISO3Code() {
        return countryISO3Code;
    }

    public int getMtPointsType() {
        return mtPointsType;
    }

    public int getNonLatinSymbols() {
        return nonLatinSymbols;
    }
}
