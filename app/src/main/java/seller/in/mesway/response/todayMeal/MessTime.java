package seller.in.mesway.response.todayMeal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MessTime {

    @SerializedName("mess_id")
    @Expose
    private String messId;
    @SerializedName("breakfast_time")
    @Expose
    private String breakfastTime;
    @SerializedName("lunch_time")
    @Expose
    private String lunchTime;
    @SerializedName("dinner_time")
    @Expose
    private String dinnerTime;
    @SerializedName("created_timestamp")
    @Expose
    private String createdTimestamp;

    public String getMessId() {
        return messId;
    }

    public void setMessId(String messId) {
        this.messId = messId;
    }

    public String getBreakfastTime() {
        return breakfastTime;
    }

    public void setBreakfastTime(String breakfastTime) {
        this.breakfastTime = breakfastTime;
    }

    public String getLunchTime() {
        return lunchTime;
    }

    public void setLunchTime(String lunchTime) {
        this.lunchTime = lunchTime;
    }

    public String getDinnerTime() {
        return dinnerTime;
    }

    public void setDinnerTime(String dinnerTime) {
        this.dinnerTime = dinnerTime;
    }

    public String getCreatedTimestamp() {
        return createdTimestamp;
    }

    public void setCreatedTimestamp(String createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }
}
