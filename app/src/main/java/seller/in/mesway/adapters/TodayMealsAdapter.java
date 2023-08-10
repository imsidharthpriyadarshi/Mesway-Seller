package seller.in.mesway.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import seller.in.mesway.R;
import seller.in.mesway.fragments.home.AllViewTodayMeals;
import seller.in.mesway.models.TodayMealsModel;

public class TodayMealsAdapter extends RecyclerView.Adapter<TodayMealsAdapter.ViewHolder> {
    private List<TodayMealsModel> todayMealsModelList;
    private Context mContext;
    private AllViewTodayMeals IAllViewTodayMeals;
    private final List<TodayMealsModel> tempmealModelList;


    public TodayMealsAdapter(List<TodayMealsModel> todayMealsModelList, Context mContext,AllViewTodayMeals IAllViewTodayMeals) {
        this.todayMealsModelList = todayMealsModelList;
        this.mContext = mContext;
        this.tempmealModelList = new ArrayList<>(todayMealsModelList);

        this.IAllViewTodayMeals=IAllViewTodayMeals;

    }

    @NonNull
    @Override
    public TodayMealsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.model_today_meals,parent,false);
        return new ViewHolder(view, mContext);

    }

    @Override
    public void onBindViewHolder(@NonNull TodayMealsAdapter.ViewHolder holder, int position) {
        TodayMealsModel todayMealsModel= todayMealsModelList.get(position);

        holder.setAllValue(todayMealsModel.getTop_meal_type(),todayMealsModel.getMeal_status(),todayMealsModel.getUser_name(),todayMealsModel.getMeal_mess_time(),todayMealsModel.getFull_plan_price(),todayMealsModel.getRefund_amount_value(),todayMealsModel.getUser_address(),todayMealsModel.getTotal_delivered_meal(),todayMealsModel.getLeft_meal(),todayMealsModel.getPayment_mode(),todayMealsModel.getPayment_status(),todayMealsModel.getPlan_type(),todayMealsModel.getPlan_price(),todayMealsModel.getCancel_credit());

        IAllViewTodayMeals.allViewOfTodayMeal(holder.etl_code,holder.et_code,holder.btn_user_call,holder.btn_verify,holder.btn_arrived,holder.cons_get_refund,holder.cons_verify,holder.cons_get_payment,holder.cons_normal_info,holder.cons_more_detail,todayMealsModel.getUser_address_number(),todayMealsModel.getUser_number(),todayMealsModel.getUser_id(),todayMealsModel.getSubs_id(),holder.btn_send_code,holder.progressBar_send_code,todayMealsModel.getTop_meal_type().toLowerCase(),holder.progressBar_verify_code,holder.img_down,holder.t_more_detail,todayMealsModel.getFull_plan_price());

    }

    @Override
    public int getItemCount() {
        return todayMealsModelList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView top_meal_type,meal_status,user_name,meal_mess_time,full_plan_price,refund_amount_value,user_address,t_more_detail;
        private final ImageView tick_img_top;
        private final ConstraintLayout cons_get_payment,cons_get_refund,cons_verify,cons_normal_info,cons_more_detail;
        private final MaterialButton btn_user_call,btn_arrived,btn_verify,btn_send_code;
        private final TextInputLayout etl_code;
        private final TextInputEditText et_code;
        private final  TextView total_delivered_meal,left_meal,payment_mode,payment_status,plan_type,total_down_plan_price,plan_price,t_refund_value,cancel_credit;
        private final Context vContext;
        private final ProgressBar progressBar_send_code,progressBar_verify_code;
        private ImageView img_down;

        public ViewHolder(@NonNull View itemView,Context context) {
            super(itemView);
            top_meal_type=itemView.findViewById(R.id.meal_type);
            meal_status=itemView.findViewById(R.id.t_today_meal_status);
            tick_img_top=itemView.findViewById(R.id.img_today_meal_status);
            user_name=itemView.findViewById(R.id.t_user_name);
            meal_mess_time=itemView.findViewById(R.id.t_delivery_time_range);
            full_plan_price=itemView.findViewById(R.id.t_plan_price_value);
            refund_amount_value=itemView.findViewById(R.id.t_refund_value);
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

            etl_code=itemView.findViewById(R.id.etl_code);
            et_code=itemView.findViewById(R.id.et_code);

            progressBar_send_code=itemView.findViewById(R.id.progressBar_send_code);
            progressBar_verify_code=itemView.findViewById(R.id.progressBar_verify_code);


            btn_user_call=itemView.findViewById(R.id.btn_call_user);
            btn_verify=itemView.findViewById(R.id.btn_verify);
            btn_arrived=itemView.findViewById(R.id.btn_arrived);
            btn_send_code=itemView.findViewById(R.id.btn_send_code);

            cons_get_refund=itemView.findViewById(R.id.cons_get_refund);
            cons_verify=itemView.findViewById(R.id.cons_verify);
            cons_get_payment=itemView.findViewById(R.id.cons_pay_today);
            cons_normal_info=itemView.findViewById(R.id.cons_normal_info);
            cons_more_detail=itemView.findViewById(R.id.cons_more_detail);


            img_down=itemView.findViewById(R.id.img_down_img);




        }

        private void setAllValue(String meal_type, String v_meal_status, String v_user_name, String v_meal_mess_time, String v_full_plan_price, String v_refund_amount_value, String v_user_address, String v_total_delivered_meal, String v_left_meal, String v_payment_mode, String v_payment_status, String v_plan_type, String v_plan_price, String v_cancel_credit)
        {

            if (Objects.equals(v_meal_status.toLowerCase(), "upcoming")){
                meal_status.setText("Upcoming");
                tick_img_top.setVisibility(View.GONE);
                btn_arrived.setVisibility(View.VISIBLE);
                btn_arrived.setEnabled(true);
                btn_send_code.setEnabled(true);
                btn_user_call.setEnabled(true);
                meal_status.setTextColor(ContextCompat.getColor(vContext,R.color.colorPrimary));


            }else if (Objects.equals(v_meal_status.toLowerCase(),"delivered")){
                meal_status.setText("Delivered");

                meal_status.setTextColor(ContextCompat.getColor(vContext,R.color.green));
                tick_img_top.setVisibility(View.VISIBLE);
                btn_arrived.setEnabled(false);
                btn_send_code.setEnabled(false);
                btn_user_call.setEnabled(false);
                btn_send_code.setVisibility(View.GONE);



            }




            if (Integer.parseInt(v_total_delivered_meal)==0 && v_payment_status.equalsIgnoreCase("not done")){
                cons_get_payment.setVisibility(View.VISIBLE);
            }else {
                cons_get_payment.setVisibility(View.GONE);

            }

            if (Integer.parseInt(v_left_meal)==1 && Integer.parseInt(v_refund_amount_value)!=0){
                cons_get_refund.setVisibility(View.VISIBLE);


            }else {
                cons_get_refund.setVisibility(View.GONE);


            }
            if (v_payment_status.equalsIgnoreCase("not done")){

                payment_status.setText("Not Paid");
            }else {

                payment_status.setText("Paid");
                payment_status.setTextColor(ContextCompat.getColor(vContext,R.color.green));


            }
            top_meal_type.setText(meal_type);
            user_name.setText(v_user_name);
            meal_mess_time.setText(v_meal_mess_time);
            full_plan_price.setText("\u20B9 "+v_full_plan_price);
            total_down_plan_price.setText("\u20B9 "+v_full_plan_price);
            refund_amount_value.setText("\u20B9 "+v_refund_amount_value);
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

    public Filter getFilter() {
        return filter;
    }

    private final Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<TodayMealsModel> filteredList = new ArrayList<>();
            if (charSequence == null || charSequence.length() == 0) {
                filteredList.addAll(tempmealModelList);
            } else {
                String filterPattern = charSequence.toString().toLowerCase();
                boolean breakfast=false,lunch=false,dinner=false;
                for (TodayMealsModel item : tempmealModelList) {

                    if (filterPattern.equals("breakfast")) {
                        breakfast = true;
                        if (item.getTop_meal_type().equalsIgnoreCase("breakfast")) {

                            filteredList.add(item);
                        }
                    }

                    if (filterPattern.equals("lunch")) {
                        lunch = true;
                        if (item.getTop_meal_type().equalsIgnoreCase("lunch")) {
                            filteredList.add(item);
                        }

                    }

                    if (filterPattern.equals("dinner")) {
                        dinner = true;
                        if (item.getTop_meal_type().equalsIgnoreCase("dinner")) {
                            filteredList.add(item);
                        }

                    }

                        if (item.getUser_name().toLowerCase().contains(filterPattern)){
                            filteredList.add(item);
                        }


                }
                if (breakfast){

                    IAllViewTodayMeals.countMealType(filteredList.size(),"breakfast");
                }else if (lunch){
                    IAllViewTodayMeals.countMealType(filteredList.size(),"lunch");

                }else if (dinner){
                    IAllViewTodayMeals.countMealType(filteredList.size(),"dinner");


                }

                if (filteredList.isEmpty()){
                    Toast.makeText(mContext, "Sorry, No Meal found", Toast.LENGTH_SHORT).show();

                }
            }
            FilterResults filterResults= new FilterResults();
            filterResults.values=filteredList;

            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            todayMealsModelList.clear();
            todayMealsModelList.addAll((Collection<? extends TodayMealsModel>) filterResults.values);
            notifyDataSetChanged();
        }
    };




}
