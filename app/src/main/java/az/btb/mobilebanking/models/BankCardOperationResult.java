package az.btb.mobilebanking.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BankCardOperationResult {

    @SerializedName("bankCardOperationStatus")
    @Expose
    private Integer bankCardOperationStatus;
    @SerializedName("rrn")
    @Expose
    private String rrn;
    @SerializedName("rrnRefill")
    @Expose
    private String rrnRefill;
    @SerializedName("idCardOperation")
    @Expose
    private Integer idCardOperation;

    public BankCardOperationResult(Integer bankCardOperationStatus, String rrn, String rrnRefill, Integer idCardOperation) {
        this.bankCardOperationStatus = bankCardOperationStatus;
        this.rrn = rrn;
        this.rrnRefill = rrnRefill;
        this.idCardOperation = idCardOperation;
    }

    public Integer getBankCardOperationStatus() {
        return bankCardOperationStatus;
    }

    public void setBankCardOperationStatus(Integer bankCardOperationStatus) {
        this.bankCardOperationStatus = bankCardOperationStatus;
    }

    public String getRrn() {
        return rrn;
    }

    public void setRrn(String rrn) {
        this.rrn = rrn;
    }

    public String getRrnRefill() {
        return rrnRefill;
    }

    public void setRrnRefill(String rrnRefill) {
        this.rrnRefill = rrnRefill;
    }

    public Integer getIdCardOperation() {
        return idCardOperation;
    }

    public void setIdCardOperation(Integer idCardOperation) {
        this.idCardOperation = idCardOperation;
    }
}
