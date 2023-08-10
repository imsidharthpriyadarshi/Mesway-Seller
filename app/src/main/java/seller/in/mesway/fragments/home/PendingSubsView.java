package seller.in.mesway.fragments.home;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.button.MaterialButton;

public interface PendingSubsView {
    void getPendingSubsView(ImageView img_top, ConstraintLayout cons_more_detail, TextView t_more_detail, MaterialButton btn_address_approved,MaterialButton btn_rejected,MaterialButton btn_activated,ConstraintLayout cons_normal_info,String subs_id,String user_d,MaterialButton btn_call_user,String address_number,String user_number);

}
