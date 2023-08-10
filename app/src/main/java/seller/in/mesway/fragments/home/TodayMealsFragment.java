package seller.in.mesway.fragments.home;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import seller.in.mesway.R;
import seller.in.mesway.adapters.TodayMealsAdapter;
import seller.in.mesway.client.ApiClient;
import seller.in.mesway.client.ApiInterface;
import seller.in.mesway.databinding.FragmentTodayMealsBinding;
import seller.in.mesway.models.TodayMealsModel;
import seller.in.mesway.response.DetailResponse;
import seller.in.mesway.response.todayMeal.TodayMeals;
import seller.in.mesway.reusable.Constant;
import seller.in.mesway.reusable.EncryptedSharedPreferencesInstance;
import seller.in.mesway.reusable.Reusable;
import seller.in.mesway.viewModels.TodayMealViewModel;

public class TodayMealsFragment extends Fragment {
    private FragmentTodayMealsBinding todayMealsBinding;
    private Snackbar snackbar;
    private boolean isConnected;
    private Activity activity;
    private AlertDialog alertDialog;
    private SharedPreferences sharedPreferences;
    private ApiInterface apiInterface;
    private String date_with_day = "none";
    private Date current_date;
    private String date_in_str;
    private NavController navController;
    private TodayMealsAdapter todayMealsAdapter;
    private AllViewTodayMeals IAllViewTodayMeals;


    private TodayMealViewModel todayMealViewModel;
    private TodayMeals today_Meals;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        todayMealsBinding = FragmentTodayMealsBinding.inflate(inflater, container, false);
        return todayMealsBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        if (isConnected) {
            dateLoading(view);
            isLoadingObserver();

        } else {
            snackbar = Snackbar.make(view, "No Internet Connection", Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction("Refresh", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    todayMealViewModel.loadTodayMeals(activity);
                    snackbar.dismiss();
                }
            });
            snackbar.setBackgroundTint(ContextCompat.getColor(activity, R.color.red));
            snackbar.show();
        }
    }

    private void isLoadingObserver() {
        todayMealViewModel.isLoading.observe((LifecycleOwner) activity, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    alertDialog.show();

                } else {
                    alertDialog.dismiss();

                }
            }
        });




    }

    private void dateLoading(View views) {
        alertDialog.show();

        Call<String> getDate = apiInterface.get_date_as_date();

        getDate.enqueue(new Callback<String>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                if (response.code() == 200) {
                    assert response.body() != null;
                    String dtString = response.body();
                    date_in_str = dtString;
                    todayMealsStatusObserver(views);

                    @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = new Date();
                    try {
                        date = simpleDateFormat.parse(dtString);
                        current_date = date;


                    } catch (ParseException e) {
                        e.printStackTrace();
                        alertDialog.dismiss();

                    }
                    if (date != null) {
                        String dayOfWeek = (String) DateFormat.format("EEE", date);
                        String monthString = (String) DateFormat.format("MMM", date);
                        String year = (String) DateFormat.format("yyyy", date);
                        String day = (String) DateFormat.format("dd", date);
                        date_with_day = dayOfWeek + ", " + day + " " + monthString + " " + year;
                        todayMealsBinding.tTodayDate.setText(date_with_day);

                    } else {

                        todayMealsBinding.tTodayDate.setText(response.body());
                        date_with_day = response.body();
                        alertDialog.dismiss();


                    }

                    todayMealsBinding.dateShimmer.stopShimmer();
                    todayMealsBinding.dateShimmer.setVisibility(View.GONE);
                    todayMealsBinding.tTodayDate.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                alertDialog.dismiss();
            }
        });

        clickHandle(views);
    }

    private void clickHandle(View views) {

        todayMealsBinding.imgRefreshUpcomingMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.show();
                todayMealViewModel.loadTodayMeals(activity);

            }
        });
        final int[] img_search_count = {0};
        todayMealsBinding.imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (img_search_count[0] ==1){
                    todayMealsBinding.etSearchByName.clearFocus();
                    hideKeyboard();
                    todayMealsBinding.lnFilterLayout.setVisibility(View.VISIBLE);
                    todayMealsBinding.etlSearchByName.setVisibility(View.INVISIBLE);

                    img_search_count[0]=0;

                }else {
                    todayMealsBinding.lnFilterLayout.setVisibility(View.INVISIBLE);
                    todayMealsBinding.etlSearchByName.setVisibility(View.VISIBLE);
                    img_search_count[0]++;



                }
            }
        });

        todayMealsBinding.etSearchByName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim()!=null){

                    todayMealsAdapter.getFilter().filter(s);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        todayMealsBinding.chipGroup.setOnCheckedStateChangeListener(new ChipGroup.OnCheckedStateChangeListener() {
            @Override
            public void onCheckedChanged(@NonNull ChipGroup group, @NonNull List<Integer> checkedIds) {
                if (!checkedIds.isEmpty()){
                    Chip checkChip= activity.findViewById(checkedIds.get(0));
                    if (checkChip.getText().equals("Breakfast")){
                        if (checkChip.isChecked()) {
                            todayMealsAdapter.getFilter().filter("breakfast");
                        }
                    } else if (checkChip.getText().equals("Lunch")) {
                        if (checkChip.isChecked()){
                            todayMealsAdapter.getFilter().filter("lunch");
                        }
                    }else if (checkChip.getText().equals("Dinner")) {
                        if (checkChip.isChecked()){
                            todayMealsAdapter.getFilter().filter("dinner");
                        }
                    }

                }else {

                    todayMealsAdapter.getFilter().filter(null);
                }

            }
        });
    }

    private void todayMealsStatusObserver(View view) {
        todayMealViewModel.status_code.observe((LifecycleOwner) activity, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if (integer == 200) {
                    getTodayMealsObserver(view);
                } else {
                    getTodayMealDetailObserver(view);
                }
            }
        });

    }

    private void getTodayMealDetailObserver(View view) {
        todayMealViewModel.detail.observe((LifecycleOwner) activity, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                snackbar = Snackbar.make(view, s, Snackbar.LENGTH_SHORT);
                snackbar.setBackgroundTint(ContextCompat.getColor(activity, R.color.red));
                snackbar.show();
            }
        });
    }

    private void getTodayMealsObserver(View view) {
        todayMealViewModel.todayMealsMutableLiveData.observe((LifecycleOwner) activity, new Observer<TodayMeals>() {
            @Override
            public void onChanged(TodayMeals todayMeals) {
                today_Meals = todayMeals;
                recViewOperation(todayMeals, view);

            }
        });
    }

    private void recViewOperation(TodayMeals todayMeals, View views) {
        IAllViewTodayMeals = new AllViewTodayMeals() {
            @Override
            public void allViewOfTodayMeal(TextInputLayout etl_code, TextInputEditText et_code, MaterialButton btn_call_user, MaterialButton btn_verify, MaterialButton btn_arrived, ConstraintLayout cons_give_refund, ConstraintLayout cons_verify, ConstraintLayout cons_get_payment, ConstraintLayout cons_normal_info, ConstraintLayout cons_more_detail, String address_number, String user_number, String user_id, String subs_id, MaterialButton btn_send_code, ProgressBar progressbar_send_code, String meal_type, ProgressBar progressBar_verify_code, ImageView img_down, TextView t_more_detail,String full_payment) {
                final int[] cons_more_detail_count = {0};
                cons_more_detail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (cons_more_detail_count[0] == 0) {
                            cons_normal_info.setVisibility(View.VISIBLE);
                            t_more_detail.setText("Less detail");
                            img_down.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.ic_baseline_arrow_drop_up_24));
                            cons_more_detail_count[0]++;
                        } else {
                            cons_normal_info.setVisibility(View.GONE);
                            t_more_detail.setText("More detail");
                            img_down.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.ic_baseline_arrow_drop_down));

                            cons_more_detail_count[0] = 0;


                        }
                    }
                });

                validateCode(et_code, etl_code);


                btn_call_user.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent call = new Intent(Intent.ACTION_DIAL);
                        String number = "tel:" + address_number;
                        call.setData(Uri.parse(number));
                        startActivity(call);

                    }
                });

                btn_call_user.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        Intent call = new Intent(Intent.ACTION_DIAL);
                        String number = "tel:" + user_number;
                        call.setData(Uri.parse(number));
                        startActivity(call);

                        return false;
                    }
                });

                btn_arrived.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        btn_arrived.setVisibility(View.GONE);
                        btn_send_code.setVisibility(View.VISIBLE);
                    }
                });

                btn_send_code.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sendDeliveryCode(views, btn_send_code, user_id, subs_id, progressbar_send_code, meal_type, cons_verify,full_payment);

                    }
                });

                btn_verify.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (clickVerifyCode(et_code, etl_code)) {
                            verifyDeliveryCode(views, btn_verify, meal_type, subs_id, cons_verify, progressBar_verify_code, et_code);
                        }
                    }
                });


            }

            @Override
            public void countMealType(int meal_count,String meal_type) {
                if (meal_type.equalsIgnoreCase("breakfast")){

                    snackbar = Snackbar.make(views, "Breakfast tiffin orders: "+meal_count, Snackbar.LENGTH_INDEFINITE);
                    snackbar.setBackgroundTint(ContextCompat.getColor(activity, R.color.green));
                    snackbar.setAction("Ok", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            snackbar.dismiss();
                        }
                    });
                    snackbar.show();
                }else if (meal_type.equalsIgnoreCase("lunch"))    {

                    snackbar = Snackbar.make(views, "Lunch tiffin orders: "+meal_count, Snackbar.LENGTH_INDEFINITE);
                    snackbar.setBackgroundTint(ContextCompat.getColor(activity, R.color.green));
                    snackbar.setAction("Ok", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            snackbar.dismiss();
                        }
                    });
                    snackbar.show();
                }else if (meal_type.equalsIgnoreCase("dinner")){
                    snackbar = Snackbar.make(views, "Dinner tiffin orders: "+meal_count, Snackbar.LENGTH_INDEFINITE);
                    snackbar.setBackgroundTint(ContextCompat.getColor(activity, R.color.green));
                    snackbar.setAction("Ok", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            snackbar.dismiss();
                        }
                    });
                    snackbar.show();
                }
            }
        };
        todayMealsAdapter = new TodayMealsAdapter(getTodayMealOperationsList(todayMeals), activity, IAllViewTodayMeals);
        todayMealsBinding.recViewTodayMeal.setAdapter(todayMealsAdapter);
        todayMealsBinding.recViewTodayMeal.setLayoutManager(new LinearLayoutManager(activity));
        todayMealsBinding.recViewTodayMeal.setNestedScrollingEnabled(false);


    }

    private void validateCode(TextInputEditText et_code, TextInputLayout etl_code) {
        et_code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                clickVerifyCode(et_code, etl_code);

            }
        });
    }

    private boolean clickVerifyCode(TextInputEditText et_code, TextInputLayout etl_code) {
        if (!TextUtils.isEmpty(et_code.getText())) {
            if (et_code.getText().length() == 6) {

                etl_code.setErrorEnabled(false);

                return true;


            } else {
                etl_code.setErrorEnabled(true);

                return false;

            }

        }
        etl_code.setErrorEnabled(true);
        return false;
    }

    private void hideKeyboard() {
        View views = activity.getCurrentFocus();
        if (views != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(views.getWindowToken(), 0);

        }

    }

    private void verifyDeliveryCode(View view, MaterialButton btn_verify, String meal_type, String subs_id, ConstraintLayout cons_verify, ProgressBar progressBar_verify_code, TextInputEditText et_code) {
        hideKeyboard();
        progressBar_verify_code.setVisibility(View.VISIBLE);
        btn_verify.setVisibility(View.GONE);
        Call<Boolean> booleanCall = apiInterface.give_delivery(Constant.TOKEN_TYPE_VALUE + sharedPreferences.getString(Constant.ACCESS_TOKEN, null), UUID.fromString(sharedPreferences.getString(Constant.MESS_ID, null)), UUID.fromString(subs_id), et_code.getText().toString(), meal_type);
        booleanCall.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(@NonNull Call<Boolean> call, @NonNull Response<Boolean> response) {
                Gson gson = new GsonBuilder().create();
                if (response.code() == 200) {
                    progressBar_verify_code.setVisibility(View.GONE);
                    btn_verify.setVisibility(View.VISIBLE);
                    cons_verify.setVisibility(View.GONE);
                    alertDialog.show();
                    todayMealViewModel.loadTodayMeals(activity);
                    snackbar = Snackbar.make(view, meal_type + " Delivered Successfully", Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(ContextCompat.getColor(activity, R.color.green));
                    snackbar.show();
                    todayMealViewModel.loadTodayMeals(activity);


                } else{
                    try {
                        DetailResponse detailResponse = gson.fromJson(response.errorBody().charStream(), DetailResponse.class);
                        snackbar = Snackbar.make(view, "" + detailResponse.getDetail(), Snackbar.LENGTH_SHORT);
                        snackbar.setBackgroundTint(ContextCompat.getColor(activity, R.color.red));
                        snackbar.show();
                        btn_verify.setVisibility(View.VISIBLE);

                        progressBar_verify_code.setVisibility(View.GONE);


                    } catch (Exception e) {
                        snackbar = Snackbar.make(view, response.code() + ":Something went error ", Snackbar.LENGTH_SHORT);
                        snackbar.setBackgroundTint(ContextCompat.getColor(activity, R.color.red));

                        snackbar.show();
                        btn_verify.setVisibility(View.VISIBLE);

                        progressBar_verify_code.setVisibility(View.GONE);


                    }
                }

            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                snackbar = Snackbar.make(view, "Failure: Check your internet connection/Something went error", Snackbar.LENGTH_SHORT);
                snackbar.setBackgroundTint(ContextCompat.getColor(activity, R.color.red));
                btn_verify.setVisibility(View.VISIBLE);

                snackbar.show();
                progressBar_verify_code.setVisibility(View.GONE);
            }
        });


    }

    private void enterCode() {


    }

    private void sendDeliveryCode(View view, MaterialButton btn_send_code, String user_id, String subs_id, ProgressBar progressBar, String meal_type, ConstraintLayout cons_verify_layout,String full_payment) {
        progressBar.setVisibility(View.VISIBLE);
        btn_send_code.setVisibility(View.GONE);

        Call<Boolean> booleanCall = apiInterface.send_delivery_code(Constant.TOKEN_TYPE_VALUE + sharedPreferences.getString(Constant.ACCESS_TOKEN, null), UUID.fromString(sharedPreferences.getString(Constant.MESS_ID, null)), UUID.fromString(subs_id), UUID.fromString(user_id), meal_type);
        booleanCall.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(@NonNull Call<Boolean> call, @NonNull Response<Boolean> response) {
                Gson gson = new GsonBuilder().create();
                if (response.code() == 200) {
                    progressBar.setVisibility(View.GONE);
                    btn_send_code.setVisibility(View.VISIBLE);
                    cons_verify_layout.setVisibility(View.VISIBLE);
                    snackbar = Snackbar.make(view, "Delivery Code sent Successfully", Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(ContextCompat.getColor(activity, R.color.green));
                    snackbar.show();


                } else if (response.code() == 303) {
                    try {
                        assert response.errorBody() != null;
                        DetailResponse detailResponse = gson.fromJson(response.errorBody().charStream(), DetailResponse.class);

                        navController.navigate(TodayMealsFragmentDirections.actionTodayMealsFragmentToTakePaymentBottomsheetDialogFragment(sharedPreferences.getString(Constant.MESS_ID,null),subs_id,detailResponse.getDetail(),full_payment));


                        cons_verify_layout.setVisibility(View.GONE);
                        btn_send_code.setVisibility(View.VISIBLE);

                        progressBar.setVisibility(View.GONE);


                    } catch (Exception e) {
                        snackbar = Snackbar.make(view, response.code() + ":Something went error ", Snackbar.LENGTH_SHORT);
                        snackbar.setBackgroundTint(ContextCompat.getColor(activity, R.color.red));
                        cons_verify_layout.setVisibility(View.GONE);

                        snackbar.show();
                        btn_send_code.setVisibility(View.VISIBLE);

                        progressBar.setVisibility(View.GONE);


                    }

                } else{
                    try {
                        DetailResponse detailResponse = gson.fromJson(response.errorBody().charStream(), DetailResponse.class);
                        snackbar = Snackbar.make(view, "" + detailResponse.getDetail(), Snackbar.LENGTH_SHORT);
                        snackbar.setBackgroundTint(ContextCompat.getColor(activity, R.color.red));
                        snackbar.show();
                        cons_verify_layout.setVisibility(View.GONE);
                        btn_send_code.setVisibility(View.VISIBLE);

                        progressBar.setVisibility(View.GONE);


                    } catch (Exception e) {
                        snackbar = Snackbar.make(view, response.code() + ":Something went error ", Snackbar.LENGTH_SHORT);
                        snackbar.setBackgroundTint(ContextCompat.getColor(activity, R.color.red));
                        cons_verify_layout.setVisibility(View.GONE);

                        snackbar.show();
                        btn_send_code.setVisibility(View.VISIBLE);

                        progressBar.setVisibility(View.GONE);


                    }
            }

            }

            @Override
            public void onFailure(@NonNull Call<Boolean> call, @NonNull Throwable t) {
                snackbar = Snackbar.make(view, "Failure: Check your internet connection/Something went error", Snackbar.LENGTH_SHORT);
                snackbar.setBackgroundTint(ContextCompat.getColor(activity, R.color.red));
                cons_verify_layout.setVisibility(View.GONE);
                btn_send_code.setVisibility(View.VISIBLE);

                snackbar.show();
                progressBar.setVisibility(View.GONE);
            }
        });


    }

    @SuppressLint("SetTextI18n")
    private List<TodayMealsModel> getTodayMealOperationsList(TodayMeals todayMeals) {
        List<TodayMealsModel> todayMealsModelList = new ArrayList<>();
        int total_meal_count = 0, deliver_meal_count = 0, left_meal_count = 0;
        for (int i = 0; i < todayMeals.getTodayMeal().size(); i++) {
            int subs_left_meal_count = todayMeals.getTodayMeal().get(i).getLeftMeals();

            String first_meal = "none", second_meal = "none", third_meal = "none";
           // if (todayMeals.getTodayMeal().get(i).getDeliveredMeal()!=0){



            if (!Objects.equals(todayMeals.getTodayMeal().get(i).getStartingMeal(), "none")) {

                first_meal = todayMeals.getTodayMeal().get(i).getStartingMeal();
            }

            if (!Objects.equals(todayMeals.getTodayMeal().get(i).getSecondMeal(), "none")) {

                second_meal = todayMeals.getTodayMeal().get(i).getSecondMeal();
            }

            if (!Objects.equals(todayMeals.getTodayMeal().get(i).getThirdMeal(), "none")) {
                third_meal = todayMeals.getTodayMeal().get(i).getThirdMeal();
            }


            if (todayMeals.getTodayMeal().get(i).getIsFirstOrder()){


                    if (Objects.equals(todayMeals.getTodayMeal().get(i).getStartingMeal(),"breakfast")){
                        first_meal="none";
                        second_meal="none";
                        third_meal="none";
                        if (!Objects.equals(todayMeals.getTodayMeal().get(i).getStartingMeal(), "none")) {

                            first_meal = todayMeals.getTodayMeal().get(i).getStartingMeal();
                        }

                        if (!Objects.equals(todayMeals.getTodayMeal().get(i).getSecondMeal(), "none")) {
                            second_meal = todayMeals.getTodayMeal().get(i).getSecondMeal();
                        }

                        if (!Objects.equals(todayMeals.getTodayMeal().get(i).getThirdMeal(), "none")) {
                            third_meal = todayMeals.getTodayMeal().get(i).getThirdMeal();
                        }

                    }else if (Objects.equals(todayMeals.getTodayMeal().get(i).getStartingMeal(),"lunch")){
                        first_meal="none";
                        second_meal="none";
                        third_meal="none";

                        if (!Objects.equals(todayMeals.getTodayMeal().get(i).getStartingMeal(), "none")) {

                            first_meal = "lunch";
                        }

                        if (!Objects.equals(todayMeals.getTodayMeal().get(i).getSecondMeal(), "none")) {
                            if (Objects.equals(todayMeals.getTodayMeal().get(i).getSecondMeal(), "dinner")){

                                second_meal = "dinner";
                            }
                        }

                        if (!Objects.equals(todayMeals.getTodayMeal().get(i).getThirdMeal(), "none")) {
                            if (Objects.equals(todayMeals.getTodayMeal().get(i).getThirdMeal(), "dinner")){

                                third_meal = "dinner";
                            }

                        }

                    }else if (Objects.equals(todayMeals.getTodayMeal().get(i).getStartingMeal(),"dinner")){
                        second_meal="none";
                        third_meal="none";

                            first_meal = "dinner";



                    }




            }

           // Toast.makeText(activity, i+" "+first_meal+" "+second_meal+" "+third_meal, Toast.LENGTH_SHORT).show();
            boolean is_lunch_available = Objects.equals("lunch", first_meal) | Objects.equals("lunch", second_meal) | Objects.equals("lunch", third_meal);
            boolean is_dinner_available = Objects.equals("dinner", first_meal) | Objects.equals("dinner", second_meal) | Objects.equals("dinner", third_meal);
            boolean is_breakfast_available=Objects.equals("breakfast",first_meal)|Objects.equals("breakfast",second_meal)|Objects.equals("breakfast",third_meal);
            if (todayMeals.getTodayMeal().get(i).getDate() != null) {


                @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date date = new Date();
                String date_in_string = todayMeals.getTodayMeal().get(i).getDate();
                try {
                    date = simpleDateFormat.parse(date_in_string);


                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if (date_in_string.equals(date_in_str)) {
                    if (is_breakfast_available) {
                        if (Objects.equals("breakfast",first_meal )) {
                            if (!Objects.equals(todayMeals.getTodayMeal().get(i).getBreakfast(), "cancel")) {
                                if (subs_left_meal_count != 0) {
                                    if (todayMeals.isBreakfast()) {


                                        if (!Objects.equals(todayMeals.getTodayMeal().get(i).getBreakfast(), "delivered")) {
                                            todayMealsModelList.add(new TodayMealsModel("Breakfast", "upcoming", todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getFullName(), todayMeals.getTodayMeal().get(i).getMessInfo().getMessTime().get(0).getBreakfastTime(), todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getMobileNumber(), todayMeals.getTodayMeal().get(i).getUserInfo().getMobileNumber(), todayMeals.getTodayMeal().get(i).getUserId(), todayMeals.getTodayMeal().get(i).getPaymentValue(), todayMeals.getTodayMeal().get(i).getSecurityMoney() + "", todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getFullAddress() + ", " + todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getLandmark(), todayMeals.getTodayMeal().get(i).getSubscriptionId(), todayMeals.getTodayMeal().get(i).getDeliveredMeal() + "", todayMeals.getTodayMeal().get(i).getLeftMeals() + "", todayMeals.getTodayMeal().get(i).getPaymentBy(), todayMeals.getTodayMeal().get(i).getPaymentStatus(), todayMeals.getTodayMeal().get(i).getPlanType(), todayMeals.getTodayMeal().get(i).getPlanPrice(), todayMeals.getTodayMeal().get(i).getAllowedCancelMeal() + ""));
                                            total_meal_count++;
                                            subs_left_meal_count--;

                                        } else {
                                            //just pass status delivered
                                            todayMealsModelList.add(new TodayMealsModel("Breakfast", "delivered", todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getFullName(),  todayMeals.getTodayMeal().get(i).getMessInfo().getMessTime().get(0).getBreakfastTime(), todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getMobileNumber(), todayMeals.getTodayMeal().get(i).getUserInfo().getMobileNumber(), todayMeals.getTodayMeal().get(i).getUserId(), todayMeals.getTodayMeal().get(i).getPaymentValue(), todayMeals.getTodayMeal().get(i).getSecurityMoney() + "", todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getFullAddress() + ", " + todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getLandmark(), todayMeals.getTodayMeal().get(i).getSubscriptionId(), todayMeals.getTodayMeal().get(i).getDeliveredMeal() + "", todayMeals.getTodayMeal().get(i).getLeftMeals() + "", todayMeals.getTodayMeal().get(i).getPaymentBy(), todayMeals.getTodayMeal().get(i).getPaymentStatus(), todayMeals.getTodayMeal().get(i).getPlanType(), todayMeals.getTodayMeal().get(i).getPlanPrice(), todayMeals.getTodayMeal().get(i).getAllowedCancelMeal() + ""));
                                            total_meal_count++;

                                            deliver_meal_count++;


                                        }
                                    }
                                }
                            }
                        } else if (Objects.equals("breakfast", second_meal)) {


                            if (!Objects.equals(todayMeals.getTodayMeal().get(i).getBreakfast(), "cancel")) {
                                if (subs_left_meal_count != 0) {
                                    if (todayMeals.isBreakfast()) {

                                        if (!Objects.equals(todayMeals.getTodayMeal().get(i).getBreakfast(), "delivered")) {
                                            todayMealsModelList.add(new TodayMealsModel("Breakfast", "upcoming", todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getFullName(),  todayMeals.getTodayMeal().get(i).getMessInfo().getMessTime().get(0).getLunchTime(), todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getMobileNumber(), todayMeals.getTodayMeal().get(i).getUserInfo().getMobileNumber(), todayMeals.getTodayMeal().get(i).getUserId(), todayMeals.getTodayMeal().get(i).getPaymentValue(), todayMeals.getTodayMeal().get(i).getSecurityMoney() + "", todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getFullAddress() + ", " + todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getLandmark(), todayMeals.getTodayMeal().get(i).getSubscriptionId(), todayMeals.getTodayMeal().get(i).getDeliveredMeal() + "", todayMeals.getTodayMeal().get(i).getLeftMeals() + "", todayMeals.getTodayMeal().get(i).getPaymentBy(), todayMeals.getTodayMeal().get(i).getPaymentStatus(), todayMeals.getTodayMeal().get(i).getPlanType(), todayMeals.getTodayMeal().get(i).getPlanPrice(), todayMeals.getTodayMeal().get(i).getAllowedCancelMeal() + ""));
                                            total_meal_count++;
                                            subs_left_meal_count--;

                                        } else {
                                            //just pass status delivered
                                            todayMealsModelList.add(new TodayMealsModel("Breakfast", "delivered", todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getFullName(),  todayMeals.getTodayMeal().get(i).getMessInfo().getMessTime().get(0).getLunchTime(), todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getMobileNumber(), todayMeals.getTodayMeal().get(i).getUserInfo().getMobileNumber(), todayMeals.getTodayMeal().get(i).getUserId(), todayMeals.getTodayMeal().get(i).getPaymentValue(), todayMeals.getTodayMeal().get(i).getSecurityMoney() + "", todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getFullAddress() + ", " + todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getLandmark(), todayMeals.getTodayMeal().get(i).getSubscriptionId(), todayMeals.getTodayMeal().get(i).getDeliveredMeal() + "", todayMeals.getTodayMeal().get(i).getLeftMeals() + "", todayMeals.getTodayMeal().get(i).getPaymentBy(), todayMeals.getTodayMeal().get(i).getPaymentStatus(), todayMeals.getTodayMeal().get(i).getPlanType(), todayMeals.getTodayMeal().get(i).getPlanPrice(), todayMeals.getTodayMeal().get(i).getAllowedCancelMeal() + ""));
                                            total_meal_count++;

                                            deliver_meal_count++;

                                        }
                                    }
                                }
                            }

                        } else if (Objects.equals("breakfast", third_meal)) {

                            if (!Objects.equals(todayMeals.getTodayMeal().get(i).getBreakfast(), "cancel")) {
                                if (subs_left_meal_count != 0) {
                                    if (todayMeals.isBreakfast()) {

                                        if (!Objects.equals(todayMeals.getTodayMeal().get(i).getBreakfast(), "delivered")) {
                                            todayMealsModelList.add(new TodayMealsModel("Breakfast", "upcoming", todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getFullName(),  todayMeals.getTodayMeal().get(i).getMessInfo().getMessTime().get(0).getDinnerTime(), todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getMobileNumber(), todayMeals.getTodayMeal().get(i).getUserInfo().getMobileNumber(), todayMeals.getTodayMeal().get(i).getUserId(), todayMeals.getTodayMeal().get(i).getPaymentValue(), todayMeals.getTodayMeal().get(i).getSecurityMoney() + "", todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getFullAddress() + ", " + todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getLandmark(), todayMeals.getTodayMeal().get(i).getSubscriptionId(), todayMeals.getTodayMeal().get(i).getDeliveredMeal() + "", todayMeals.getTodayMeal().get(i).getLeftMeals() + "", todayMeals.getTodayMeal().get(i).getPaymentBy(), todayMeals.getTodayMeal().get(i).getPaymentStatus(), todayMeals.getTodayMeal().get(i).getPlanType(), todayMeals.getTodayMeal().get(i).getPlanPrice(), todayMeals.getTodayMeal().get(i).getAllowedCancelMeal() + ""));
                                            total_meal_count++;
                                            subs_left_meal_count--;

                                        } else {
                                            //just pass status delivered
                                            todayMealsModelList.add(new TodayMealsModel("Breakfast", "delivered", todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getFullName(),  todayMeals.getTodayMeal().get(i).getMessInfo().getMessTime().get(0).getDinnerTime(), todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getMobileNumber(), todayMeals.getTodayMeal().get(i).getUserInfo().getMobileNumber(), todayMeals.getTodayMeal().get(i).getUserId(), todayMeals.getTodayMeal().get(i).getPaymentValue(), todayMeals.getTodayMeal().get(i).getSecurityMoney() + "", todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getFullAddress() + ", " + todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getLandmark(), todayMeals.getTodayMeal().get(i).getSubscriptionId(), todayMeals.getTodayMeal().get(i).getDeliveredMeal() + "", todayMeals.getTodayMeal().get(i).getLeftMeals() + "", todayMeals.getTodayMeal().get(i).getPaymentBy(), todayMeals.getTodayMeal().get(i).getPaymentStatus(), todayMeals.getTodayMeal().get(i).getPlanType(), todayMeals.getTodayMeal().get(i).getPlanPrice(), todayMeals.getTodayMeal().get(i).getAllowedCancelMeal() + ""));
                                            total_meal_count++;

                                            deliver_meal_count++;


                                        }
                                    }

                                }

                            }
                        }
                    }

                    if (is_lunch_available) {
                        if (Objects.equals("lunch",first_meal)) {

                            //check kro ki delivered to nhi h ydi hai to status deliverd kro
                            if (!Objects.equals(todayMeals.getTodayMeal().get(i).getLunch(), "cancel")) {
                                if (subs_left_meal_count != 0) {
                                    if (todayMeals.isLunch()) {

                                        if (!Objects.equals(todayMeals.getTodayMeal().get(i).getLunch(), "delivered")) {
                                            todayMealsModelList.add(new TodayMealsModel("Lunch", "upcoming", todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getFullName(),  todayMeals.getTodayMeal().get(i).getMessInfo().getMessTime().get(0).getBreakfastTime(), todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getMobileNumber(), todayMeals.getTodayMeal().get(i).getUserInfo().getMobileNumber(), todayMeals.getTodayMeal().get(i).getUserId(), todayMeals.getTodayMeal().get(i).getPaymentValue(), todayMeals.getTodayMeal().get(i).getSecurityMoney() + "", todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getFullAddress() + ", " + todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getLandmark(), todayMeals.getTodayMeal().get(i).getSubscriptionId(), todayMeals.getTodayMeal().get(i).getDeliveredMeal() + "", todayMeals.getTodayMeal().get(i).getLeftMeals() + "", todayMeals.getTodayMeal().get(i).getPaymentBy(), todayMeals.getTodayMeal().get(i).getPaymentStatus(), todayMeals.getTodayMeal().get(i).getPlanType(), todayMeals.getTodayMeal().get(i).getPlanPrice(), todayMeals.getTodayMeal().get(i).getAllowedCancelMeal() + ""));
                                            total_meal_count++;
                                            subs_left_meal_count--;

                                        } else {
                                            //just pass status delivered
                                            todayMealsModelList.add(new TodayMealsModel("Lunch", "delivered", todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getFullName(),  todayMeals.getTodayMeal().get(i).getMessInfo().getMessTime().get(0).getBreakfastTime(), todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getMobileNumber(), todayMeals.getTodayMeal().get(i).getUserInfo().getMobileNumber(), todayMeals.getTodayMeal().get(i).getUserId(), todayMeals.getTodayMeal().get(i).getPaymentValue(), todayMeals.getTodayMeal().get(i).getSecurityMoney() + "", todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getFullAddress() + ", " + todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getLandmark(), todayMeals.getTodayMeal().get(i).getSubscriptionId(), todayMeals.getTodayMeal().get(i).getDeliveredMeal() + "", todayMeals.getTodayMeal().get(i).getLeftMeals() + "", todayMeals.getTodayMeal().get(i).getPaymentBy(), todayMeals.getTodayMeal().get(i).getPaymentStatus(), todayMeals.getTodayMeal().get(i).getPlanType(), todayMeals.getTodayMeal().get(i).getPlanPrice(), todayMeals.getTodayMeal().get(i).getAllowedCancelMeal() + ""));
                                            total_meal_count++;

                                            deliver_meal_count++;

                                        }
                                    }
                                }
                            }

                        } else if (Objects.equals("lunch",second_meal)) {

                            if (!Objects.equals(todayMeals.getTodayMeal().get(i).getLunch(), "cancel")) {
                                if (subs_left_meal_count != 0) {
                                    if (todayMeals.isLunch()) {

                                        if (!Objects.equals(todayMeals.getTodayMeal().get(i).getLunch(), "delivered")) {
                                            todayMealsModelList.add(new TodayMealsModel("Lunch", "upcoming", todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getFullName(),  todayMeals.getTodayMeal().get(i).getMessInfo().getMessTime().get(0).getLunchTime(), todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getMobileNumber(), todayMeals.getTodayMeal().get(i).getUserInfo().getMobileNumber(), todayMeals.getTodayMeal().get(i).getUserId(), todayMeals.getTodayMeal().get(i).getPaymentValue(), todayMeals.getTodayMeal().get(i).getSecurityMoney() + "", todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getFullAddress() + ", " + todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getLandmark(), todayMeals.getTodayMeal().get(i).getSubscriptionId(), todayMeals.getTodayMeal().get(i).getDeliveredMeal() + "", todayMeals.getTodayMeal().get(i).getLeftMeals() + "", todayMeals.getTodayMeal().get(i).getPaymentBy(), todayMeals.getTodayMeal().get(i).getPaymentStatus(), todayMeals.getTodayMeal().get(i).getPlanType(), todayMeals.getTodayMeal().get(i).getPlanPrice(), todayMeals.getTodayMeal().get(i).getAllowedCancelMeal() + ""));

                                            total_meal_count++;
                                            subs_left_meal_count--;
                                        } else {
                                            //just pass status delivered
                                            todayMealsModelList.add(new TodayMealsModel("Lunch", "delivered", todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getFullName(),  todayMeals.getTodayMeal().get(i).getMessInfo().getMessTime().get(0).getLunchTime(), todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getMobileNumber(), todayMeals.getTodayMeal().get(i).getUserInfo().getMobileNumber(), todayMeals.getTodayMeal().get(i).getUserId(), todayMeals.getTodayMeal().get(i).getPaymentValue(), todayMeals.getTodayMeal().get(i).getSecurityMoney() + "", todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getFullAddress() + ", " + todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getLandmark(), todayMeals.getTodayMeal().get(i).getSubscriptionId(), todayMeals.getTodayMeal().get(i).getDeliveredMeal() + "", todayMeals.getTodayMeal().get(i).getLeftMeals() + "", todayMeals.getTodayMeal().get(i).getPaymentBy(), todayMeals.getTodayMeal().get(i).getPaymentStatus(), todayMeals.getTodayMeal().get(i).getPlanType(), todayMeals.getTodayMeal().get(i).getPlanPrice(), todayMeals.getTodayMeal().get(i).getAllowedCancelMeal() + ""));
                                            total_meal_count++;

                                            deliver_meal_count++;


                                        }
                                    }
                                }
                            }


                        } else if (Objects.equals("lunch",third_meal)) {

                            if (!Objects.equals(todayMeals.getTodayMeal().get(i).getLunch(), "cancel")) {
                                if (subs_left_meal_count != 0) {
                                    if (todayMeals.isLunch()) {


                                        if (!Objects.equals(todayMeals.getTodayMeal().get(i).getLunch(), "delivered")) {
                                            todayMealsModelList.add(new TodayMealsModel("Lunch", "upcoming", todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getFullName(),  todayMeals.getTodayMeal().get(i).getMessInfo().getMessTime().get(0).getDinnerTime(), todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getMobileNumber(), todayMeals.getTodayMeal().get(i).getUserInfo().getMobileNumber(), todayMeals.getTodayMeal().get(i).getUserId(), todayMeals.getTodayMeal().get(i).getPaymentValue(), todayMeals.getTodayMeal().get(i).getSecurityMoney() + "", todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getFullAddress() + ", " + todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getLandmark(), todayMeals.getTodayMeal().get(i).getSubscriptionId(), todayMeals.getTodayMeal().get(i).getDeliveredMeal() + "", todayMeals.getTodayMeal().get(i).getLeftMeals() + "", todayMeals.getTodayMeal().get(i).getPaymentBy(), todayMeals.getTodayMeal().get(i).getPaymentStatus(), todayMeals.getTodayMeal().get(i).getPlanType(), todayMeals.getTodayMeal().get(i).getPlanPrice(), todayMeals.getTodayMeal().get(i).getAllowedCancelMeal() + ""));
                                            total_meal_count++;
                                            subs_left_meal_count--;

                                        } else {
                                            //just pass status delivered
                                            todayMealsModelList.add(new TodayMealsModel("Lunch", "delivered", todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getFullName(),  todayMeals.getTodayMeal().get(i).getMessInfo().getMessTime().get(0).getDinnerTime(), todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getMobileNumber(), todayMeals.getTodayMeal().get(i).getUserInfo().getMobileNumber(), todayMeals.getTodayMeal().get(i).getUserId(), todayMeals.getTodayMeal().get(i).getPaymentValue(), todayMeals.getTodayMeal().get(i).getSecurityMoney() + "", todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getFullAddress() + ", " + todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getLandmark(), todayMeals.getTodayMeal().get(i).getSubscriptionId(), todayMeals.getTodayMeal().get(i).getDeliveredMeal() + "", todayMeals.getTodayMeal().get(i).getLeftMeals() + "", todayMeals.getTodayMeal().get(i).getPaymentBy(), todayMeals.getTodayMeal().get(i).getPaymentStatus(), todayMeals.getTodayMeal().get(i).getPlanType(), todayMeals.getTodayMeal().get(i).getPlanPrice(), todayMeals.getTodayMeal().get(i).getAllowedCancelMeal() + ""));
                                            total_meal_count++;

                                            deliver_meal_count++;

                                        }
                                    }
                                }

                            }
                        }
                    }

                    if (is_dinner_available) {

                        if (Objects.equals(first_meal, "dinner")) {

                            if (!Objects.equals(todayMeals.getTodayMeal().get(i).getDinner(), "cancel")) {
                                if (subs_left_meal_count != 0) {
                                    if (todayMeals.isDinner()) {

                                        if (!Objects.equals(todayMeals.getTodayMeal().get(i).getDinner(), "delivered")) {
                                            todayMealsModelList.add(new TodayMealsModel("Dinner", "upcoming", todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getFullName(),  todayMeals.getTodayMeal().get(i).getMessInfo().getMessTime().get(0).getBreakfastTime(), todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getMobileNumber(), todayMeals.getTodayMeal().get(i).getUserInfo().getMobileNumber(), todayMeals.getTodayMeal().get(i).getUserId(), todayMeals.getTodayMeal().get(i).getPaymentValue(), todayMeals.getTodayMeal().get(i).getSecurityMoney() + "", todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getFullAddress() + ", " + todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getLandmark(), todayMeals.getTodayMeal().get(i).getSubscriptionId(), todayMeals.getTodayMeal().get(i).getDeliveredMeal() + "", todayMeals.getTodayMeal().get(i).getLeftMeals() + "", todayMeals.getTodayMeal().get(i).getPaymentBy(), todayMeals.getTodayMeal().get(i).getPaymentStatus(), todayMeals.getTodayMeal().get(i).getPlanType(), todayMeals.getTodayMeal().get(i).getPlanPrice(), todayMeals.getTodayMeal().get(i).getAllowedCancelMeal() + ""));
                                            total_meal_count++;
                                            subs_left_meal_count--;

                                        } else {
                                            //just pass status delivered
                                            todayMealsModelList.add(new TodayMealsModel("Dinner", "delivered", todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getFullName(),  todayMeals.getTodayMeal().get(i).getMessInfo().getMessTime().get(0).getBreakfastTime(), todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getMobileNumber(), todayMeals.getTodayMeal().get(i).getUserInfo().getMobileNumber(), todayMeals.getTodayMeal().get(i).getUserId(), todayMeals.getTodayMeal().get(i).getPaymentValue(), todayMeals.getTodayMeal().get(i).getSecurityMoney() + "", todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getFullAddress() + ", " + todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getLandmark(), todayMeals.getTodayMeal().get(i).getSubscriptionId(), todayMeals.getTodayMeal().get(i).getDeliveredMeal() + "", todayMeals.getTodayMeal().get(i).getLeftMeals() + "", todayMeals.getTodayMeal().get(i).getPaymentBy(), todayMeals.getTodayMeal().get(i).getPaymentStatus(), todayMeals.getTodayMeal().get(i).getPlanType(), todayMeals.getTodayMeal().get(i).getPlanPrice(), todayMeals.getTodayMeal().get(i).getAllowedCancelMeal() + ""));
                                            total_meal_count++;
                                            deliver_meal_count++;


                                        }
                                    }
                                }
                            }

                        } else if (Objects.equals(second_meal, "dinner")) {
                            if (!Objects.equals(todayMeals.getTodayMeal().get(i).getDinner(), "cancel")) {
                                if (subs_left_meal_count != 0) {
                                    if (todayMeals.isDinner()) {
                                        if (!Objects.equals(todayMeals.getTodayMeal().get(i).getDinner(), "delivered")) {
                                            todayMealsModelList.add(new TodayMealsModel("Dinner", "upcoming", todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getFullName(),  todayMeals.getTodayMeal().get(i).getMessInfo().getMessTime().get(0).getLunchTime(), todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getMobileNumber(), todayMeals.getTodayMeal().get(i).getUserInfo().getMobileNumber(), todayMeals.getTodayMeal().get(i).getUserId(), todayMeals.getTodayMeal().get(i).getPaymentValue(), todayMeals.getTodayMeal().get(i).getSecurityMoney() + "", todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getFullAddress() + ", " + todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getLandmark(), todayMeals.getTodayMeal().get(i).getSubscriptionId(), todayMeals.getTodayMeal().get(i).getDeliveredMeal() + "", todayMeals.getTodayMeal().get(i).getLeftMeals() + "", todayMeals.getTodayMeal().get(i).getPaymentBy(), todayMeals.getTodayMeal().get(i).getPaymentStatus(), todayMeals.getTodayMeal().get(i).getPlanType(), todayMeals.getTodayMeal().get(i).getPlanPrice(), todayMeals.getTodayMeal().get(i).getAllowedCancelMeal() + ""));
                                            total_meal_count++;
                                            subs_left_meal_count--;

                                        } else {
                                            //just pass status delivered
                                            todayMealsModelList.add(new TodayMealsModel("Dinner", "delivered", todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getFullName(),  todayMeals.getTodayMeal().get(i).getMessInfo().getMessTime().get(0).getLunchTime(), todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getMobileNumber(), todayMeals.getTodayMeal().get(i).getUserInfo().getMobileNumber(), todayMeals.getTodayMeal().get(i).getUserId(), todayMeals.getTodayMeal().get(i).getPaymentValue(), todayMeals.getTodayMeal().get(i).getSecurityMoney() + "", todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getFullAddress() + ", " + todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getLandmark(), todayMeals.getTodayMeal().get(i).getSubscriptionId(), todayMeals.getTodayMeal().get(i).getDeliveredMeal() + "", todayMeals.getTodayMeal().get(i).getLeftMeals() + "", todayMeals.getTodayMeal().get(i).getPaymentBy(), todayMeals.getTodayMeal().get(i).getPaymentStatus(), todayMeals.getTodayMeal().get(i).getPlanType(), todayMeals.getTodayMeal().get(i).getPlanPrice(), todayMeals.getTodayMeal().get(i).getAllowedCancelMeal() + ""));
                                            deliver_meal_count++;
                                            total_meal_count++;



                                        }
                                    }
                                }
                            }


                        } else if (Objects.equals(third_meal, "dinner")) {
                            if (!Objects.equals(todayMeals.getTodayMeal().get(i).getDinner(), "cancel")) {
                                if (subs_left_meal_count != 0) {
                                    if (todayMeals.isDinner()) {


                                        if (!Objects.equals(todayMeals.getTodayMeal().get(i).getDinner(), "delivered")) {
                                            todayMealsModelList.add(new TodayMealsModel("Dinner", "upcoming", todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getFullName(),  todayMeals.getTodayMeal().get(i).getMessInfo().getMessTime().get(0).getDinnerTime(), todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getMobileNumber(), todayMeals.getTodayMeal().get(i).getUserInfo().getMobileNumber(), todayMeals.getTodayMeal().get(i).getUserId(), todayMeals.getTodayMeal().get(i).getPaymentValue(), todayMeals.getTodayMeal().get(i).getSecurityMoney() + "", todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getFullAddress() + ", " + todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getLandmark(), todayMeals.getTodayMeal().get(i).getSubscriptionId(), todayMeals.getTodayMeal().get(i).getDeliveredMeal() + "", todayMeals.getTodayMeal().get(i).getLeftMeals() + "", todayMeals.getTodayMeal().get(i).getPaymentBy(), todayMeals.getTodayMeal().get(i).getPaymentStatus(), todayMeals.getTodayMeal().get(i).getPlanType(), todayMeals.getTodayMeal().get(i).getPlanPrice(), todayMeals.getTodayMeal().get(i).getAllowedCancelMeal() + ""));

                                            total_meal_count++;
                                            subs_left_meal_count--;
                                        } else {
                                            //just pass status delivered
                                            todayMealsModelList.add(new TodayMealsModel("Dinner", "delivered", todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getFullName(),  todayMeals.getTodayMeal().get(i).getMessInfo().getMessTime().get(0).getDinnerTime(), todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getMobileNumber(), todayMeals.getTodayMeal().get(i).getUserInfo().getMobileNumber(), todayMeals.getTodayMeal().get(i).getUserId(), todayMeals.getTodayMeal().get(i).getPaymentValue(), todayMeals.getTodayMeal().get(i).getSecurityMoney() + "", todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getFullAddress() + ", " + todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getLandmark(), todayMeals.getTodayMeal().get(i).getSubscriptionId(), todayMeals.getTodayMeal().get(i).getDeliveredMeal() + "", todayMeals.getTodayMeal().get(i).getLeftMeals() + "", todayMeals.getTodayMeal().get(i).getPaymentBy(), todayMeals.getTodayMeal().get(i).getPaymentStatus(), todayMeals.getTodayMeal().get(i).getPlanType(), todayMeals.getTodayMeal().get(i).getPlanPrice(), todayMeals.getTodayMeal().get(i).getAllowedCancelMeal() + ""));
                                            total_meal_count++;
                                            deliver_meal_count++;


                                        }
                                    }
                                }
                            }

                        }
                    }

                } else {



                    if (is_breakfast_available) {
                        if (Objects.equals(first_meal, "breakfast")) {
                            if (subs_left_meal_count != 0) {
                                if (todayMeals.isBreakfast()) {


                                    todayMealsModelList.add(new TodayMealsModel("Breakfast", "upcoming", todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getFullName(),  todayMeals.getTodayMeal().get(i).getMessInfo().getMessTime().get(0).getBreakfastTime(), todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getMobileNumber(), todayMeals.getTodayMeal().get(i).getUserInfo().getMobileNumber(), todayMeals.getTodayMeal().get(i).getUserId(), todayMeals.getTodayMeal().get(i).getPaymentValue(), todayMeals.getTodayMeal().get(i).getSecurityMoney() + "", todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getFullAddress() + ", " + todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getLandmark(), todayMeals.getTodayMeal().get(i).getSubscriptionId(), todayMeals.getTodayMeal().get(i).getDeliveredMeal() + "", todayMeals.getTodayMeal().get(i).getLeftMeals() + "", todayMeals.getTodayMeal().get(i).getPaymentBy(), todayMeals.getTodayMeal().get(i).getPaymentStatus(), todayMeals.getTodayMeal().get(i).getPlanType(), todayMeals.getTodayMeal().get(i).getPlanPrice(), todayMeals.getTodayMeal().get(i).getAllowedCancelMeal() + ""));
                                    total_meal_count++;
                                    subs_left_meal_count--;
                                }
                            }
                        } else if (Objects.equals(second_meal, "breakfast")) {
                            if (subs_left_meal_count != 0) {
                                if (todayMeals.isBreakfast()) {

                                    todayMealsModelList.add(new TodayMealsModel("Breakfast", "upcoming", todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getFullName(),  todayMeals.getTodayMeal().get(i).getMessInfo().getMessTime().get(0).getLunchTime(), todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getMobileNumber(), todayMeals.getTodayMeal().get(i).getUserInfo().getMobileNumber(), todayMeals.getTodayMeal().get(i).getUserId(), todayMeals.getTodayMeal().get(i).getPaymentValue(), todayMeals.getTodayMeal().get(i).getSecurityMoney() + "", todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getFullAddress() + ", " + todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getLandmark(), todayMeals.getTodayMeal().get(i).getSubscriptionId(), todayMeals.getTodayMeal().get(i).getDeliveredMeal() + "", todayMeals.getTodayMeal().get(i).getLeftMeals() + "", todayMeals.getTodayMeal().get(i).getPaymentBy(), todayMeals.getTodayMeal().get(i).getPaymentStatus(), todayMeals.getTodayMeal().get(i).getPlanType(), todayMeals.getTodayMeal().get(i).getPlanPrice(), todayMeals.getTodayMeal().get(i).getAllowedCancelMeal() + ""));
                                    total_meal_count++;
                                    subs_left_meal_count--;
                                }
                            }

                        } else if (Objects.equals(third_meal, "breakfast")) {
                            //onlu upcoming
                            if (subs_left_meal_count != 0) {
                                if (todayMeals.isBreakfast()) {

                                    todayMealsModelList.add(new TodayMealsModel("Breakfast", "upcoming", todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getFullName(),  todayMeals.getTodayMeal().get(i).getMessInfo().getMessTime().get(0).getDinnerTime(), todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getMobileNumber(), todayMeals.getTodayMeal().get(i).getUserInfo().getMobileNumber(), todayMeals.getTodayMeal().get(i).getUserId(), todayMeals.getTodayMeal().get(i).getPaymentValue(), todayMeals.getTodayMeal().get(i).getSecurityMoney() + "", todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getFullAddress() + ", " + todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getLandmark(), todayMeals.getTodayMeal().get(i).getSubscriptionId(), todayMeals.getTodayMeal().get(i).getDeliveredMeal() + "", todayMeals.getTodayMeal().get(i).getLeftMeals() + "", todayMeals.getTodayMeal().get(i).getPaymentBy(), todayMeals.getTodayMeal().get(i).getPaymentStatus(), todayMeals.getTodayMeal().get(i).getPlanType(), todayMeals.getTodayMeal().get(i).getPlanPrice(), todayMeals.getTodayMeal().get(i).getAllowedCancelMeal() + ""));
                                    total_meal_count++;
                                    subs_left_meal_count--;
                                }
                            }

                        }
                    }

                    if (is_lunch_available) {
                        if (Objects.equals(first_meal, "lunch")) {
                            if (subs_left_meal_count != 0) {
                                if (todayMeals.isLunch()) {

                                    todayMealsModelList.add(new TodayMealsModel("Lunch", "upcoming", todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getFullName(),  todayMeals.getTodayMeal().get(i).getMessInfo().getMessTime().get(0).getBreakfastTime(), todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getMobileNumber(), todayMeals.getTodayMeal().get(i).getUserInfo().getMobileNumber(), todayMeals.getTodayMeal().get(i).getUserId(), todayMeals.getTodayMeal().get(i).getPaymentValue(), todayMeals.getTodayMeal().get(i).getSecurityMoney() + "", todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getFullAddress() + ", " + todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getLandmark(), todayMeals.getTodayMeal().get(i).getSubscriptionId(), todayMeals.getTodayMeal().get(i).getDeliveredMeal() + "", todayMeals.getTodayMeal().get(i).getLeftMeals() + "", todayMeals.getTodayMeal().get(i).getPaymentBy(), todayMeals.getTodayMeal().get(i).getPaymentStatus(), todayMeals.getTodayMeal().get(i).getPlanType(), todayMeals.getTodayMeal().get(i).getPlanPrice(), todayMeals.getTodayMeal().get(i).getAllowedCancelMeal() + ""));
                                    total_meal_count++;
                                    subs_left_meal_count--;
                                }
                            }
                        } else if (Objects.equals(second_meal, "lunch")) {
                            if (subs_left_meal_count != 0) {
                                if (todayMeals.isLunch()) {

                                    todayMealsModelList.add(new TodayMealsModel("Lunch", "upcoming", todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getFullName(),  todayMeals.getTodayMeal().get(i).getMessInfo().getMessTime().get(0).getLunchTime(), todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getMobileNumber(), todayMeals.getTodayMeal().get(i).getUserInfo().getMobileNumber(), todayMeals.getTodayMeal().get(i).getUserId(), todayMeals.getTodayMeal().get(i).getPaymentValue(), todayMeals.getTodayMeal().get(i).getSecurityMoney() + "", todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getFullAddress() + ", " + todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getLandmark(), todayMeals.getTodayMeal().get(i).getSubscriptionId(), todayMeals.getTodayMeal().get(i).getDeliveredMeal() + "", todayMeals.getTodayMeal().get(i).getLeftMeals() + "", todayMeals.getTodayMeal().get(i).getPaymentBy(), todayMeals.getTodayMeal().get(i).getPaymentStatus(), todayMeals.getTodayMeal().get(i).getPlanType(), todayMeals.getTodayMeal().get(i).getPlanPrice(), todayMeals.getTodayMeal().get(i).getAllowedCancelMeal() + ""));
                                    total_meal_count++;
                                    subs_left_meal_count--;
                                }
                            }

                        } else if (Objects.equals(third_meal, "lunch")) {
                            if (subs_left_meal_count != 0) {
                                if (todayMeals.isLunch()) {
                                    //onlu upcoming
                                    todayMealsModelList.add(new TodayMealsModel("Lunch", "upcoming", todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getFullName(),  todayMeals.getTodayMeal().get(i).getMessInfo().getMessTime().get(0).getDinnerTime(), todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getMobileNumber(), todayMeals.getTodayMeal().get(i).getUserInfo().getMobileNumber(), todayMeals.getTodayMeal().get(i).getUserId(), todayMeals.getTodayMeal().get(i).getPaymentValue(), todayMeals.getTodayMeal().get(i).getSecurityMoney() + "", todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getFullAddress() + ", " + todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getLandmark(), todayMeals.getTodayMeal().get(i).getSubscriptionId(), todayMeals.getTodayMeal().get(i).getDeliveredMeal() + "", todayMeals.getTodayMeal().get(i).getLeftMeals() + "", todayMeals.getTodayMeal().get(i).getPaymentBy(), todayMeals.getTodayMeal().get(i).getPaymentStatus(), todayMeals.getTodayMeal().get(i).getPlanType(), todayMeals.getTodayMeal().get(i).getPlanPrice(), todayMeals.getTodayMeal().get(i).getAllowedCancelMeal() + ""));
                                    total_meal_count++;
                                    subs_left_meal_count--;
                                }
                            }
                        }
                    }

                    if (is_dinner_available) {
                        if (Objects.equals(first_meal, "dinner")) {
                            if (subs_left_meal_count != 0) {
                                if (todayMeals.isDinner()) {

                                    todayMealsModelList.add(new TodayMealsModel("Dinner", "upcoming", todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getFullName(),  todayMeals.getTodayMeal().get(i).getMessInfo().getMessTime().get(0).getBreakfastTime(), todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getMobileNumber(), todayMeals.getTodayMeal().get(i).getUserInfo().getMobileNumber(), todayMeals.getTodayMeal().get(i).getUserId(), todayMeals.getTodayMeal().get(i).getPaymentValue(), todayMeals.getTodayMeal().get(i).getSecurityMoney() + "", todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getFullAddress() + ", " + todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getLandmark(), todayMeals.getTodayMeal().get(i).getSubscriptionId(), todayMeals.getTodayMeal().get(i).getDeliveredMeal() + "", todayMeals.getTodayMeal().get(i).getLeftMeals() + "", todayMeals.getTodayMeal().get(i).getPaymentBy(), todayMeals.getTodayMeal().get(i).getPaymentStatus(), todayMeals.getTodayMeal().get(i).getPlanType(), todayMeals.getTodayMeal().get(i).getPlanPrice(), todayMeals.getTodayMeal().get(i).getAllowedCancelMeal() + ""));
                                    total_meal_count++;
                                    subs_left_meal_count--;
                                }
                            }
                        } else if (Objects.equals(second_meal, "dinner")) {
                            if (subs_left_meal_count != 0) {
                                if (todayMeals.isDinner()) {

                                    todayMealsModelList.add(new TodayMealsModel("Dinner", "upcoming", todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getFullName(),  todayMeals.getTodayMeal().get(i).getMessInfo().getMessTime().get(0).getLunchTime(), todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getMobileNumber(), todayMeals.getTodayMeal().get(i).getUserInfo().getMobileNumber(), todayMeals.getTodayMeal().get(i).getUserId(), todayMeals.getTodayMeal().get(i).getPaymentValue(), todayMeals.getTodayMeal().get(i).getSecurityMoney() + "", todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getFullAddress() + ", " + todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getLandmark(), todayMeals.getTodayMeal().get(i).getSubscriptionId(), todayMeals.getTodayMeal().get(i).getDeliveredMeal() + "", todayMeals.getTodayMeal().get(i).getLeftMeals() + "", todayMeals.getTodayMeal().get(i).getPaymentBy(), todayMeals.getTodayMeal().get(i).getPaymentStatus(), todayMeals.getTodayMeal().get(i).getPlanType(), todayMeals.getTodayMeal().get(i).getPlanPrice(), todayMeals.getTodayMeal().get(i).getAllowedCancelMeal() + ""));
                                    total_meal_count++;
                                    subs_left_meal_count--;
                                }
                            }

                        } else if (Objects.equals(third_meal, "dinner")) {
                            if (subs_left_meal_count != 0) {
                                if (todayMeals.isDinner()) {
                                    //onlu upcoming
                                    todayMealsModelList.add(new TodayMealsModel("Dinner", "upcoming", todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getFullName(),  todayMeals.getTodayMeal().get(i).getMessInfo().getMessTime().get(0).getDinnerTime(), todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getMobileNumber(), todayMeals.getTodayMeal().get(i).getUserInfo().getMobileNumber(), todayMeals.getTodayMeal().get(i).getUserId(), todayMeals.getTodayMeal().get(i).getPaymentValue(), todayMeals.getTodayMeal().get(i).getSecurityMoney() + "", todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getFullAddress() + ", " + todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getLandmark(), todayMeals.getTodayMeal().get(i).getSubscriptionId(), todayMeals.getTodayMeal().get(i).getDeliveredMeal() + "", todayMeals.getTodayMeal().get(i).getLeftMeals() + "", todayMeals.getTodayMeal().get(i).getPaymentBy(), todayMeals.getTodayMeal().get(i).getPaymentStatus(), todayMeals.getTodayMeal().get(i).getPlanType(), todayMeals.getTodayMeal().get(i).getPlanPrice(), todayMeals.getTodayMeal().get(i).getAllowedCancelMeal() + ""));
                                    total_meal_count++;
                                    subs_left_meal_count--;
                                }
                            }

                        }
                    }


                }
            } else {


                if (is_breakfast_available) {
                    if (Objects.equals(first_meal, "breakfast")) {
                        if (subs_left_meal_count != 0) {
                            if (todayMeals.isBreakfast()) {

                                todayMealsModelList.add(new TodayMealsModel("Breakfast", "upcoming", todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getFullName(),  todayMeals.getTodayMeal().get(i).getMessInfo().getMessTime().get(0).getBreakfastTime(), todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getMobileNumber(), todayMeals.getTodayMeal().get(i).getUserInfo().getMobileNumber(), todayMeals.getTodayMeal().get(i).getUserId(), todayMeals.getTodayMeal().get(i).getPaymentValue(), todayMeals.getTodayMeal().get(i).getSecurityMoney() + "", todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getFullAddress() + ", " + todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getLandmark(), todayMeals.getTodayMeal().get(i).getSubscriptionId(), todayMeals.getTodayMeal().get(i).getDeliveredMeal() + "", todayMeals.getTodayMeal().get(i).getLeftMeals() + "", todayMeals.getTodayMeal().get(i).getPaymentBy(), todayMeals.getTodayMeal().get(i).getPaymentStatus(), todayMeals.getTodayMeal().get(i).getPlanType(), todayMeals.getTodayMeal().get(i).getPlanPrice(), todayMeals.getTodayMeal().get(i).getAllowedCancelMeal() + ""));
                                total_meal_count++;
                                subs_left_meal_count--;
                            }}
                    } else if (Objects.equals(second_meal, "breakfast")) {
                        if (subs_left_meal_count != 0) {
                            if (todayMeals.isBreakfast()) {

                                todayMealsModelList.add(new TodayMealsModel("Breakfast", "upcoming", todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getFullName(),  todayMeals.getTodayMeal().get(i).getMessInfo().getMessTime().get(0).getLunchTime(), todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getMobileNumber(), todayMeals.getTodayMeal().get(i).getUserInfo().getMobileNumber(), todayMeals.getTodayMeal().get(i).getUserId(), todayMeals.getTodayMeal().get(i).getPaymentValue(), todayMeals.getTodayMeal().get(i).getSecurityMoney() + "", todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getFullAddress() + ", " + todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getLandmark(), todayMeals.getTodayMeal().get(i).getSubscriptionId(), todayMeals.getTodayMeal().get(i).getDeliveredMeal() + "", todayMeals.getTodayMeal().get(i).getLeftMeals() + "", todayMeals.getTodayMeal().get(i).getPaymentBy(), todayMeals.getTodayMeal().get(i).getPaymentStatus(), todayMeals.getTodayMeal().get(i).getPlanType(), todayMeals.getTodayMeal().get(i).getPlanPrice(), todayMeals.getTodayMeal().get(i).getAllowedCancelMeal() + ""));
                                total_meal_count++;
                                subs_left_meal_count--;
                            }}

                    } else if (Objects.equals(third_meal, "breakfast")) {
                        //onlu upcoming
                        if (subs_left_meal_count != 0) {
                            if (todayMeals.isBreakfast()) {
                                todayMealsModelList.add(new TodayMealsModel("Breakfast", "upcoming", todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getFullName(),  todayMeals.getTodayMeal().get(i).getMessInfo().getMessTime().get(0).getDinnerTime(), todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getMobileNumber(), todayMeals.getTodayMeal().get(i).getUserInfo().getMobileNumber(), todayMeals.getTodayMeal().get(i).getUserId(), todayMeals.getTodayMeal().get(i).getPaymentValue(), todayMeals.getTodayMeal().get(i).getSecurityMoney() + "", todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getFullAddress() + ", " + todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getLandmark(), todayMeals.getTodayMeal().get(i).getSubscriptionId(), todayMeals.getTodayMeal().get(i).getDeliveredMeal() + "", todayMeals.getTodayMeal().get(i).getLeftMeals() + "", todayMeals.getTodayMeal().get(i).getPaymentBy(), todayMeals.getTodayMeal().get(i).getPaymentStatus(), todayMeals.getTodayMeal().get(i).getPlanType(), todayMeals.getTodayMeal().get(i).getPlanPrice(), todayMeals.getTodayMeal().get(i).getAllowedCancelMeal() + ""));
                                total_meal_count++;
                                subs_left_meal_count--;
                            }}

                    }
                }

                if (is_lunch_available) {
                    if (Objects.equals(first_meal, "lunch")) {
                        if (subs_left_meal_count != 0) {
                            if (todayMeals.isLunch()) {

                                todayMealsModelList.add(new TodayMealsModel("Lunch", "upcoming", todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getFullName(),  todayMeals.getTodayMeal().get(i).getMessInfo().getMessTime().get(0).getBreakfastTime(), todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getMobileNumber(), todayMeals.getTodayMeal().get(i).getUserInfo().getMobileNumber(), todayMeals.getTodayMeal().get(i).getUserId(), todayMeals.getTodayMeal().get(i).getPaymentValue(), todayMeals.getTodayMeal().get(i).getSecurityMoney() + "", todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getFullAddress() + ", " + todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getLandmark(), todayMeals.getTodayMeal().get(i).getSubscriptionId(), todayMeals.getTodayMeal().get(i).getDeliveredMeal() + "", todayMeals.getTodayMeal().get(i).getLeftMeals() + "", todayMeals.getTodayMeal().get(i).getPaymentBy(), todayMeals.getTodayMeal().get(i).getPaymentStatus(), todayMeals.getTodayMeal().get(i).getPlanType(), todayMeals.getTodayMeal().get(i).getPlanPrice(), todayMeals.getTodayMeal().get(i).getAllowedCancelMeal() + ""));
                                total_meal_count++;
                                subs_left_meal_count--;
                            }}
                    } else if (Objects.equals(second_meal, "lunch")) {
                        if (subs_left_meal_count != 0) {
                            if (todayMeals.isLunch()) {

                                todayMealsModelList.add(new TodayMealsModel("Lunch", "upcoming", todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getFullName(),  todayMeals.getTodayMeal().get(i).getMessInfo().getMessTime().get(0).getLunchTime(), todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getMobileNumber(), todayMeals.getTodayMeal().get(i).getUserInfo().getMobileNumber(), todayMeals.getTodayMeal().get(i).getUserId(), todayMeals.getTodayMeal().get(i).getPaymentValue(), todayMeals.getTodayMeal().get(i).getSecurityMoney() + "", todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getFullAddress() + ", " + todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getLandmark(), todayMeals.getTodayMeal().get(i).getSubscriptionId(), todayMeals.getTodayMeal().get(i).getDeliveredMeal() + "", todayMeals.getTodayMeal().get(i).getLeftMeals() + "", todayMeals.getTodayMeal().get(i).getPaymentBy(), todayMeals.getTodayMeal().get(i).getPaymentStatus(), todayMeals.getTodayMeal().get(i).getPlanType(), todayMeals.getTodayMeal().get(i).getPlanPrice(), todayMeals.getTodayMeal().get(i).getAllowedCancelMeal() + ""));
                                total_meal_count++;
                                subs_left_meal_count--;
                            }}

                    } else if (Objects.equals(third_meal, "lunch")) {
                        //onlu upcoming
                        if (subs_left_meal_count != 0) {
                            if (todayMeals.isLunch()) {
                                todayMealsModelList.add(new TodayMealsModel("Lunch", "upcoming", todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getFullName(),  todayMeals.getTodayMeal().get(i).getMessInfo().getMessTime().get(0).getDinnerTime(), todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getMobileNumber(), todayMeals.getTodayMeal().get(i).getUserInfo().getMobileNumber(), todayMeals.getTodayMeal().get(i).getUserId(), todayMeals.getTodayMeal().get(i).getPaymentValue(), todayMeals.getTodayMeal().get(i).getSecurityMoney() + "", todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getFullAddress() + ", " + todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getLandmark(), todayMeals.getTodayMeal().get(i).getSubscriptionId(), todayMeals.getTodayMeal().get(i).getDeliveredMeal() + "", todayMeals.getTodayMeal().get(i).getLeftMeals() + "", todayMeals.getTodayMeal().get(i).getPaymentBy(), todayMeals.getTodayMeal().get(i).getPaymentStatus(), todayMeals.getTodayMeal().get(i).getPlanType(), todayMeals.getTodayMeal().get(i).getPlanPrice(), todayMeals.getTodayMeal().get(i).getAllowedCancelMeal() + ""));
                                total_meal_count++;
                                subs_left_meal_count--;
                            }}

                    }
                }

                if (is_dinner_available) {
                    if (Objects.equals(first_meal, "dinner")) {
                        if (subs_left_meal_count != 0) {
                            if (todayMeals.isDinner()) {

                                todayMealsModelList.add(new TodayMealsModel("Dinner", "upcoming", todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getFullName(),  todayMeals.getTodayMeal().get(i).getMessInfo().getMessTime().get(0).getBreakfastTime(), todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getMobileNumber(), todayMeals.getTodayMeal().get(i).getUserInfo().getMobileNumber(), todayMeals.getTodayMeal().get(i).getUserId(), todayMeals.getTodayMeal().get(i).getPaymentValue(), todayMeals.getTodayMeal().get(i).getSecurityMoney() + "", todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getFullAddress() + ", " + todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getLandmark(), todayMeals.getTodayMeal().get(i).getSubscriptionId(), todayMeals.getTodayMeal().get(i).getDeliveredMeal() + "", todayMeals.getTodayMeal().get(i).getLeftMeals() + "", todayMeals.getTodayMeal().get(i).getPaymentBy(), todayMeals.getTodayMeal().get(i).getPaymentStatus(), todayMeals.getTodayMeal().get(i).getPlanType(), todayMeals.getTodayMeal().get(i).getPlanPrice(), todayMeals.getTodayMeal().get(i).getAllowedCancelMeal() + ""));
                                total_meal_count++;
                                subs_left_meal_count--;
                            }}
                    } else if (Objects.equals(second_meal, "dinner")) {
                        if (subs_left_meal_count != 0) {
                            if (todayMeals.isDinner()) {

                                todayMealsModelList.add(new TodayMealsModel("Dinner", "upcoming", todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getFullName(),  todayMeals.getTodayMeal().get(i).getMessInfo().getMessTime().get(0).getLunchTime(), todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getMobileNumber(), todayMeals.getTodayMeal().get(i).getUserInfo().getMobileNumber(), todayMeals.getTodayMeal().get(i).getUserId(), todayMeals.getTodayMeal().get(i).getPaymentValue(), todayMeals.getTodayMeal().get(i).getSecurityMoney() + "", todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getFullAddress() + ", " + todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getLandmark(), todayMeals.getTodayMeal().get(i).getSubscriptionId(), todayMeals.getTodayMeal().get(i).getDeliveredMeal() + "", todayMeals.getTodayMeal().get(i).getLeftMeals() + "", todayMeals.getTodayMeal().get(i).getPaymentBy(), todayMeals.getTodayMeal().get(i).getPaymentStatus(), todayMeals.getTodayMeal().get(i).getPlanType(), todayMeals.getTodayMeal().get(i).getPlanPrice(), todayMeals.getTodayMeal().get(i).getAllowedCancelMeal() + ""));
                                total_meal_count++;
                                subs_left_meal_count--;
                            }}

                    } else if (Objects.equals(third_meal, "dinner")) {
                        if (subs_left_meal_count != 0) {
                            if (todayMeals.isDinner()) {
                                //onlu upcoming
                                todayMealsModelList.add(new TodayMealsModel("Dinner", "upcoming", todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getFullName(),  todayMeals.getTodayMeal().get(i).getMessInfo().getMessTime().get(0).getDinnerTime(), todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getMobileNumber(), todayMeals.getTodayMeal().get(i).getUserInfo().getMobileNumber(), todayMeals.getTodayMeal().get(i).getUserId(), todayMeals.getTodayMeal().get(i).getPaymentValue(), todayMeals.getTodayMeal().get(i).getSecurityMoney() + "", todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getFullAddress() + ", " + todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getLandmark(), todayMeals.getTodayMeal().get(i).getSubscriptionId(), todayMeals.getTodayMeal().get(i).getDeliveredMeal() + "", todayMeals.getTodayMeal().get(i).getLeftMeals() + "", todayMeals.getTodayMeal().get(i).getPaymentBy(), todayMeals.getTodayMeal().get(i).getPaymentStatus(), todayMeals.getTodayMeal().get(i).getPlanType(), todayMeals.getTodayMeal().get(i).getPlanPrice(), todayMeals.getTodayMeal().get(i).getAllowedCancelMeal() + ""));
                                total_meal_count++;
                                subs_left_meal_count--;
                            }}

                    }
                }


            }

        }
        todayMealsBinding.tTotalMealsValue.setText(total_meal_count + "");
        todayMealsBinding.tLeftMealsValue.setText((total_meal_count - deliver_meal_count) + "");
        todayMealsBinding.tDeliveredMeal.setText(deliver_meal_count + "");
        if (todayMealsModelList.size() == 0) {
            if (!todayMeals.isBreakfast()&& !todayMeals.isLunch()&& !todayMeals.isDinner()){

                todayMealsBinding.recViewTodayMeal.setVisibility(View.GONE);
                todayMealsBinding.lnNoUpcomingMeal.setVisibility(View.VISIBLE);
                todayMealsBinding.tNoMeals.setText("You do not serves today");
                todayMealsBinding.lnFilterLayout.setVisibility(View.GONE);
                todayMealsBinding.imgSearch.setVisibility(View.GONE);

            }else {
                todayMealsBinding.recViewTodayMeal.setVisibility(View.GONE);
                todayMealsBinding.lnNoUpcomingMeal.setVisibility(View.VISIBLE);
                todayMealsBinding.lnFilterLayout.setVisibility(View.GONE);
                todayMealsBinding.imgSearch.setVisibility(View.GONE);


            }

        } else {

            todayMealsBinding.recViewTodayMeal.setVisibility(View.VISIBLE);
            todayMealsBinding.lnNoUpcomingMeal.setVisibility(View.GONE);
            todayMealsBinding.lnFilterLayout.setVisibility(View.VISIBLE);
            todayMealsBinding.imgSearch.setVisibility(View.VISIBLE);


        }
        enableChip(todayMeals);
        alertDialog.dismiss();

        return todayMealsModelList;


    }

    private void initView(View view) {
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        isConnected = Reusable.CheckInternet(activity);
        alertDialog = Reusable.alertDialog(activity);
        navController= Navigation.findNavController(view);
        todayMealViewModel = new ViewModelProvider((ViewModelStoreOwner) activity).get(TodayMealViewModel.class);
        sharedPreferences = EncryptedSharedPreferencesInstance.getSharedPreferences(Constant.MY_GLOBAL_PREFERENCES, activity);


    }

    private void enableChip(TodayMeals todayMeals){
        if (todayMeals.isBreakfast()){
            todayMealsBinding.chipBreakfast.setEnabled(true);

        }
        if (todayMeals.isLunch()){
            todayMealsBinding.chipLunch.setEnabled(true);

        }

        if (todayMeals.isDinner()){
            todayMealsBinding.chipDinner.setEnabled(true);

        }

    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = context instanceof Activity ? (Activity) context : null;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (snackbar != null) {
            snackbar.dismiss();

        }
    }
}