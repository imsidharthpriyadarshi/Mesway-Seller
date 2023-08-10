package seller.in.mesway.fragments.home;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.button.MaterialButton;

public interface ActiveSubsView {
    void getActiveSubsView(ImageView img_top, ConstraintLayout cons_more_detail, TextView t_more_detail, ConstraintLayout cons_normal_info, MaterialButton btn_call, String address_number, String user_number);
}
