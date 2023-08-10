package seller.in.mesway.fragments.registration;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import seller.in.mesway.R;
import seller.in.mesway.activity.App;
import seller.in.mesway.client.ApiClient;
import seller.in.mesway.client.ApiInterface;
import seller.in.mesway.databinding.FragmentSecondStepBinding;
import seller.in.mesway.response.DetailResponse;
import seller.in.mesway.response.MeswayServingDayResponse;
import seller.in.mesway.response.SecondStepResponse;
import seller.in.mesway.reusable.Constant;
import seller.in.mesway.reusable.EncryptedSharedPreferencesInstance;

public class SecondStepFragment extends Fragment {
    private FragmentSecondStepBinding secondStepBinding;
    private NavController navController;
    private SharedPreferences sharedPreferences;
    private ApiInterface apiInterface;
    private Snackbar snackbar;
    private List<MeswayServingDayResponse> meswayServingDayResponseList;
    private String main_serves = "b35f6f14-e92c-4581-9c90-a593ff6b3a96", mon_serves = "b35f6f14-e92c-4581-9c90-a593ff6b3a96", tue_serves = "b35f6f14-e92c-4581-9c90-a593ff6b3a96", wed_serves = "b35f6f14-e92c-4581-9c90-a593ff6b3a96", thu_serves = "b35f6f14-e92c-4581-9c90-a593ff6b3a96", fri_serves = "b35f6f14-e92c-4581-9c90-a593ff6b3a96", sat_serves = "b35f6f14-e92c-4581-9c90-a593ff6b3a96", sun_serves = "b35f6f14-e92c-4581-9c90-a593ff6b3a96";


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        secondStepBinding = FragmentSecondStepBinding.inflate(inflater, container, false);
        return secondStepBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView(view);
        getAllTypeServingMeal(view);
        checkOperations();
        dayWiseCheckOperations();
        clickHandle(view);


    }

    private void getAllTypeServingMeal(View view) {
        secondStepBinding.linearLoader.setVisibility(View.VISIBLE);
        Call<List<MeswayServingDayResponse>> listCall = apiInterface.get_all_mesway_serving_meal(App.getAPIKey());
        listCall.enqueue(new Callback<List<MeswayServingDayResponse>>() {
            @Override
            public void onResponse(@NonNull Call<List<MeswayServingDayResponse>> call, @NonNull Response<List<MeswayServingDayResponse>> response) {
                Gson gson = new GsonBuilder().create();
                if (response.code() == 200) {
                    meswayServingDayResponseList = response.body();
                    secondStepBinding.linearLoader.setVisibility(View.GONE);

                } else {

                    try {
                        DetailResponse detailResponse = gson.fromJson(response.errorBody().charStream(), DetailResponse.class);
                        snackbar = Snackbar.make(view, "" + detailResponse.getDetail(), Snackbar.LENGTH_SHORT);
                        snackbar.setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.red));

                        snackbar.show();


                    } catch (Exception e) {
                        snackbar = Snackbar.make(view, response.code() + ":Something went error ", Snackbar.LENGTH_SHORT);
                        snackbar.setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.red));

                        snackbar.show();


                    }

                }
            }

            @Override
            public void onFailure(@NonNull Call<List<MeswayServingDayResponse>> call, @NonNull Throwable t) {
                snackbar = Snackbar.make(view, "Failure: Check your internet connection/Something went error", Snackbar.LENGTH_SHORT);
                snackbar.setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.red));

                snackbar.show();

            }
        });
    }

    private void dayWiseCheckOperations() {
        monServesCheckOperation();
        tueServesCheckOperation();
        wedServesCheckOperation();
        thuServesCheckOperation();
        friServesCheckOperation();
        satServesCheckOperation();
        sunServesCheckOperation();

    }

    private void monServesCheckOperation() {
        dayCheckBoxUniversalOperation(secondStepBinding.monBreakfastCheckbox, "mon", secondStepBinding.monBreakfastCheckbox, secondStepBinding.monLunchCheckbox, secondStepBinding.monDinnerCheckbox);
        dayCheckBoxUniversalOperation(secondStepBinding.monLunchCheckbox, "mon", secondStepBinding.monBreakfastCheckbox, secondStepBinding.monLunchCheckbox, secondStepBinding.monDinnerCheckbox);
        dayCheckBoxUniversalOperation(secondStepBinding.monDinnerCheckbox, "mon", secondStepBinding.monBreakfastCheckbox, secondStepBinding.monLunchCheckbox, secondStepBinding.monDinnerCheckbox);


    }

    private void tueServesCheckOperation() {
        dayCheckBoxUniversalOperation(secondStepBinding.tueBreakfastCheckbox, "tue", secondStepBinding.tueBreakfastCheckbox, secondStepBinding.tueLunchCheckbox, secondStepBinding.tueDinnerCheckbox);
        dayCheckBoxUniversalOperation(secondStepBinding.tueLunchCheckbox, "tue", secondStepBinding.tueBreakfastCheckbox, secondStepBinding.tueLunchCheckbox, secondStepBinding.tueDinnerCheckbox);
        dayCheckBoxUniversalOperation(secondStepBinding.tueDinnerCheckbox, "tue", secondStepBinding.tueBreakfastCheckbox, secondStepBinding.tueLunchCheckbox, secondStepBinding.tueDinnerCheckbox);

    }

    private void wedServesCheckOperation() {
        dayCheckBoxUniversalOperation(secondStepBinding.wedBreakfastCheckbox, "wed", secondStepBinding.wedBreakfastCheckbox, secondStepBinding.wedLunchCheckbox, secondStepBinding.wedDinnerCheckbox);
        dayCheckBoxUniversalOperation(secondStepBinding.wedLunchCheckbox, "wed", secondStepBinding.wedBreakfastCheckbox, secondStepBinding.wedLunchCheckbox, secondStepBinding.wedDinnerCheckbox);
        dayCheckBoxUniversalOperation(secondStepBinding.wedDinnerCheckbox, "wed", secondStepBinding.wedBreakfastCheckbox, secondStepBinding.wedLunchCheckbox, secondStepBinding.wedDinnerCheckbox);

    }

    private void thuServesCheckOperation() {
        dayCheckBoxUniversalOperation(secondStepBinding.thuBreakfastCheckbox, "thu", secondStepBinding.thuBreakfastCheckbox, secondStepBinding.thuLunchCheckbox, secondStepBinding.thuDinnerCheckbox);
        dayCheckBoxUniversalOperation(secondStepBinding.thuLunchCheckbox, "thu", secondStepBinding.thuBreakfastCheckbox, secondStepBinding.thuLunchCheckbox, secondStepBinding.thuDinnerCheckbox);
        dayCheckBoxUniversalOperation(secondStepBinding.thuDinnerCheckbox, "thu", secondStepBinding.thuBreakfastCheckbox, secondStepBinding.thuLunchCheckbox, secondStepBinding.thuDinnerCheckbox);

    }

    private void friServesCheckOperation() {
        dayCheckBoxUniversalOperation(secondStepBinding.friBreakfastCheckbox, "fri", secondStepBinding.friBreakfastCheckbox, secondStepBinding.friLunchCheckbox, secondStepBinding.friDinnerCheckbox);
        dayCheckBoxUniversalOperation(secondStepBinding.friLunchCheckbox, "fri", secondStepBinding.friBreakfastCheckbox, secondStepBinding.friLunchCheckbox, secondStepBinding.friDinnerCheckbox);
        dayCheckBoxUniversalOperation(secondStepBinding.friDinnerCheckbox, "fri", secondStepBinding.friBreakfastCheckbox, secondStepBinding.friLunchCheckbox, secondStepBinding.friDinnerCheckbox);

    }

    private void satServesCheckOperation() {
        dayCheckBoxUniversalOperation(secondStepBinding.satBreakfastCheckbox, "sat", secondStepBinding.satBreakfastCheckbox, secondStepBinding.satLunchCheckbox, secondStepBinding.satDinnerCheckbox);
        dayCheckBoxUniversalOperation(secondStepBinding.satLunchCheckbox, "sat", secondStepBinding.satBreakfastCheckbox, secondStepBinding.satLunchCheckbox, secondStepBinding.satDinnerCheckbox);
        dayCheckBoxUniversalOperation(secondStepBinding.satDinnerCheckbox, "sat", secondStepBinding.satBreakfastCheckbox, secondStepBinding.satLunchCheckbox, secondStepBinding.satDinnerCheckbox);

    }

    private void sunServesCheckOperation() {
        dayCheckBoxUniversalOperation(secondStepBinding.sunBreakfastCheckbox, "sun", secondStepBinding.sunBreakfastCheckbox, secondStepBinding.sunLunchCheckbox, secondStepBinding.sunDinnerCheckbox);
        dayCheckBoxUniversalOperation(secondStepBinding.sunLunchCheckbox, "sun", secondStepBinding.sunBreakfastCheckbox, secondStepBinding.sunLunchCheckbox, secondStepBinding.sunDinnerCheckbox);
        dayCheckBoxUniversalOperation(secondStepBinding.sunDinnerCheckbox, "sun", secondStepBinding.sunBreakfastCheckbox, secondStepBinding.sunLunchCheckbox, secondStepBinding.sunDinnerCheckbox);

    }

    private void dayCheckBoxUniversalOperation(CheckBox which_checkbox, String day, CheckBox first, CheckBox second, CheckBox third) {
        which_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                boolean breakfast = false, lunch = false, dinner = false;
                if (which_checkbox.getId() == first.getId()) {
                    breakfast = b;

                } else {
                    if (first.isChecked()) {
                        breakfast = true;

                    }
                }
                if (which_checkbox.getId() == second.getId()) {
                    lunch = b;
                } else {
                    if (second.isChecked()) {
                        lunch = true;

                    }
                }
                if (which_checkbox.getId() == third.getId()) {
                    dinner = b;
                } else {
                    if (third.isChecked()) {
                        dinner = true;

                    }
                }
                callSubsIdFinder(day, breakfast, lunch, dinner);

            }
        });

    }


    private void callSubsIdFinder(String day, boolean breakfast, boolean lunch, boolean dinner) {
    //    Toast.makeText(requireActivity(), ""+breakfast+" "+lunch+" "+dinner, Toast.LENGTH_SHORT).show();
        int meal_count = 0;
        if (breakfast) {
            meal_count++;

        }
        if (lunch) {
            meal_count++;

        }
        if (dinner) {
            meal_count++;

        }

        for (int i = 0; i < meswayServingDayResponseList.size(); i++) {
            if (meswayServingDayResponseList.get(i).getMeals_number() == meal_count) {
                if (meswayServingDayResponseList.get(i).isBreakfast() == breakfast && meswayServingDayResponseList.get(i).isLunch() == lunch && meswayServingDayResponseList.get(i).isDinner() == dinner) {
                    if (Objects.equals(day, "main")) {

                        if (secondStepBinding.checkboxAllDaySameAsMessServes.isChecked()) {
                            main_serves = meswayServingDayResponseList.get(i).getServing_meals_id();
                            assignMessServesToAll();
                        } else {
                            main_serves = meswayServingDayResponseList.get(i).getServing_meals_id();

                        }
                    }

                    if (!secondStepBinding.checkboxAllDaySameAsMessServes.isChecked()) {
                        switch (day) {
                            case "mon":
                                mon_serves = meswayServingDayResponseList.get(i).getServing_meals_id();

                                break;
                            case "tue":
                                tue_serves = meswayServingDayResponseList.get(i).getServing_meals_id();

                                break;
                            case "wed":
                                wed_serves = meswayServingDayResponseList.get(i).getServing_meals_id();

                                break;
                            case "thu":
                                thu_serves = meswayServingDayResponseList.get(i).getServing_meals_id();

                                break;
                            case "fri":
                                fri_serves = meswayServingDayResponseList.get(i).getServing_meals_id();

                                break;
                            case "sat":
                                sat_serves = meswayServingDayResponseList.get(i).getServing_meals_id();

                                break;
                            case "sun":
                                sun_serves = meswayServingDayResponseList.get(i).getServing_meals_id();
                                break;
                        }
                    }
                }

            }

        }


    }


    private void checkOperations() {
        secondStepBinding.breakfastCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (secondStepBinding.checkboxAllDaySameAsMessServes.isChecked()) {
                    if (b) {

                        enableAllBreakfast();
                    } else {
                        disableAllBreakFast();

                    }

                }

                boolean breakfast = false, lunch = false, dinner = false;
                breakfast = b;
                if (secondStepBinding.lunchCheckbox.isChecked()) {
                    lunch = true;
                }
                if (secondStepBinding.dinnerCheckbox.isChecked()) {
                    dinner = true;
                }
                //if checked allsame then assign all the
                callSubsIdFinder("main", breakfast, lunch, dinner);

            }


        });

        secondStepBinding.lunchCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (secondStepBinding.checkboxAllDaySameAsMessServes.isChecked()) {

                    if (b) {
                        enableAllLunch();

                    } else {
                        disableAllLunch();
                    }
                }
                boolean breakfast = false, lunch = false, dinner = false;
                if (secondStepBinding.breakfastCheckbox.isChecked()) {
                    breakfast = true;
                }
                lunch = b;
                if (secondStepBinding.dinnerCheckbox.isChecked()) {
                    dinner = true;
                }
                //if checked allsame then assign all the
                callSubsIdFinder("main", breakfast, lunch, dinner);


            }
        });
        secondStepBinding.dinnerCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (secondStepBinding.checkboxAllDaySameAsMessServes.isChecked()) {

                    if (b) {
                        enableAllDinner();

                    } else {

                        disableAllDinner();
                    }
                }
                boolean breakfast = false, lunch = false, dinner = false;
                if (secondStepBinding.breakfastCheckbox.isChecked()) {
                    breakfast = true;
                }
                if (secondStepBinding.lunchCheckbox.isChecked()) {
                    lunch = true;
                }
                dinner = b;
                //if checked all same then assign all the
                callSubsIdFinder("main", breakfast, lunch, dinner);


            }
        });

        secondStepBinding.checkboxAllDaySameAsMessServes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {

                    visibleAllDayLinerLayout();
                    assignMessServesToAll();

                } else {

                    goneAllDayLinerLayout();
                }


                if (secondStepBinding.dinnerCheckbox.isChecked()) {
                    enableAllDinner();
                } else {
                    disableAllDinner();
                }
                if (secondStepBinding.lunchCheckbox.isChecked()) {
                    enableAllLunch();
                } else {
                    disableAllLunch();
                }

                if (secondStepBinding.breakfastCheckbox.isChecked()) {
                    enableAllBreakfast();
                } else {
                    disableAllBreakFast();
                }


            }
        });

    }

    private void assignMessServesToAll() {
        mon_serves = main_serves;
        tue_serves = main_serves;
        wed_serves = main_serves;
        thu_serves = main_serves;
        fri_serves = main_serves;
        sat_serves = main_serves;
        sun_serves = main_serves;
    }

    private void clickHandle(View view) {
        secondStepBinding.btnNext2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Objects.equals(main_serves, "b35f6f14-e92c-4581-9c90-a593ff6b3a96") && main_serves != null) {
                    if (!(Objects.equals(mon_serves, "b35f6f14-e92c-4581-9c90-a593ff6b3a96") && Objects.equals(tue_serves, "b35f6f14-e92c-4581-9c90-a593ff6b3a96") && Objects.equals(wed_serves, "b35f6f14-e92c-4581-9c90-a593ff6b3a96") && Objects.equals(thu_serves, "b35f6f14-e92c-4581-9c90-a593ff6b3a96") && Objects.equals(fri_serves, "b35f6f14-e92c-4581-9c90-a593ff6b3a96") && Objects.equals(sat_serves, "b35f6f14-e92c-4581-9c90-a593ff6b3a96") && Objects.equals(sun_serves, "b35f6f14-e92c-4581-9c90-a593ff6b3a96"))) {

                        sendRequest(view);

                    } else {
                        snackbar = Snackbar.make(view, "Plz check your Day severing meal (You have to served atleast for one day)", Snackbar.LENGTH_SHORT);
                        snackbar.setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.red));

                        snackbar.show();

                    }


                } else {

                    snackbar = Snackbar.make(view, "Plz check your Mess severing meal (You have to choose mess serving meal)", Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.red));

                    snackbar.show();

                }

                //    navController.navigate(SecondStepFragmentDirections.actionGlobalThirdStepFragment());
            }
        });
    }

    private void sendRequest(View view) {
        hideKeyboard();
        secondStepBinding.linearLoader.setVisibility(View.VISIBLE);
        Call<SecondStepResponse> secondStepResponseCall= apiInterface.second_step(Constant.TOKEN_TYPE_VALUE+sharedPreferences.getString(Constant.ACCESS_TOKEN,null),UUID.fromString(sharedPreferences.getString(Constant.MESS_ID,null)), UUID.fromString(main_serves),UUID.fromString(main_serves),UUID.fromString(tue_serves),UUID.fromString(wed_serves),UUID.fromString(thu_serves),UUID.fromString(fri_serves),UUID.fromString(sat_serves),UUID.fromString(sun_serves));
        secondStepResponseCall.enqueue(new Callback<SecondStepResponse>() {
            @Override
            public void onResponse(@NonNull Call<SecondStepResponse> call, @NonNull Response<SecondStepResponse> response) {
               Gson gson= new GsonBuilder().create();
                if (response.code()==200){
                    NavOptions navOptions= new NavOptions.Builder()
                            .setPopUpTo(R.id.secondStepFragment,true)
                            .build();
                    SecondStepResponse secondStepResponse= response.body();
                    sharedPreferences.edit()
                            .putString(Constant.MESS_SERVING_MEAL,main_serves)
                            .putString(Constant.REGISTRATION_STEP,"3")
                            .apply();
                    try {
                        navController.navigate(SecondStepFragmentDirections.actionGlobalThirdStepFragment(),navOptions);
                    }catch (Exception ignored){}

                    secondStepBinding.linearLoader.setVisibility(View.GONE);



                }else{
                    try {
                        DetailResponse detailResponse=gson.fromJson(response.errorBody().charStream(),DetailResponse.class);
                        snackbar = Snackbar.make(view, "" + detailResponse.getDetail(), Snackbar.LENGTH_SHORT);
                        snackbar.setBackgroundTint(ContextCompat.getColor(requireContext(),R.color.red));

                        snackbar.show();

                        secondStepBinding.linearLoader.setVisibility(View.GONE);


                    }catch (Exception e){
                        snackbar= Snackbar.make(view, response.code()+":Something went error ", Snackbar.LENGTH_SHORT);
                        snackbar.setBackgroundTint(ContextCompat.getColor(requireContext(),R.color.red));

                        snackbar.show();

                        secondStepBinding.linearLoader.setVisibility(View.GONE);

                    }


                }

            }

            @Override
            public void onFailure(@NonNull Call<SecondStepResponse> call, @NonNull Throwable t) {
                snackbar=  Snackbar.make(view, "Failure: Check your internet connection/Something went error", Snackbar.LENGTH_SHORT);
                snackbar.setBackgroundTint(ContextCompat.getColor(requireContext(),R.color.red));

                snackbar.show();
                secondStepBinding.linearLoader.setVisibility(View.GONE);


            }
        });


    }

    private void initView(View view) {
        meswayServingDayResponseList = new ArrayList<>();
        navController = Navigation.findNavController(view);
        Retrofit retrofit = ApiClient.getClient();
        apiInterface = retrofit.create(ApiInterface.class);
        sharedPreferences = EncryptedSharedPreferencesInstance.getSharedPreferences(Constant.MY_GLOBAL_PREFERENCES, requireActivity());
    }

    private void enableAllBreakfast() {
        secondStepBinding.monBreakfastCheckbox.setChecked(true);
        secondStepBinding.tueBreakfastCheckbox.setChecked(true);
        secondStepBinding.wedBreakfastCheckbox.setChecked(true);
        secondStepBinding.wedBreakfastCheckbox.setChecked(true);
        secondStepBinding.thuBreakfastCheckbox.setChecked(true);
        secondStepBinding.satBreakfastCheckbox.setChecked(true);
        secondStepBinding.friBreakfastCheckbox.setChecked(true);
        secondStepBinding.sunBreakfastCheckbox.setChecked(true);


    }

    private void disableAllBreakFast() {

        secondStepBinding.monBreakfastCheckbox.setChecked(false);
        secondStepBinding.tueBreakfastCheckbox.setChecked(false);
        secondStepBinding.wedBreakfastCheckbox.setChecked(false);
        secondStepBinding.wedBreakfastCheckbox.setChecked(false);
        secondStepBinding.thuBreakfastCheckbox.setChecked(false);
        secondStepBinding.satBreakfastCheckbox.setChecked(false);
        secondStepBinding.friBreakfastCheckbox.setChecked(false);
        secondStepBinding.sunBreakfastCheckbox.setChecked(false);

    }


    private void enableAllLunch() {
        secondStepBinding.monLunchCheckbox.setChecked(true);
        secondStepBinding.tueLunchCheckbox.setChecked(true);
        secondStepBinding.wedLunchCheckbox.setChecked(true);
        secondStepBinding.wedLunchCheckbox.setChecked(true);
        secondStepBinding.thuLunchCheckbox.setChecked(true);
        secondStepBinding.satLunchCheckbox.setChecked(true);
        secondStepBinding.friLunchCheckbox.setChecked(true);
        secondStepBinding.sunLunchCheckbox.setChecked(true);


    }

    private void disableAllLunch() {

        secondStepBinding.monLunchCheckbox.setChecked(false);
        secondStepBinding.tueLunchCheckbox.setChecked(false);
        secondStepBinding.wedLunchCheckbox.setChecked(false);
        secondStepBinding.wedLunchCheckbox.setChecked(false);
        secondStepBinding.thuLunchCheckbox.setChecked(false);
        secondStepBinding.satLunchCheckbox.setChecked(false);
        secondStepBinding.friLunchCheckbox.setChecked(false);
        secondStepBinding.sunLunchCheckbox.setChecked(false);

    }


    private void enableAllDinner() {
        secondStepBinding.monDinnerCheckbox.setChecked(true);
        secondStepBinding.tueDinnerCheckbox.setChecked(true);
        secondStepBinding.wedDinnerCheckbox.setChecked(true);
        secondStepBinding.wedDinnerCheckbox.setChecked(true);
        secondStepBinding.thuDinnerCheckbox.setChecked(true);
        secondStepBinding.satDinnerCheckbox.setChecked(true);
        secondStepBinding.friDinnerCheckbox.setChecked(true);
        secondStepBinding.sunDinnerCheckbox.setChecked(true);


    }

    private void disableAllDinner() {

        secondStepBinding.monDinnerCheckbox.setChecked(false);
        secondStepBinding.tueDinnerCheckbox.setChecked(false);
        secondStepBinding.wedDinnerCheckbox.setChecked(false);
        secondStepBinding.wedDinnerCheckbox.setChecked(false);
        secondStepBinding.thuDinnerCheckbox.setChecked(false);
        secondStepBinding.satDinnerCheckbox.setChecked(false);
        secondStepBinding.friDinnerCheckbox.setChecked(false);
        secondStepBinding.sunDinnerCheckbox.setChecked(false);

    }

    private void visibleAllDayLinerLayout() {
        secondStepBinding.monLnVisibility.setVisibility(View.VISIBLE);
        secondStepBinding.tueLnVisibility.setVisibility(View.VISIBLE);
        secondStepBinding.wedLnVisibility.setVisibility(View.VISIBLE);
        secondStepBinding.thuLnVisibility.setVisibility(View.VISIBLE);
        secondStepBinding.friLnVisibility.setVisibility(View.VISIBLE);
        secondStepBinding.satLnVisibility.setVisibility(View.VISIBLE);
        secondStepBinding.sunLnVisibility.setVisibility(View.VISIBLE);


    }

    private void goneAllDayLinerLayout() {
        secondStepBinding.monLnVisibility.setVisibility(View.GONE);
        secondStepBinding.tueLnVisibility.setVisibility(View.GONE);
        secondStepBinding.wedLnVisibility.setVisibility(View.GONE);
        secondStepBinding.thuLnVisibility.setVisibility(View.GONE);
        secondStepBinding.friLnVisibility.setVisibility(View.GONE);
        secondStepBinding.satLnVisibility.setVisibility(View.GONE);
        secondStepBinding.sunLnVisibility.setVisibility(View.GONE);


    }

    private void hideKeyboard() {
        View views = requireActivity().getCurrentFocus();
        if (views != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(views.getWindowToken(), 0);

        }

    }

}