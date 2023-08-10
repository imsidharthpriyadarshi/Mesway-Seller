
package seller.in.mesway.response.todayMeal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserInfo {

    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("full_name")
    @Expose
    private String fullName;
    @SerializedName("mobile_number")
    @Expose
    private String mobileNumber;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("email_verified")
    @Expose
    private Boolean emailVerified;
    @SerializedName("number_verified")
    @Expose
    private Boolean numberVerified;
    @SerializedName("reg_steps")
    @Expose
    private Integer regSteps;
    @SerializedName("subscription_count")
    @Expose
    private Integer subscriptionCount;
    @SerializedName("notification_token")
    @Expose
    private String notificationToken;
    @SerializedName("logout_form_all")
    @Expose
    private String logoutFormAll;
    @SerializedName("langitude")
    @Expose
    private String langitude;
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("location")
    @Expose
    private String location;
    @SerializedName("created_timestamp")
    @Expose
    private String createdTimestamp;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(Boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public Boolean getNumberVerified() {
        return numberVerified;
    }

    public void setNumberVerified(Boolean numberVerified) {
        this.numberVerified = numberVerified;
    }

    public Integer getRegSteps() {
        return regSteps;
    }

    public void setRegSteps(Integer regSteps) {
        this.regSteps = regSteps;
    }

    public Integer getSubscriptionCount() {
        return subscriptionCount;
    }

    public void setSubscriptionCount(Integer subscriptionCount) {
        this.subscriptionCount = subscriptionCount;
    }

    public String getNotificationToken() {
        return notificationToken;
    }

    public void setNotificationToken(String notificationToken) {
        this.notificationToken = notificationToken;
    }

    public String getLogoutFormAll() {
        return logoutFormAll;
    }

    public void setLogoutFormAll(String logoutFormAll) {
        this.logoutFormAll = logoutFormAll;
    }

    public String getLangitude() {
        return langitude;
    }

    public void setLangitude(String langitude) {
        this.langitude = langitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCreatedTimestamp() {
        return createdTimestamp;
    }

    public void setCreatedTimestamp(String createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(UserInfo.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("userId");
        sb.append('=');
        sb.append(((this.userId == null)?"<null>":this.userId));
        sb.append(',');
        sb.append("fullName");
        sb.append('=');
        sb.append(((this.fullName == null)?"<null>":this.fullName));
        sb.append(',');
        sb.append("mobileNumber");
        sb.append('=');
        sb.append(((this.mobileNumber == null)?"<null>":this.mobileNumber));
        sb.append(',');
        sb.append("email");
        sb.append('=');
        sb.append(((this.email == null)?"<null>":this.email));
        sb.append(',');
        sb.append("emailVerified");
        sb.append('=');
        sb.append(((this.emailVerified == null)?"<null>":this.emailVerified));
        sb.append(',');
        sb.append("numberVerified");
        sb.append('=');
        sb.append(((this.numberVerified == null)?"<null>":this.numberVerified));
        sb.append(',');
        sb.append("regSteps");
        sb.append('=');
        sb.append(((this.regSteps == null)?"<null>":this.regSteps));
        sb.append(',');
        sb.append("subscriptionCount");
        sb.append('=');
        sb.append(((this.subscriptionCount == null)?"<null>":this.subscriptionCount));
        sb.append(',');
        sb.append("notificationToken");
        sb.append('=');
        sb.append(((this.notificationToken == null)?"<null>":this.notificationToken));
        sb.append(',');
        sb.append("logoutFormAll");
        sb.append('=');
        sb.append(((this.logoutFormAll == null)?"<null>":this.logoutFormAll));
        sb.append(',');
        sb.append("langitude");
        sb.append('=');
        sb.append(((this.langitude == null)?"<null>":this.langitude));
        sb.append(',');
        sb.append("latitude");
        sb.append('=');
        sb.append(((this.latitude == null)?"<null>":this.latitude));
        sb.append(',');
        sb.append("location");
        sb.append('=');
        sb.append(((this.location == null)?"<null>":this.location));
        sb.append(',');
        sb.append("createdTimestamp");
        sb.append('=');
        sb.append(((this.createdTimestamp == null)?"<null>":this.createdTimestamp));
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

}
