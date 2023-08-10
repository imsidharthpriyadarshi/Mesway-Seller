package seller.in.mesway.fragments.home;

import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public interface AllViewTodayMeals {
    void allViewOfTodayMeal(TextInputLayout etl_code, TextInputEditText et_code, MaterialButton btn_call_user, MaterialButton btn_verify, MaterialButton btn_arrived, ConstraintLayout cons_give_refund, ConstraintLayout cons_verify, ConstraintLayout cons_get_payment, ConstraintLayout cons_normal_info, ConstraintLayout cons_more_detail, String address_number, String user_main_number, String user_id, String subs_id, MaterialButton btn_send_code, ProgressBar progressBar_send_code, String meal_type, ProgressBar progressBar_verify_code, ImageView img_down, TextView t_more_detail,String total_amount);

    void countMealType(int meal_count,String meal_type);

}
