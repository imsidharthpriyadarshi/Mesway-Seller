package seller.in.mesway.models;

public class ActiveSubscriptionModel {
    private String top_meal_type,subs_status,user_name,stating_date,user_address_number,user_number,user_id,full_plan_price,refund_amount_value,user_address,subs_id,starting_meal_value;
    private String total_delivered_meal,left_meal,payment_mode,payment_status,plan_type,total_price,plan_price,cancel_credit;

    public String getStarting_meal_value() {
        return starting_meal_value;
    }

    public void setStarting_meal_value(String starting_meal_value) {
        this.starting_meal_value = starting_meal_value;
    }

    public ActiveSubscriptionModel(String starting_meal_value, String subs_status, String user_name, String stating_date, String user_address_number, String user_number, String user_id, String full_plan_price, String refund_amount_value, String user_address, String subs_id, String total_delivered_meal, String left_meal, String payment_mode, String payment_status, String plan_type, String total_price, String plan_price, String cancel_credit) {
this.starting_meal_value=starting_meal_value;
        this.subs_status=subs_status;
        this.user_name = user_name;
        this.stating_date = stating_date;
        this.user_address_number = user_address_number;
        this.user_number = user_number;
        this.user_id = user_id;
        this.full_plan_price = full_plan_price;
        this.refund_amount_value = refund_amount_value;
        this.user_address = user_address;
        this.subs_id = subs_id;
        this.total_delivered_meal = total_delivered_meal;
        this.left_meal = left_meal;
        this.payment_mode = payment_mode;
        this.payment_status = payment_status;
        this.plan_type = plan_type;
        this.total_price = total_price;
        this.plan_price = plan_price;
        this.cancel_credit = cancel_credit;
    }

    public String getTop_meal_type() {
        return top_meal_type;
    }

    public void setTop_meal_type(String top_meal_type) {
        this.top_meal_type = top_meal_type;
    }

    public String getSubs_status() {
        return subs_status;
    }

    public void setSubs_status(String subs_status) {
        this.subs_status = subs_status;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getStating_date() {
        return stating_date;
    }

    public void setStating_date(String stating_date) {
        this.stating_date = stating_date;
    }

    public String getUser_address_number() {
        return user_address_number;
    }

    public void setUser_address_number(String user_address_number) {
        this.user_address_number = user_address_number;
    }

    public String getUser_number() {
        return user_number;
    }

    public void setUser_number(String user_number) {
        this.user_number = user_number;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getFull_plan_price() {
        return full_plan_price;
    }

    public void setFull_plan_price(String full_plan_price) {
        this.full_plan_price = full_plan_price;
    }

    public String getRefund_amount_value() {
        return refund_amount_value;
    }

    public void setRefund_amount_value(String refund_amount_value) {
        this.refund_amount_value = refund_amount_value;
    }

    public String getUser_address() {
        return user_address;
    }

    public void setUser_address(String user_address) {
        this.user_address = user_address;
    }

    public String getSubs_id() {
        return subs_id;
    }

    public void setSubs_id(String subs_id) {
        this.subs_id = subs_id;
    }

    public String getTotal_delivered_meal() {
        return total_delivered_meal;
    }

    public void setTotal_delivered_meal(String total_delivered_meal) {
        this.total_delivered_meal = total_delivered_meal;
    }

    public String getLeft_meal() {
        return left_meal;
    }

    public void setLeft_meal(String left_meal) {
        this.left_meal = left_meal;
    }

    public String getPayment_mode() {
        return payment_mode;
    }

    public void setPayment_mode(String payment_mode) {
        this.payment_mode = payment_mode;
    }

    public String getPayment_status() {
        return payment_status;
    }

    public void setPayment_status(String payment_status) {
        this.payment_status = payment_status;
    }

    public String getPlan_type() {
        return plan_type;
    }

    public void setPlan_type(String plan_type) {
        this.plan_type = plan_type;
    }

    public String getTotal_price() {
        return total_price;
    }

    public void setTotal_price(String total_price) {
        this.total_price = total_price;
    }

    public String getPlan_price() {
        return plan_price;
    }

    public void setPlan_price(String plan_price) {
        this.plan_price = plan_price;
    }

    public String getCancel_credit() {
        return cancel_credit;
    }

    public void setCancel_credit(String cancel_credit) {
        this.cancel_credit = cancel_credit;
    }
}
