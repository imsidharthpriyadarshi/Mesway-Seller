package seller.in.mesway.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NormalSubscription {

    @SerializedName("subscription_id")
    @Expose
    private String subscriptionId;
    @SerializedName("mess_id")
    @Expose
    private String messId;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("start_from")
    @Expose
    private String startFrom;
    @SerializedName("plan_type")
    @Expose
    private String planType;
    @SerializedName("starting_meal")
    @Expose
    private String startingMeal;
    @SerializedName("second_meal")
    @Expose
    private String secondMeal;
    @SerializedName("third_meal")
    @Expose
    private String thirdMeal;
    @SerializedName("plan_price")
    @Expose
    private String planPrice;
    @SerializedName("subs_type")
    @Expose
    private String subsType;
    @SerializedName("payment_value")
    @Expose
    private String paymentValue;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("subs_rejected_reason")
    @Expose
    private String subsRejectedReason;
    @SerializedName("valid_upto")
    @Expose
    private String validUpto;
    @SerializedName("total_meals")
    @Expose
    private Integer totalMeals;
    @SerializedName("left_meals")
    @Expose
    private Integer leftMeals;
    @SerializedName("order_id")
    @Expose
    private String orderId;
    @SerializedName("payment_mode")
    @Expose
    private String paymentMode;
    @SerializedName("payment_by")
    @Expose
    private String paymentBy;
    @SerializedName("is_first_order")
    @Expose
    private Boolean isFirstOrder;
    @SerializedName("payment_status")
    @Expose
    private String paymentStatus;
    @SerializedName("allowed_cancel_meal")
    @Expose
    private Integer allowedCancelMeal;
    @SerializedName("how_many_meal_cancel")
    @Expose
    private Integer howManyMealCancel;
    @SerializedName("delivered_meal")
    @Expose
    private Integer deliveredMeal;
    @SerializedName("user_location")
    @Expose
    private String userLocation;
    @SerializedName("breakfast")
    @Expose
    private String breakfast;
    @SerializedName("lunch")
    @Expose
    private String lunch;
    @SerializedName("dinner")
    @Expose
    private String dinner;

    private boolean is_refunded;
    private Integer security_money;

    public boolean isIs_refunded() {
        return is_refunded;
    }

    public void setIs_refunded(boolean is_refunded) {
        this.is_refunded = is_refunded;
    }

    public Integer getSecurity_money() {
        return security_money;
    }

    public void setSecurity_money(Integer security_money) {
        this.security_money = security_money;
    }

    public Boolean getFirstOrder() {
        return isFirstOrder;
    }

    public void setFirstOrder(Boolean firstOrder) {
        isFirstOrder = firstOrder;
    }


    public String getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(String subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public String getMessId() {
        return messId;
    }

    public void setMessId(String messId) {
        this.messId = messId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStartFrom() {
        return startFrom;
    }

    public void setStartFrom(String startFrom) {
        this.startFrom = startFrom;
    }

    public String getPlanType() {
        return planType;
    }

    public void setPlanType(String planType) {
        this.planType = planType;
    }

    public String getStartingMeal() {
        return startingMeal;
    }

    public void setStartingMeal(String startingMeal) {
        this.startingMeal = startingMeal;
    }

    public String getSecondMeal() {
        return secondMeal;
    }

    public void setSecondMeal(String secondMeal) {
        this.secondMeal = secondMeal;
    }

    public String getThirdMeal() {
        return thirdMeal;
    }

    public void setThirdMeal(String thirdMeal) {
        this.thirdMeal = thirdMeal;
    }

    public String getPlanPrice() {
        return planPrice;
    }

    public void setPlanPrice(String planPrice) {
        this.planPrice = planPrice;
    }

    public String getSubsType() {
        return subsType;
    }

    public void setSubsType(String subsType) {
        this.subsType = subsType;
    }

    public String getPaymentValue() {
        return paymentValue;
    }

    public void setPaymentValue(String paymentValue) {
        this.paymentValue = paymentValue;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSubsRejectedReason() {
        return subsRejectedReason;
    }

    public void setSubsRejectedReason(String subsRejectedReason) {
        this.subsRejectedReason = subsRejectedReason;
    }

    public String getValidUpto() {
        return validUpto;
    }

    public void setValidUpto(String validUpto) {
        this.validUpto = validUpto;
    }

    public Integer getTotalMeals() {
        return totalMeals;
    }

    public void setTotalMeals(Integer totalMeals) {
        this.totalMeals = totalMeals;
    }

    public Integer getLeftMeals() {
        return leftMeals;
    }

    public void setLeftMeals(Integer leftMeals) {
        this.leftMeals = leftMeals;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getPaymentBy() {
        return paymentBy;
    }

    public void setPaymentBy(String paymentBy) {
        this.paymentBy = paymentBy;
    }

    public Boolean getIsFirstOrder() {
        return isFirstOrder;
    }

    public void setIsFirstOrder(Boolean isFirstOrder) {
        this.isFirstOrder = isFirstOrder;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public Integer getAllowedCancelMeal() {
        return allowedCancelMeal;
    }

    public void setAllowedCancelMeal(Integer allowedCancelMeal) {
        this.allowedCancelMeal = allowedCancelMeal;
    }

    public Integer getHowManyMealCancel() {
        return howManyMealCancel;
    }

    public void setHowManyMealCancel(Integer howManyMealCancel) {
        this.howManyMealCancel = howManyMealCancel;
    }

    public Integer getDeliveredMeal() {
        return deliveredMeal;
    }

    public void setDeliveredMeal(Integer deliveredMeal) {
        this.deliveredMeal = deliveredMeal;
    }

    public String getUserLocation() {
        return userLocation;
    }

    public void setUserLocation(String userLocation) {
        this.userLocation = userLocation;
    }

    public String getBreakfast() {
        return breakfast;
    }

    public void setBreakfast(String breakfast) {
        this.breakfast = breakfast;
    }

    public String getLunch() {
        return lunch;
    }

    public void setLunch(String lunch) {
        this.lunch = lunch;
    }

    public String getDinner() {
        return dinner;
    }

    public void setDinner(String dinner) {
        this.dinner = dinner;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(NormalSubscription.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("subscriptionId");
        sb.append('=');
        sb.append(((this.subscriptionId == null)?"<null>":this.subscriptionId));
        sb.append(',');
        sb.append("messId");
        sb.append('=');
        sb.append(((this.messId == null)?"<null>":this.messId));
        sb.append(',');
        sb.append("userId");
        sb.append('=');
        sb.append(((this.userId == null)?"<null>":this.userId));
        sb.append(',');
        sb.append("startFrom");
        sb.append('=');
        sb.append(((this.startFrom == null)?"<null>":this.startFrom));
        sb.append(',');
        sb.append("planType");
        sb.append('=');
        sb.append(((this.planType == null)?"<null>":this.planType));
        sb.append(',');
        sb.append("startingMeal");
        sb.append('=');
        sb.append(((this.startingMeal == null)?"<null>":this.startingMeal));
        sb.append(',');
        sb.append("secondMeal");
        sb.append('=');
        sb.append(((this.secondMeal == null)?"<null>":this.secondMeal));
        sb.append(',');
        sb.append("thirdMeal");
        sb.append('=');
        sb.append(((this.thirdMeal == null)?"<null>":this.thirdMeal));
        sb.append(',');
        sb.append("planPrice");
        sb.append('=');
        sb.append(((this.planPrice == null)?"<null>":this.planPrice));
        sb.append(',');
        sb.append("subsType");
        sb.append('=');
        sb.append(((this.subsType == null)?"<null>":this.subsType));
        sb.append(',');
        sb.append("paymentValue");
        sb.append('=');
        sb.append(((this.paymentValue == null)?"<null>":this.paymentValue));
        sb.append(',');
        sb.append("status");
        sb.append('=');
        sb.append(((this.status == null)?"<null>":this.status));
        sb.append(',');
        sb.append("subsRejectedReason");
        sb.append('=');
        sb.append(((this.subsRejectedReason == null)?"<null>":this.subsRejectedReason));
        sb.append(',');
        sb.append("validUpto");
        sb.append('=');
        sb.append(((this.validUpto == null)?"<null>":this.validUpto));
        sb.append(',');
        sb.append("totalMeals");
        sb.append('=');
        sb.append(((this.totalMeals == null)?"<null>":this.totalMeals));
        sb.append(',');
        sb.append("leftMeals");
        sb.append('=');
        sb.append(((this.leftMeals == null)?"<null>":this.leftMeals));
        sb.append(',');
        sb.append("orderId");
        sb.append('=');
        sb.append(((this.orderId == null)?"<null>":this.orderId));
        sb.append(',');
        sb.append("paymentMode");
        sb.append('=');
        sb.append(((this.paymentMode == null)?"<null>":this.paymentMode));
        sb.append(',');
        sb.append("paymentBy");
        sb.append('=');
        sb.append(((this.paymentBy == null)?"<null>":this.paymentBy));
        sb.append(',');
        sb.append("isFirstOrder");
        sb.append('=');
        sb.append(((this.isFirstOrder == null)?"<null>":this.isFirstOrder));
        sb.append(',');
        sb.append("paymentStatus");
        sb.append('=');
        sb.append(((this.paymentStatus == null)?"<null>":this.paymentStatus));
        sb.append(',');
        sb.append("allowedCancelMeal");
        sb.append('=');
        sb.append(((this.allowedCancelMeal == null)?"<null>":this.allowedCancelMeal));
        sb.append(',');
        sb.append("howManyMealCancel");
        sb.append('=');
        sb.append(((this.howManyMealCancel == null)?"<null>":this.howManyMealCancel));
        sb.append(',');
        sb.append("deliveredMeal");
        sb.append('=');
        sb.append(((this.deliveredMeal == null)?"<null>":this.deliveredMeal));
        sb.append(',');
        sb.append("userLocation");
        sb.append('=');
        sb.append(((this.userLocation == null)?"<null>":this.userLocation));
        sb.append(',');
        sb.append("breakfast");
        sb.append('=');
        sb.append(((this.breakfast == null)?"<null>":this.breakfast));
        sb.append(',');
        sb.append("lunch");
        sb.append('=');
        sb.append(((this.lunch == null)?"<null>":this.lunch));
        sb.append(',');
        sb.append("dinner");
        sb.append('=');
        sb.append(((this.dinner == null)?"<null>":this.dinner));
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

}