package az.btb.mobilebanking.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ForeignReceiverInfoHistory implements Serializable {
    @SerializedName("beneficiaryBankSwiftCode")
    @Expose
    private String beneficiaryBankSwiftCode;
    @SerializedName("beneficiaryBankName")
    @Expose
    private String beneficiaryBankName;
    @SerializedName("beneficiaryBankCountry")
    @Expose
    private String beneficiaryBankCountry;
    @SerializedName("beneficiaryBankCity")
    @Expose
    private String beneficiaryBankCity;
    @SerializedName("beneficiaryBankCorrespondentAccount")
    @Expose
    private String beneficiaryBankCorrespondentAccount;
    @SerializedName("beneficiaryAccountNumber")
    @Expose
    private String beneficiaryAccountNumber;
    @SerializedName("beneficiaryAccountName")
    @Expose
    private String beneficiaryAccountName;
    @SerializedName("intermediataryBankSwiftCode")
    @Expose
    private String intermediataryBankSwiftCode;
    @SerializedName("intermediataryBankName")
    @Expose
    private String intermediataryBankName;
    @SerializedName("intermediataryBankCountry")
    @Expose
    private String intermediataryBankCountry;
    @SerializedName("intermediataryBankCity")
    @Expose
    private String intermediataryBankCity;
    @SerializedName("intermediataryBankCorrespondentAccount")
    @Expose
    private String intermediataryBankCorrespondentAccount;

    public String getBeneficiaryBankSwiftCode() {
        return beneficiaryBankSwiftCode;
    }

    public void setBeneficiaryBankSwiftCode(String beneficiaryBankSwiftCode) {
        this.beneficiaryBankSwiftCode = beneficiaryBankSwiftCode;
    }

    public String getBeneficiaryBankName() {
        return beneficiaryBankName;
    }

    public void setBeneficiaryBankName(String beneficiaryBankName) {
        this.beneficiaryBankName = beneficiaryBankName;
    }

    public String getBeneficiaryBankCountry() {
        return beneficiaryBankCountry;
    }

    public void setBeneficiaryBankCountry(String beneficiaryBankCountry) {
        this.beneficiaryBankCountry = beneficiaryBankCountry;
    }

    public String getBeneficiaryBankCity() {
        return beneficiaryBankCity;
    }

    public void setBeneficiaryBankCity(String beneficiaryBankCity) {
        this.beneficiaryBankCity = beneficiaryBankCity;
    }

    public String getBeneficiaryBankCorrespondentAccount() {
        return beneficiaryBankCorrespondentAccount;
    }

    public void setBeneficiaryBankCorrespondentAccount(String beneficiaryBankCorrespondentAccount) {
        this.beneficiaryBankCorrespondentAccount = beneficiaryBankCorrespondentAccount;
    }

    public String getBeneficiaryAccountNumber() {
        return beneficiaryAccountNumber;
    }

    public void setBeneficiaryAccountNumber(String beneficiaryAccountNumber) {
        this.beneficiaryAccountNumber = beneficiaryAccountNumber;
    }

    public String getBeneficiaryAccountName() {
        return beneficiaryAccountName;
    }

    public void setBeneficiaryAccountName(String beneficiaryAccountName) {
        this.beneficiaryAccountName = beneficiaryAccountName;
    }

    public String getIntermediataryBankSwiftCode() {
        return intermediataryBankSwiftCode;
    }

    public void setIntermediataryBankSwiftCode(String intermediataryBankSwiftCode) {
        this.intermediataryBankSwiftCode = intermediataryBankSwiftCode;
    }

    public String getIntermediataryBankName() {
        return intermediataryBankName;
    }

    public void setIntermediataryBankName(String intermediataryBankName) {
        this.intermediataryBankName = intermediataryBankName;
    }

    public String getIntermediataryBankCountry() {
        return intermediataryBankCountry;
    }

    public void setIntermediataryBankCountry(String intermediataryBankCountry) {
        this.intermediataryBankCountry = intermediataryBankCountry;
    }

    public String getIntermediataryBankCity() {
        return intermediataryBankCity;
    }

    public void setIntermediataryBankCity(String intermediataryBankCity) {
        this.intermediataryBankCity = intermediataryBankCity;
    }

    public String getIntermediataryBankCorrespondentAccount() {
        return intermediataryBankCorrespondentAccount;
    }

    public void setIntermediataryBankCorrespondentAccount(String intermediataryBankCorrespondentAccount) {
        this.intermediataryBankCorrespondentAccount = intermediataryBankCorrespondentAccount;
    }
}
