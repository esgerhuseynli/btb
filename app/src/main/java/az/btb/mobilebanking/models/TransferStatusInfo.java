package az.btb.mobilebanking.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TransferStatusInfo {
    @SerializedName("mtTransferStatus")
    @Expose
    private int transferStatus;
    @SerializedName("transferDate")
    @Expose
    private String transferDate;
    @SerializedName("receiveDate")
    @Expose
    private String receiveDate;
    @SerializedName("declineDate")
    @Expose
    private String declineDate;

    public TransferStatusInfo(int transferStatus, String transferDate, String receiveDate, String declineDate) {
        this.transferStatus = transferStatus;
        this.transferDate = transferDate;
        this.receiveDate = receiveDate;
        this.declineDate = declineDate;
    }

    public int getTransferStatus() { return transferStatus; }

    public void setTransferStatus(int transferStatus) { this.transferStatus = transferStatus; }

    public String getTransferDate() { return transferDate; }

    public void setTransferDate(String transferDate) { this.transferDate = transferDate; }

    public String getReceiveDate() { return receiveDate; }

    public void setReceiveDate(String receiveDate) { this.receiveDate = receiveDate; }

    public String getDeclineDate() { return declineDate; }

    public void setDeclineDate(String declineDate) { this.declineDate = declineDate; }
}
