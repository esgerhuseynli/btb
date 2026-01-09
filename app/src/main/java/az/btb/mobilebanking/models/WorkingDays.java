package az.btb.mobilebanking.models;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WorkingDays {
    @SerializedName("from")
    @Expose
    private Integer from;
    @SerializedName("to")
    @Expose
    private Integer to;
    @SerializedName("dayOffs")
    @Expose
    private List<Integer> dayOffs;

    public WorkingDays(Integer from, Integer to, List<Integer> dayOffs) {
        this.from = from;
        this.to = to;
        this.dayOffs = dayOffs;
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

    public List<Integer> getDayOffs() {
        return dayOffs;
    }

    public void setDayOffs(List<Integer> dayOffs) {
        this.dayOffs = dayOffs;
    }
}
