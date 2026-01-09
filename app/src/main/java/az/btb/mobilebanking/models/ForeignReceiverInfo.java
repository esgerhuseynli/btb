package az.btb.mobilebanking.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ForeignReceiverInfo {
    @SerializedName("BeneficiaryBankSwiftCode")
    @Expose
    private String beneficiaryBankSwiftCode;
    @SerializedName("BeneficiaryBankName")
    @Expose
    private String beneficiaryBankName;
    @SerializedName("BeneficiaryBankCountry")
    @Expose
    private String beneficiaryBankCountry;
    @SerializedName("BeneficiaryBankCity")
    @Expose
    private String beneficiaryBankCity;
    @SerializedName("BeneficiaryBankCorrespondentAccount")
    @Expose
    private String beneficiaryBankCorrespondentAccount;
    @SerializedName("BeneficiaryAccountNumber")
    @Expose
    private String beneficiaryAccountNumber;
    @SerializedName("BeneficiaryAccountName")
    @Expose
    private String beneficiaryAccountName;
    @SerializedName("IntermediataryBankSwiftCode")
    @Expose
    private String intermediateBankSwiftCode;
    @SerializedName("IntermediataryBankName")
    @Expose
    private String intermediateBankName;
    @SerializedName("IntermediataryBankCountry")
    @Expose
    private String intermediateBankCountry;
    @SerializedName("IntermediataryBankCity")
    @Expose
    private String intermediateBankCity;
    @SerializedName("IntermediataryBankCorrespondentAccount")
    @Expose
    private String intermediateBankCorrespondentAccount;

    public ForeignReceiverInfo(String beneficiaryBankSwiftCode, String beneficiaryBankName, String beneficiaryBankCountry, String beneficiaryBankCity, String beneficiaryBankCorrespondentAccount, String beneficiaryAccountNumber, String beneficiaryAccountName, String intermediateBankSwiftCode, String intermediateBankName, String intermediateBankCountry, String intermediateBankCity, String intermediateBankCorrespondentAccount) {
        this.beneficiaryBankSwiftCode = beneficiaryBankSwiftCode;
        this.beneficiaryBankName = beneficiaryBankName;
        this.beneficiaryBankCountry = beneficiaryBankCountry;
        this.beneficiaryBankCity = beneficiaryBankCity;
        this.beneficiaryBankCorrespondentAccount = beneficiaryBankCorrespondentAccount;
        this.beneficiaryAccountNumber = beneficiaryAccountNumber;
        this.beneficiaryAccountName = beneficiaryAccountName;
        this.intermediateBankSwiftCode = intermediateBankSwiftCode;
        this.intermediateBankName = intermediateBankName;
        this.intermediateBankCountry = intermediateBankCountry;
        this.intermediateBankCity = intermediateBankCity;
        this.intermediateBankCorrespondentAccount = intermediateBankCorrespondentAccount;
    }

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

    public String getIntermediateBankSwiftCode() {
        return intermediateBankSwiftCode;
    }

    public void setIntermediateBankSwiftCode(String intermediateBankSwiftCode) {
        this.intermediateBankSwiftCode = intermediateBankSwiftCode;
    }

    public String getIntermediateBankName() {
        return intermediateBankName;
    }

    public void setIntermediateBankName(String intermediateBankName) {
        this.intermediateBankName = intermediateBankName;
    }

    public String getIntermediateBankCountry() {
        return intermediateBankCountry;
    }

    public void setIntermediateBankCountry(String intermediateBankCountry) {
        this.intermediateBankCountry = intermediateBankCountry;
    }

    public String getIntermediateBankCity() {
        return intermediateBankCity;
    }

    public void setIntermediateBankCity(String intermediateBankCity) {
        this.intermediateBankCity = intermediateBankCity;
    }

    public String getIntermediateBankCorrespondentAccount() {
        return intermediateBankCorrespondentAccount;
    }

    public void setIntermediateBankCorrespondentAccount(String intermediateBankCorrespondentAccount) {
        this.intermediateBankCorrespondentAccount = intermediateBankCorrespondentAccount;
    }
}
