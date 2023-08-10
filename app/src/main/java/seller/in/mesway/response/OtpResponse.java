package seller.in.mesway.response;

import java.util.UUID;

public class OtpResponse {
    private String access_token;
    private String token_type;
    private int reg_steps;
    private UUID mess_id;
    private String mobile_number;
    private String email;
    private String mess_serving;

    public String getMess_serving() {
        return mess_serving;
    }

    public String getAccess_token() {
        return access_token;
    }

    public String getToken_type() {
        return token_type;
    }

    public int getReg_steps() {
        return reg_steps;
    }

    public UUID getMess_id() {
        return mess_id;
    }

    public String getMobile_number() {
        return mobile_number;
    }

    public String getEmail() {
        return email;
    }
}
