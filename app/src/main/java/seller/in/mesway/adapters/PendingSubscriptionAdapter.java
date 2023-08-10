package seller.in.mesway.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.util.List;

import seller.in.mesway.R;
import seller.in.mesway.fragments.home.PendingSubsView;
import seller.in.mesway.models.ActiveSubscriptionModel;
import seller.in.mesway.models.PendingSubscriptionModel;

public class PendingSubscriptionAdapter extends RecyclerView.Adapter<PendingSubscriptionAdapter.ViewHolder> {
    private List<PendingSubscriptionModel>  pendingSubscriptionModelList;
    private Context mContext;
    private PendingSubsView iPendingSubView;


    public PendingSubscriptionAdapter(List<PendingSubscriptionModel> pendingSubscriptionModelList, Context mContext, PendingSubsView iPendingSubView) {
        this.pendingSubscriptionModelList = pendingSubscriptionModelList;
        this.mContext = mContext;
        this.iPendingSubView = iPendingSubView;
    }

    @NonNull
    @Override
    public PendingSubscriptionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.model_pending_subs,parent,false);
        return new ViewHolder(view, mContext);
    }

    @Override
    public void onBindViewHolder(@NonNull PendingSubscriptionAdapter.ViewHolder holder, int position) {
        PendingSubscriptionModel pendingSubscriptionModel= pendingSubscriptionModelList.get(position);

        holder.setAllValue(pendingSubscriptionModel.getStarting_meal_value(),pendingSubscriptionModel.getUser_name(),pendingSubscriptionModel.getStating_date(),pendingSubscriptionModel.getFull_plan_price(),pendingSubscriptionModel.getRefund_amount_value(),pendingSubscriptionModel.getUser_address(),pendingSubscriptionModel.getTotal_delivered_meal(),pendingSubscriptionModel.getLeft_meal(),pendingSubscriptionModel.getPayment_mode(),pendingSubscriptionModel.getPayment_status(),pendingSubscriptionModel.getPlan_type(),pendingSubscriptionModel.getPlan_price(),pendingSubscriptionModel.getCancel_credit());
        iPendingSubView.getPendingSubsView(holder.img_down,holder.cons_more_detail,holder.t_more_detail,holder.btn_approved_address,holder.btn_reject_subs,holder.btn_activate_subs,holder.cons_normal_info,pendingSubscriptionModel.getSubs_id(),pendingSubscriptionModel.getUser_id(),holder.btn_call_user,pendingSubscriptionModel.getUser_address_number(),pendingSubscriptionModel.getUser_number());
    }

    @Override
    public int getItemCount() {
        return pendingSubscriptionModelList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView top_subs_type,subs_status,user_name,starting_date,user_address,t_more_detail,starting_meal;

        private final ConstraintLayout cons_more_detail, cons_normal_info;

        private final TextView total_delivered_meal,left_meal,payment_mode,payment_status,plan_type,total_down_plan_price,plan_price,t_refund_value,cancel_credit;

        private final ImageView img_down;

        private final Context vContext;


        private final MaterialButton btn_approved_address,btn_reject_subs,btn_activate_subs,btn_call_user;

        public ViewHolder(@NonNull View itemView,Context context) {
            super(itemView);

            top_subs_type=itemView.findViewById(R.id.subs_type);
            subs_status=itemView.findViewById(R.id.t_subs_active_status);
            user_name=itemView.findViewById(R.id.t_user_name);
            starting_date=itemView.findViewById(R.id.t_delivery_time_range);
            user_address=itemView.findViewById(R.id.t_address_value);
            total_delivered_meal=itemView.findViewById(R.id.t_total_meal_delivery_value);
            left_meal=itemView.findViewById(R.id.t_left_meals_value);
            payment_mode=itemView.findViewById(R.id.t_payment_mode_value);
            payment_status=itemView.findViewById(R.id.t_payment_status_value);
            plan_type=itemView.findViewById(R.id.t_plan_type_value);
            total_down_plan_price=itemView.findViewById(R.id.t_total_price);
            plan_price=itemView.findViewById(R.id.t_down_plan_price_value);
            t_refund_value=itemView.findViewById(R.id.t_down_refund_value);
            cancel_credit=itemView.findViewById(R.id.t_cancel_credit_left);
            t_more_detail=itemView.findViewById(R.id.t_more_detail);
            starting_meal=itemView.findViewById(R.id.t_starting_meal);

            vContext=context;

            cons_more_detail=itemView.findViewById(R.id.cons_more_detail);
            cons_normal_info=itemView.findViewById(R.id.cons_normal_info);

            img_down=itemView.findViewById(R.id.img_down_img);

            btn_activate_subs=itemView.findViewById(R.id.btn_activate_subscription);
            btn_approved_address=itemView.findViewById(R.id.btn_address_appreoved);
            btn_reject_subs=itemView.findViewById(R.id.btn_reject_subs);
            btn_call_user=itemView.findViewById(R.id.btn_call_user);

        }

        private void setAllValue( String t_starting_meal,String v_user_name, String v_stating_date, String v_full_plan_price, String v_refund_amount_value, String v_user_address, String v_total_delivered_meal, String v_left_meal, String v_payment_mode, String v_payment_status, String v_plan_type, String v_plan_price, String v_cancel_credit)
        {

            starting_meal.setText(t_starting_meal);

            if (v_payment_status.equalsIgnoreCase("not done")){

                payment_status.setText("Not Paid");
            }else {

                payment_status.setText("Paid");
                payment_status.setTextColor(ContextCompat.getColor(vContext,R.color.green));


            }
            top_subs_type.setText(v_plan_type);
            user_name.setText(v_user_name);
            starting_date.setText(v_stating_date);
            total_down_plan_price.setText("\u20B9 "+v_full_plan_price);
            t_refund_value.setText("\u20B9 "+v_refund_amount_value);
            user_address.setText(v_user_address);
            total_delivered_meal.setText(v_total_delivered_meal);
            left_meal.setText(v_left_meal);
            payment_mode.setText(v_payment_mode);
            plan_type.setText(v_plan_type);
            plan_price.setText("\u20B9 "+v_plan_price);
            cancel_credit.setText(v_cancel_credit);

        }

    }
}
