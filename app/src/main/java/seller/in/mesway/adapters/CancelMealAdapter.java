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
import java.util.List;
import seller.in.mesway.R;
import seller.in.mesway.fragments.home.CancelMealView;
import seller.in.mesway.models.CancelMealsModel;

public class CancelMealAdapter extends RecyclerView.Adapter<CancelMealAdapter.ViewHolder> {
    private List<CancelMealsModel> cancelMealsModelsList;
    private Context mContext;
    private CancelMealView ICancelMealView;

    public CancelMealAdapter(List<CancelMealsModel> cancelMealsModelsList, Context mContext,CancelMealView cancelMealView) {
        this.cancelMealsModelsList = cancelMealsModelsList;
        this.ICancelMealView=cancelMealView;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public CancelMealAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.model_cancel_meals,parent,false);
        return new ViewHolder(view, mContext);

    }

    @Override
    public void onBindViewHolder(@NonNull CancelMealAdapter.ViewHolder holder, int position) {
        CancelMealsModel cancelMealsModel= cancelMealsModelsList.get(position);

        holder.setAllValue(cancelMealsModel.getTop_meal_type(),cancelMealsModel.getUser_name(),cancelMealsModel.getMeal_mess_time(),cancelMealsModel.getFull_plan_price(),cancelMealsModel.getRefund_amount_value(),cancelMealsModel.getUser_address(),cancelMealsModel.getTotal_delivered_meal(),cancelMealsModel.getLeft_meal(),cancelMealsModel.getPayment_mode(),cancelMealsModel.getPayment_status(),cancelMealsModel.getPlan_type(),cancelMealsModel.getPlan_price(),cancelMealsModel.getCancel_credit());
        ICancelMealView.getCancelMealView(holder.img_down,holder.cons_more_detail,holder.t_more_detail,holder.cons_normal_info);

    }

    @Override
    public int getItemCount() {
        return cancelMealsModelsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private final TextView top_meal_type,meal_status,user_name,meal_mess_time,user_address,t_more_detail;
        private final ConstraintLayout cons_more_detail,cons_normal_info;

        private final TextView total_delivered_meal,left_meal,payment_mode,payment_status,plan_type,total_down_plan_price,plan_price,t_refund_value,cancel_credit;
        private ImageView img_down;

        private  Context vContext;

        public ViewHolder(@NonNull View itemView,Context context) {
            super(itemView);
            top_meal_type=itemView.findViewById(R.id.meal_type);
            meal_status=itemView.findViewById(R.id.t_cancel_meal_status);
            user_name=itemView.findViewById(R.id.t_user_name);
            meal_mess_time=itemView.findViewById(R.id.t_delivery_time_range);
            user_address=itemView.findViewById(R.id.t_did_you_arrived_address);
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

            vContext=context;

            cons_more_detail=itemView.findViewById(R.id.cons_more_detail);
            cons_normal_info=itemView.findViewById(R.id.cons_normal_info);

            img_down=itemView.findViewById(R.id.img_down_img);

        }
        private void setAllValue(String meal_type,  String v_user_name, String v_meal_mess_time, String v_full_plan_price, String v_refund_amount_value, String v_user_address, String v_total_delivered_meal, String v_left_meal, String v_payment_mode, String v_payment_status, String v_plan_type, String v_plan_price, String v_cancel_credit)
        {



            if (v_payment_status.equalsIgnoreCase("not done")){

                payment_status.setText("Not Paid");
            }else {

                payment_status.setText("Paid");
                payment_status.setTextColor(ContextCompat.getColor(vContext,R.color.green));


            }
            top_meal_type.setText(meal_type);
            user_name.setText(v_user_name);
            meal_mess_time.setText(v_meal_mess_time);
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
