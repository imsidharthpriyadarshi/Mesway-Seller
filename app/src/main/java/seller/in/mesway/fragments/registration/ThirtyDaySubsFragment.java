package seller.in.mesway.fragments.registration;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavOptions;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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
import seller.in.mesway.databinding.FragmentThirtyDaysSubBinding;
import seller.in.mesway.response.DetailResponse;
import seller.in.mesway.response.MeswayServingDayResponse;
import seller.in.mesway.response.ThirdStepResponse;
import seller.in.mesway.reusable.Constant;
import seller.in.mesway.reusable.EncryptedSharedPreferencesInstance;
import seller.in.mesway.reusable.Reusable;
import seller.in.mesway.viewModels.SubsInfoCountViewModel;


public class ThirtyDaySubsFragment extends Fragment {
    private FragmentThirtyDaysSubBinding thirtyDaysSubBinding;
    private SubsInfoCountViewModel subsInfoCountViewModel;
    private boolean is_one_req = false, is_two_req = false, is_three_req = false;
    private String subs_id = "fc4542ca-a479-4658-9e8a-649def3e1d57";
    private AlertDialog alertDialog;
    private List<MeswayServingDayResponse> meswayServingDayResponseList;
    private ApiInterface apiInterface;
    private Snackbar snackbar;
    private boolean is_security_money=false;
    private SharedPreferences sharedPreferences;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        thirtyDaysSubBinding = FragmentThirtyDaysSubBinding.inflate(inflater, container, false);
        return thirtyDaysSubBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        getAllTypeServingMeal(view);
        checkedOperations();
        performAllTextFieldOperations();
        clickHandle(view);

    }

    private void clickHandle(View views) {
        thirtyDaysSubBinding.btnAddSubs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fifthStep(views);

            }
        });

    }

    private void fifthStep(View view) {
        hideKeyboard();
        thirtyDaysSubBinding.linearLoader.setVisibility(View.VISIBLE);
        String one_meal_price="not_serving", two_meal_price="not_serving", three_meal_price="not_serving";
        int security_deposit=0;

        if (is_one_req){
            one_meal_price= Objects.requireNonNull(thirtyDaysSubBinding.etOneMealPerDay.getText()).toString();
        }
        if (is_two_req){
            two_meal_price= Objects.requireNonNull(thirtyDaysSubBinding.etTwoMealPerDay.getText()).toString();

        }
        if (is_three_req){
            three_meal_price= Objects.requireNonNull(thirtyDaysSubBinding.etThreeMealPerDay.getText()).toString();
        }


        if (is_security_money){
            security_deposit=Integer.parseInt(Objects.requireNonNull(thirtyDaysSubBinding.etSecurityAmount.getText()).toString());

        }

        Call<ThirdStepResponse> fifthStepCall= apiInterface.fifth_step(Constant.TOKEN_TYPE_VALUE+sharedPreferences.getString(Constant.ACCESS_TOKEN,null), UUID.fromString(sharedPreferences.getString(Constant.MESS_ID,null)),one_meal_price,two_meal_price,three_meal_price,subs_id,is_security_money,security_deposit,false);
        fifthStepCall.enqueue(new Callback<ThirdStepResponse>() {
            @Override
            public void onResponse(@NonNull Call<ThirdStepResponse> call, @NonNull Response<ThirdStepResponse> response) {
                Gson gson = new GsonBuilder().create();
                if (response.code() == 200) {

                    ThirdStepResponse thirdStepResponse = response.body();
                    snackbar = Snackbar.make(view, "Subscription of 30 days added successfully" , Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(ContextCompat.getColor(requireContext(),R.color.green));
                    snackbar.show();


                    thirtyDaysSubBinding.linearLoader.setVisibility(View.GONE);


                }else{
                    try {
                        DetailResponse detailResponse=gson.fromJson(response.errorBody().charStream(),DetailResponse.class);
                        snackbar = Snackbar.make(view, "" + detailResponse.getDetail(), Snackbar.LENGTH_SHORT);
                        snackbar.setBackgroundTint(ContextCompat.getColor(requireContext(),R.color.red));

                        snackbar.show();

                        thirtyDaysSubBinding.linearLoader.setVisibility(View.GONE);


                    }catch (Exception e){
                        snackbar= Snackbar.make(view, response.code()+":Something went error ", Snackbar.LENGTH_SHORT);
                        snackbar.setBackgroundTint(ContextCompat.getColor(requireContext(),R.color.red));

                        snackbar.show();

                        thirtyDaysSubBinding.linearLoader.setVisibility(View.GONE);

                    }

                }

            }

            @Override
            public void onFailure(@NonNull Call<ThirdStepResponse> call, @NonNull Throwable t) {

                snackbar=  Snackbar.make(view, "Failure: Check your internet connection/Something went error", Snackbar.LENGTH_SHORT);
                snackbar.setBackgroundTint(ContextCompat.getColor(requireContext(),R.color.red));

                snackbar.show();
                thirtyDaysSubBinding.linearLoader.setVisibility(View.GONE);

            }
        });

    }

    private void getAllTypeServingMeal(View view) {
        alertDialog.show();
        Call<List<MeswayServingDayResponse>> listCall = apiInterface.get_all_mesway_serving_meal(App.getAPIKey());
        listCall.enqueue(new Callback<List<MeswayServingDayResponse>>() {
            @Override
            public void onResponse(@NonNull Call<List<MeswayServingDayResponse>> call, @NonNull Response<List<MeswayServingDayResponse>> response) {
                Gson gson = new GsonBuilder().create();
                if (response.code() == 200) {
                    meswayServingDayResponseList = response.body();

                    for (int i = 0; i < meswayServingDayResponseList.size(); i++) {
                        if (Objects.equals(meswayServingDayResponseList.get(i).getServing_meals_id(), sharedPreferences.getString(Constant.MESS_SERVING_MEAL, null))) {
                            if (meswayServingDayResponseList.get(i).getMeals_number() == 3) {
                                is_one_req = true;
                                is_two_req = true;
                                is_three_req = true;
                            } else if (meswayServingDayResponseList.get(i).getMeals_number() == 2) {

                                is_one_req = true;
                                is_two_req = true;
                            } else if (meswayServingDayResponseList.get(i).getMeals_number() == 1) {
                                is_one_req = true;


                            }
                        }
                        subsInfoCountViewModel.setIs_one_req(is_one_req);
                        subsInfoCountViewModel.setIs_two_req(is_two_req);
                        subsInfoCountViewModel.setIs_three_req(is_three_req);
                        enableMealsDay();
                        alertDialog.dismiss();
                    }
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

    private void enableMealsDay() {
        if (is_one_req) {

            thirtyDaysSubBinding.etlOneMealPerDay.setEnabled(true);
        }
        if (is_two_req) {

            thirtyDaysSubBinding.etlTwoMealPerDay.setEnabled(true);


        }

        if (is_three_req) {
            thirtyDaysSubBinding.etlThreeMealPerDay.setEnabled(true);


        }
    }


    private void checkedOperations() {
        thirtyDaysSubBinding.checkboxYesSecurity.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {

                    is_security_money=true;
                    thirtyDaysSubBinding.etlSecurityAmount.setEnabled(true);
                    thirtyDaysSubBinding.checkboxNoSecurity.setChecked(false);



                } else {
                    thirtyDaysSubBinding.etlSecurityAmount.setEnabled(false);
                    is_security_money=false;

                }
                enableBtn();
            }
        });

        thirtyDaysSubBinding.checkboxNoSecurity.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {

                    thirtyDaysSubBinding.etlSecurityAmount.setEnabled(false);
                    thirtyDaysSubBinding.checkboxYesSecurity.setChecked(false);


                } else {
                    thirtyDaysSubBinding.etlSecurityAmount.setEnabled(true);

                }

                enableBtn();


            }
        });


    }


    private void performAllTextFieldOperations() {
        thirtyDaysSubBinding.etOneMealPerDay.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                enableBtn();
            }
        });

        thirtyDaysSubBinding.etTwoMealPerDay.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                enableBtn();
            }
        });
        thirtyDaysSubBinding.etThreeMealPerDay.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                enableBtn();
            }
        });

        thirtyDaysSubBinding.etSecurityAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                enableBtn();
            }
        });
    }

    private void enableBtn() {
        if ((!TextUtils.isEmpty(thirtyDaysSubBinding.etOneMealPerDay.getText())) == is_one_req && (!TextUtils.isEmpty(thirtyDaysSubBinding.etTwoMealPerDay.getText())) == is_two_req && (!TextUtils.isEmpty(thirtyDaysSubBinding.etThreeMealPerDay.getText())) == is_three_req) {
            if (thirtyDaysSubBinding.checkboxYesSecurity.isChecked()) {
                thirtyDaysSubBinding.btnAddSubs.setEnabled(!TextUtils.isEmpty(thirtyDaysSubBinding.etSecurityAmount.getText()));
            } else {
                thirtyDaysSubBinding.btnAddSubs.setEnabled(true);


            }
        } else {

            thirtyDaysSubBinding.btnAddSubs.setEnabled(false);


        }
    }


    private void postInfoCount() {
        subsInfoCountViewModel.setSubs_info("Get ready to take meals selling game to next level with our 30-days, 15-days, 7-days and 1 day subscription");
        subsInfoCountViewModel.setSubs_count("1/4");
    }

    private void initView() {
        alertDialog = Reusable.alertDialog(requireActivity());
        Retrofit retrofit = ApiClient.getClient();
        sharedPreferences = EncryptedSharedPreferencesInstance.getSharedPreferences(Constant.MY_GLOBAL_PREFERENCES, requireActivity());
        apiInterface = retrofit.create(ApiInterface.class);
        subsInfoCountViewModel = new ViewModelProvider(requireActivity()).get(SubsInfoCountViewModel.class);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (subsInfoCountViewModel != null) {
            postInfoCount();

        }
    }

    private void hideKeyboard() {
        View views = requireActivity().getCurrentFocus();
        if (views != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(views.getWindowToken(), 0);

        }

    }


}