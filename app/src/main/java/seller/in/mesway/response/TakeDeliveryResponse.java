package seller.in.mesway.response;

public class TakeDeliveryResponse {
    private String subs_id,user_id,mess_id,code,meal_type,created_timestamp;

    public TakeDeliveryResponse(String subs_id, String user_id, String mess_id, String code, String meal_type, String created_timestamp) {
        this.subs_id = subs_id;
        this.user_id = user_id;
        this.mess_id = mess_id;
        this.code = code;
        this.meal_type = meal_type;
        this.created_timestamp = created_timestamp;
    }

    public String getSubs_id() {
        return subs_id;
    }

    public void setSubs_id(String subs_id) {
        this.subs_id = subs_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getMess_id() {
        return mess_id;
    }

    public void setMess_id(String mess_id) {
        this.mess_id = mess_id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMeal_type() {
        return meal_type;
    }

    public void setMeal_type(String meal_type) {
        this.meal_type = meal_type;
    }

    public String getCreated_timestamp() {
        return created_timestamp;
    }

    public void setCreated_timestamp(String created_timestamp) {
        this.created_timestamp = created_timestamp;
    }
}
