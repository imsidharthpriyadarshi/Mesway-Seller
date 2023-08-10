package seller.in.mesway.client;


import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import seller.in.mesway.response.ExtraInfoResponse;
import seller.in.mesway.response.FirstTimeSellerResponse;
import seller.in.mesway.response.GoToStepResponse;
import seller.in.mesway.response.IsVerifiedResponse;
import seller.in.mesway.response.LoginSignupResponse;
import seller.in.mesway.response.MeswayServingDayResponse;
import seller.in.mesway.response.OtpResponse;
import seller.in.mesway.response.SecondStepResponse;
import seller.in.mesway.response.ThirdStepResponse;
import seller.in.mesway.response.ZipcodeResponse;
import seller.in.mesway.response.todayMeal.TodayMeals;

public interface ApiInterface {

    @POST("seller/login")
    Call<LoginSignupResponse> do_sign_in(
            @Header("Authorization") String api_key,
            @Query("mobile_number") String mobile_number


    );

    @POST("utility/verify-login")
    Call<OtpResponse> verify_number(
            @Header("Authorization") String api_key,
            @Query("number") String number,
            @Query("otp") String otp

    );

    @POST("seller/first_time_seller")
    Call<FirstTimeSellerResponse> first_time_user(
            @Header("Authorization") String access_token,
            @Query("mess_id") UUID mess_id,
            @Query("owner_full_name") String owner_full_name,
            @Query("mess_name") String mess_name,
            @Query("email") String email,
            @Query("notification_token") String notification_token,
            @Query("discription") String description,

            @Query("reference_id") String reference_id

    );

    @GET("get/get_mesway_serving_meal")
    Call<List<MeswayServingDayResponse>> get_all_mesway_serving_meal(
            @Header("Authorization") String api_key

    );

    @POST("seller/second_step_reg")
    Call<SecondStepResponse> second_step(
            @Header("Authorization") String access_token,
            @Query("mess_id") UUID mess_id,
            @Query("serving_meal") UUID serving_meal,
            @Query("monday") UUID monday,
            @Query("tuesday") UUID tuesday,
            @Query("wednesday") UUID wednesday,
            @Query("thrusday") UUID thrusday,
            @Query("friday") UUID friday,
            @Query("saturday") UUID saturday,
            @Query("sunday") UUID sunday

    );

    @POST("seller/third_step_reg")
    Call<ThirdStepResponse> third_step(
            @Header("Authorization") String access_token,
            @Query("mess_id") UUID mess_id,
            @Query("building_no") String building_no,
            @Query("pin_code") String pin_code,
            @Query("landmark") String landmark,
            @Query("latitude") String latitude,
            @Query("langitude") String longitude,
            @Query("company_address") String company_address,
            @Query("district") String district,
            @Query("state") String state

    );

    @POST("seller/fourth_step_reg")
    Call<ThirdStepResponse> fourth_step(
            @Header("Authorization") String access_token,
            @Query("mess_id") UUID mess_id,
            @Query("lunch_time") String lunch_time,
            @Query("dinner_time") String dinner_time,
            @Query("breakfast_time") String breakfast_time


    );


    @POST("seller/fifth_step_reg")
    Call<ThirdStepResponse> fifth_step(
            @Header("Authorization") String access_token,
            @Query("mess_id") UUID mess_id,
            @Query("one_meal_price_per_day") String one_meal_price_per_day,
            @Query("two_meal_price_per_day") String two_meal_price_per_day,
            @Query("three_meal_price_per_day") String three_meal_price_per_day,
            @Query("subs_id") String subs_id,
            @Query("is_security_deposite") boolean is_security_deposit,
            @Query("security_money") int security_money,
            @Query("finalize") boolean finalize



            );

    @POST("seller/fifth_step_reg")
    Call<Boolean> fifth_step_finalize(
            @Header("Authorization") String access_token,
            @Query("mess_id") UUID mess_id,
            @Query("finalize") boolean finalize


    );

    @POST("seller/sixth_step_reg")
    Call<ThirdStepResponse> sixth_step(
            @Header("Authorization") String access_token,
            @Query("mess_id") UUID mess_id,
            @Query("delivery_boy_name") String delivery_boy_name,
            @Query("delivery_boy_number") String delivery_boy_number,
            @Query("delivery_charge_per_day") int delivery_charge_per_day
            );


    @POST("seller/finalize_reg")
    Call<Boolean> finalize_step(
            @Header("Authorization") String access_token,
            @Query("mess_id") UUID mess_id


            );


    @GET("seller/is_verified")
    Call<IsVerifiedResponse> is_verified(
            @Header("Authorization") String access_token,
            @Query("mess_id") UUID mess_id


    );

    @GET("seller/click_go_to_step")
    Call<GoToStepResponse> go_to_step(
            @Header("Authorization") String access_token,
            @Query("mess_id") UUID mess_id


    );


    @GET("utility/get_extra_info")
    Call<ExtraInfoResponse> get_extra_info(
            @Header("Authorization") String api_key

    );

    @GET("get/get_today_serves_meal")
    Call<TodayMeals> get_today_serves_meal(
            @Header("Authorization") String access_token,
            @Query("mess_id") UUID mess_id


    );


    @GET("get/get_cancel_meal")
    Call<TodayMeals> get_cancel_meal(
            @Header("Authorization") String access_token,
            @Query("mess_id") UUID mess_id


    );

    @GET("get/get_all_pending_subs")
    Call<TodayMeals> get_pending_subs(
            @Header("Authorization") String access_token,
            @Query("mess_id") UUID mess_id


    );

    @GET("get/get_all_active_subs")
    Call<TodayMeals> get_active_subs(
            @Header("Authorization") String access_token,
            @Query("mess_id") UUID mess_id

    );

    @GET("get/get_total_earning")
    Call<TodayMeals> get_total_earning(
            @Header("Authorization") String access_token,
            @Query("mess_id") UUID mess_id


    );




    @GET("utility/get_date_as_date")
    Call<String> get_date_as_date(
    );




    @POST("seller/send_delivery_code")
    Call<Boolean> send_delivery_code(
            @Header("Authorization") String access_token,
            @Query("mess_id") UUID mess_id,
            @Query("subs_id") UUID subs_id,
            @Query("user_id") UUID user_id,
            @Query("meal_type") String meal_type


            );


    @POST("seller/get_payment")
    Call<ThirdStepResponse> get_payment(
            @Header("Authorization") String access_token,
            @Query("mess_id") UUID mess_id,
            @Query("subs_id") UUID subs_id,
            @Query("payment_done") boolean payment_done

    );

    @POST("seller/give_delivery")
    Call<Boolean> give_delivery(
            @Header("Authorization") String access_token,
            @Query("mess_id") UUID mess_id,
            @Query("subs_id") UUID subs_id,
            @Query("code") String code,
            @Query("meal_type") String meal_type

    );



    @POST("seller/subs_status_change")
    Call<ThirdStepResponse> subs_status_change(
            @Header("Authorization") String access_token,
            @Query("mess_id") UUID mess_id,
            @Query("subs_id") UUID subs_id,
            @Query("reason") String reason,
            @Query("status") String status

    );



    @GET("{zipcode}")
    Call<ZipcodeResponse[]> get_zip_code_data(
            @Path("zipcode") String zipcode
    );

    @POST("utility/update_notification_token")
    Call<Boolean>  update_notification_token(
      @Header("Authorization") String api_key,
      @Query("mess_id") UUID mess_id,
      @Query("notification_token") String notification_token


    );

}
