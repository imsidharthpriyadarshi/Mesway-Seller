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
import seller.in.mesway.fragments.home.ActiveSubsView;
import seller.in.mesway.models.ActiveSubscriptionModel;

public class ActiveSubscriptionAdapter extends RecyclerView.Adapter<ActiveSubscriptionAdapter.ViewHolder> {
    private List<ActiveSubscriptionModel> activeSubscriptionModelsList;
    private Context mContext;
    private ActiveSubsView iActiveSubsView;

    public ActiveSubscriptionAdapter(List<ActiveSubscriptionModel> activeSubscriptionModelsList, Context mContext, ActiveSubsView iActiveSubsView) {
        this.activeSubscriptionModelsList = activeSubscriptionModelsList;
        this.mContext = mContext;
        this.iActiveSubsView = iActiveSubsView;
    }

    @NonNull
    @Override
    public ActiveSubscriptionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.model_active_subs,parent,false);
        return new ViewHolder(view, mContext);
    }

    @Override
    public void onBindViewHolder(@NonNull ActiveSubscriptionAdapter.ViewHolder holder, int position) {
        ActiveSubscriptionModel activeSubscriptionModel= activeSubscriptionModelsList.get(position);

        holder.setAllValue(activeSubscriptionModel.getStarting_meal_value(),activeSubscriptionModel.getSubs_status(),activeSubscriptionModel.getUser_name(),activeSubscriptionModel.getStating_date(),activeSubscriptionModel.getFull_plan_price(),activeSubscriptionModel.getRefund_amount_value(),activeSubscriptionModel.getUser_address(),activeSubscriptionModel.getTotal_delivered_meal(),activeSubscriptionModel.getLeft_meal(),activeSubscriptionModel.getPayment_mode(),activeSubscriptionModel.getPayment_status(),activeSubscriptionModel.getPlan_type(),activeSubscriptionModel.getPlan_price(),activeSubscriptionModel.getCancel_credit());

        iActiveSubsView.getActiveSubsView(holder.img_down,holder.cons_more_detail,holder.t_more_detail,holder.cons_normal_info,holder.btn_call,activeSubscriptionModel.getUser_address_number(),activeSubscriptionModel.getUser_number());
    }

    @Override
    public int getItemCount() {
        return activeSubscriptionModelsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private final TextView top_subs_type,subs_status,user_name,starting_date,user_address,t_more_detail,starting_meal;

        private final ConstraintLayout cons_more_detail,cons_normal_info;

        private final TextView total_delivered_meal,left_meal,payment_mode,payment_status,plan_type,total_down_plan_price,plan_price,t_refund_value,cancel_credit;

        private ImageView img_down;

        private MaterialButton btn_call;
        private  Context vContext;

        public ViewHolder(@NonNull View itemView, Context context) {
            super(itemView);


            top_subs_type=itemView.findViewById(R.id.subs_type);
            subs_status=itemView.findViewById(R.id.t_sub_pending_status);
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

            btn_call=itemView.findViewById(R.id.btn_call_user);

            cons_normal_info=itemView.findViewById(R.id.cons_normal_info);
            cons_more_detail=itemView.findViewById(R.id.cons_more_detail);

            img_down=itemView.findViewById(R.id.img_down_img);

        }

        private void setAllValue(String starting_meal_value, String status,String v_user_name, String v_stating_date, String v_full_plan_price, String v_refund_amount_value, String v_user_address, String v_total_delivered_meal, String v_left_meal, String v_payment_mode, String v_payment_status, String v_plan_type, String v_plan_price, String v_cancel_credit)
        {
//for future use


            if (status.equalsIgnoreCase("expired")){
                subs_status.setText("Expired");
                img_down.setVisibility(View.GONE);
                subs_status.setTextColor(ContextCompat.getColor(vContext,R.color.red));

            }

            starting_meal.setText(starting_meal_value);

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
