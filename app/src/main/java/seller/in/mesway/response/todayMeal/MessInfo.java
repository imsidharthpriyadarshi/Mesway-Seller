
package seller.in.mesway.response.todayMeal;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class MessInfo {

    @SerializedName("mess_id")
    @Expose
    private String messId;
    @SerializedName("mess_name")
    @Expose
    private String messName;
    @SerializedName("mess_phone_number")
    @Expose
    private String messPhoneNumber;
    @SerializedName("mess_email")
    @Expose
    private String messEmail;
    @SerializedName("email_verified")
    @Expose
    private String emailVerified;
    @SerializedName("number_verified")
    @Expose
    private String numberVerified;
    @SerializedName("mesway_verified")
    @Expose
    private String meswayVerified;
    @SerializedName("mess_reg_steps")
    @Expose
    private Integer messRegSteps;
    @SerializedName("mess_location")
    @Expose
    private List<MessLocation> messLocation;
    @SerializedName("mess_images")
    @Expose
    private List<MessImage> messImages;

    @SerializedName("mess_time")
    @Expose
    private List<MessTime> messTime;

    public List<MessTime> getMessTime() {
        return messTime;
    }

    public void setMessTime(List<MessTime> messTime) {
        this.messTime = messTime;
    }

    public String getMessId() {
        return messId;
    }

    public void setMessId(String messId) {
        this.messId = messId;
    }

    public String getMessName() {
        return messName;
    }

    public void setMessName(String messName) {
        this.messName = messName;
    }

    public String getMessPhoneNumber() {
        return messPhoneNumber;
    }

    public void setMessPhoneNumber(String messPhoneNumber) {
        this.messPhoneNumber = messPhoneNumber;
    }

    public String getMessEmail() {
        return messEmail;
    }

    public void setMessEmail(String messEmail) {
        this.messEmail = messEmail;
    }

    public String getEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(String emailVerified) {
        this.emailVerified = emailVerified;
    }

    public String getNumberVerified() {
        return numberVerified;
    }

    public void setNumberVerified(String numberVerified) {
        this.numberVerified = numberVerified;
    }

    public String getMeswayVerified() {
        return meswayVerified;
    }

    public void setMeswayVerified(String meswayVerified) {
        this.meswayVerified = meswayVerified;
    }

    public Integer getMessRegSteps() {
        return messRegSteps;
    }

    public void setMessRegSteps(Integer messRegSteps) {
        this.messRegSteps = messRegSteps;
    }

    public List<MessLocation> getMessLocation() {
        return messLocation;
    }

    public void setMessLocation(List<MessLocation> messLocation) {
        this.messLocation = messLocation;
    }

    public List<MessImage> getMessImages() {
        return messImages;
    }

    public void setMessImages(List<MessImage> messImages) {
        this.messImages = messImages;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(MessInfo.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("messId");
        sb.append('=');
        sb.append(((this.messId == null)?"<null>":this.messId));
        sb.append(',');
        sb.append("messName");
        sb.append('=');
        sb.append(((this.messName == null)?"<null>":this.messName));
        sb.append(',');
        sb.append("messPhoneNumber");
        sb.append('=');
        sb.append(((this.messPhoneNumber == null)?"<null>":this.messPhoneNumber));
        sb.append(',');
        sb.append("messEmail");
        sb.append('=');
        sb.append(((this.messEmail == null)?"<null>":this.messEmail));
        sb.append(',');
        sb.append("emailVerified");
        sb.append('=');
        sb.append(((this.emailVerified == null)?"<null>":this.emailVerified));
        sb.append(',');
        sb.append("numberVerified");
        sb.append('=');
        sb.append(((this.numberVerified == null)?"<null>":this.numberVerified));
        sb.append(',');
        sb.append("meswayVerified");
        sb.append('=');
        sb.append(((this.meswayVerified == null)?"<null>":this.meswayVerified));
        sb.append(',');
        sb.append("messRegSteps");
        sb.append('=');
        sb.append(((this.messRegSteps == null)?"<null>":this.messRegSteps));
        sb.append(',');
        sb.append("messLocation");
        sb.append('=');
        sb.append(((this.messLocation == null)?"<null>":this.messLocation));
        sb.append(',');
        sb.append("messImages");
        sb.append('=');
        sb.append(((this.messImages == null)?"<null>":this.messImages));
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

}
