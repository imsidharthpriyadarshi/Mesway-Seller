
package seller.in.mesway.response.todayMeal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserSubscriptionLocation {

    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("full_name")
    @Expose
    private String fullName;
    @SerializedName("mobile_number")
    @Expose
    private String mobileNumber;
    @SerializedName("location_type")
    @Expose
    private String locationType;
    @SerializedName("building_no")
    @Expose
    private String buildingNo;
    @SerializedName("full_address")
    @Expose
    private String fullAddress;
    @SerializedName("landmark")
    @Expose
    private String landmark;
    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("longitude")
    @Expose
    private String longitude;
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("district")
    @Expose
    private String district;
    @SerializedName("pin_code")
    @Expose
    private String pinCode;
    @SerializedName("location_id")
    @Expose
    private String locationId;
    @SerializedName("is_deleted")
    @Expose
    private Boolean isDeleted;

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

    public String getLocationType() {
        return locationType;
    }

    public void setLocationType(String locationType) {
        this.locationType = locationType;
    }

    public String getBuildingNo() {
        return buildingNo;
    }

    public void setBuildingNo(String buildingNo) {
        this.buildingNo = buildingNo;
    }

    public String getFullAddress() {
        return fullAddress;
    }

    public void setFullAddress(String fullAddress) {
        this.fullAddress = fullAddress;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(UserSubscriptionLocation.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
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
        sb.append("locationType");
        sb.append('=');
        sb.append(((this.locationType == null)?"<null>":this.locationType));
        sb.append(',');
        sb.append("buildingNo");
        sb.append('=');
        sb.append(((this.buildingNo == null)?"<null>":this.buildingNo));
        sb.append(',');
        sb.append("fullAddress");
        sb.append('=');
        sb.append(((this.fullAddress == null)?"<null>":this.fullAddress));
        sb.append(',');
        sb.append("landmark");
        sb.append('=');
        sb.append(((this.landmark == null)?"<null>":this.landmark));
        sb.append(',');
        sb.append("state");
        sb.append('=');
        sb.append(((this.state == null)?"<null>":this.state));
        sb.append(',');
        sb.append("longitude");
        sb.append('=');
        sb.append(((this.longitude == null)?"<null>":this.longitude));
        sb.append(',');
        sb.append("latitude");
        sb.append('=');
        sb.append(((this.latitude == null)?"<null>":this.latitude));
        sb.append(',');
        sb.append("district");
        sb.append('=');
        sb.append(((this.district == null)?"<null>":this.district));
        sb.append(',');
        sb.append("pinCode");
        sb.append('=');
        sb.append(((this.pinCode == null)?"<null>":this.pinCode));
        sb.append(',');
        sb.append("locationId");
        sb.append('=');
        sb.append(((this.locationId == null)?"<null>":this.locationId));
        sb.append(',');
        sb.append("isDeleted");
        sb.append('=');
        sb.append(((this.isDeleted == null)?"<null>":this.isDeleted));
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

}
