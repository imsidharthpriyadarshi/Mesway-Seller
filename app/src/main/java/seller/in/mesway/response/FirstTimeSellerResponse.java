package seller.in.mesway.response;

import java.util.UUID;

public class FirstTimeSellerResponse {
    private int mess_reg_steps;
    private UUID mess_id;
    private String mess_phone_number;
    private String mess_email;
    private String mess_name;
    private String owner_full_name;

    private String notification_token;
    private String discription;


    public int getMess_reg_steps() {
        return mess_reg_steps;
    }

    public UUID getMess_id() {
        return mess_id;
    }

    public String getMess_phone_number() {
        return mess_phone_number;
    }

    public String getMess_email() {
        return mess_email;
    }

    public String getMess_name() {
        return mess_name;
    }

    public String getOwner_full_name() {
        return owner_full_name;
    }

    public String getDiscription() {
        return discription;
    }

    public String getNotification_token() {
        return notification_token;
    }
}
