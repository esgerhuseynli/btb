package az.btb.mobilebanking.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WorkingHours {
    @SerializedName("from")
    @Expose
    private Integer from;
    @SerializedName("to")
    @Expose
    private Integer to;
    @SerializedName("exceptionFrom")
    @Expose
    private Integer exceptionFrom;
    @SerializedName("exceptionTo")
    @Expose
    private Integer exceptionTo;
    @SerializedName("exceptionDescription")
    @Expose
    private String exceptionDescription;

    public WorkingHours(Integer from, Integer to, Integer exceptionFrom, Integer exceptionTo, String exceptionDescription) {
        this.from = from;
        this.to = to;
        this.exceptionFrom = exceptionFrom;
        this.exceptionTo = exceptionTo;
        this.exceptionDescription = exceptionDescription;
    }

    public Integer getFrom() {
        return from;
    }

    public void setFrom(Integer from) {
        this.from = from;
    }

    public Integer getTo() {
        return to;
    }

    public void setTo(Integer to) {
        this.to = to;
    }

    public Integer getExceptionFrom() {
        return exceptionFrom;
    }

    public void setExceptionFrom(Integer exceptionFrom) {
        this.exceptionFrom = exceptionFrom;
    }

    public Integer getExceptionTo() {
        return exceptionTo;
    }

    public void setExceptionTo(Integer exceptionTo) {
        this.exceptionTo = exceptionTo;
    }

    public String getExceptionDescription() {
        return exceptionDescription;
    }

    public void setExceptionDescription(String exceptionDescription) {
        this.exceptionDescription = exceptionDescription;
    }
}