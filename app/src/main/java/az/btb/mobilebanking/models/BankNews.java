package az.btb.mobilebanking.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import az.btb.mobilebanking.utils.Utils;

public class BankNews implements Serializable {
    @SerializedName("newsID")
    @Expose
    private Integer newsID;
    @SerializedName("publishingDate")
    @Expose
    private String publishingDate;
    @SerializedName("language")
    @Expose
    private Integer language;
    @SerializedName("header")
    @Expose
    private String header;
    @SerializedName("text")
    @Expose
    private String text;
    @SerializedName("newsLogoImage")
    @Expose
    private String newsLogoImage;
    @SerializedName("newsImages")
    @Expose
    private List<String> newsImages;
    @SerializedName("idCategory")
    @Expose
    private Integer idCategory;
    @SerializedName("categoryName")
    @Expose
    private String categoryName;

    public BankNews(Integer newsID, String publishingDate, Integer language, String header, String text, String newsLogoImage, List<String> newsImages, Integer idCategory, String categoryName) {
        this.newsID = newsID;
        this.publishingDate = publishingDate;
        this.language = language;
        this.header = header;
        this.text = text;
        this.newsLogoImage = newsLogoImage;
        this.newsImages = newsImages;
        this.idCategory = idCategory;
        this.categoryName = categoryName;
    }

    public Integer getNewsID() {
        return newsID;
    }

    public void setNewsID(Integer newsID) {
        this.newsID = newsID;
    }

    public String getPublishingDate() {
        return publishingDate.substring(0, 10);
    }

    public void setPublishingDate(String publishingDate) {
        this.publishingDate = publishingDate;
    }

//    public Date getPublishDate() {
//        try {
//            return Utils.dateFormatter.parse(publishingDate.substring(0, 10));
//        } catch (ParseException e) {
//            return new Date();
//        }
//    }

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

    public Integer getLanguage() {
        return language;
    }

    public void setLanguage(Integer language) {
        this.language = language;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getNewsLogoImage() {
        return newsLogoImage;
    }

    public void setNewsLogoImage(String newsLogoImage) {
        this.newsLogoImage = newsLogoImage;
    }

    public List<String> getNewsImages() {
        return newsImages;
    }

    public void setNewsImages(List<String> newsImages) {
        this.newsImages = newsImages;
    }

    public Integer getIdCategory() {
        return idCategory;
    }

    public void setIdCategory(Integer idCategory) {
        this.idCategory = idCategory;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
