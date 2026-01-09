package az.btb.mobilebanking.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.util.Date;

import az.btb.mobilebanking.utils.Utils;

public class UserNotification {
    @SerializedName("notificationID")
    @Expose
    private Integer notificationID;
    @SerializedName("notificationType")
    @Expose
    private Integer notificationType;
    @SerializedName("transactionInfoData")
    @Expose
    private TransactionInfoData transactionInfoData;
    @SerializedName("publishingDate")
    @Expose
    private String publishingDate;
    @SerializedName("header")
    @Expose
    private String header;
    @SerializedName("language")
    @Expose
    private Integer language;
    @SerializedName("text")
    @Expose
    private String text;
    @SerializedName("notificationLogoImage")
    @Expose
    private byte[] notificationLogoImage;

    public UserNotification(Integer notificationID, Integer notificationType, TransactionInfoData transactionInfoData, String publishingDate, String header, Integer language, String text, byte[] notificationLogoImage) {
        this.notificationID = notificationID;
        this.notificationType = notificationType;
        this.transactionInfoData = transactionInfoData;
        this.publishingDate = publishingDate;
        this.header = header;
        this.language = language;
        this.text = text;
        this.notificationLogoImage = notificationLogoImage;
    }

    public Integer getNotificationID() {
        return notificationID;
    }

    public void setNotificationID(Integer notificationID) {
        this.notificationID = notificationID;
    }

    public Integer getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(Integer notificationType) {
        this.notificationType = notificationType;
    }

    public TransactionInfoData getTransactionInfoData() {
        return transactionInfoData;
    }

    public void setTransactionInfoData(TransactionInfoData transactionInfoData) {
        this.transactionInfoData = transactionInfoData;
    }

    public String getPublishingDate() {
        return publishingDate;
    }

    public long getPublishTimestamp() {
        try {
            final Date time = Utils.dateFormatter.parse(publishingDate.substring(0, 10));
            return time == null ? new Date().getTime() : time.getTime();
        } catch (ParseException ignored) {
            return new Date().getTime();
        }
    }

    public String getPublishTime() {
        return publishingDate.substring(11, 16);
    }

    public void setPublishingDate(String publishingDate) {
        this.publishingDate = publishingDate;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public Integer getLanguage() {
        return language;
    }

    public void setLanguage(Integer language) {
        this.language = language;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public byte[] getNotificationLogoImage() {
        return notificationLogoImage;
    }

    public void setNotificationLogoImage(byte[] notificationLogoImage) {
        this.notificationLogoImage = notificationLogoImage;
    }
}
