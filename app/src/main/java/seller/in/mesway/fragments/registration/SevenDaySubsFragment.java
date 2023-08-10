package seller.in.mesway.fragments.registration;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Objects;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import seller.in.mesway.R;
import seller.in.mesway.client.ApiClient;
import seller.in.mesway.client.ApiInterface;
import seller.in.mesway.databinding.FragmentSevenDaySubsBinding;
import seller.in.mesway.response.DetailResponse;
import seller.in.mesway.response.ThirdStepResponse;
import seller.in.mesway.reusable.Constant;
import seller.in.mesway.reusable.EncryptedSharedPreferencesInstance;
import seller.in.mesway.viewModels.SubsInfoCountViewModel;


public class SevenDaySubsFragment extends Fragment {
    private FragmentSevenDaySubsBinding sevenDaySubsBinding;
    private SubsInfoCountViewModel subsInfoCountViewModel;
    private boolean is_one_req = false, is_two_req = false, is_three_req = false;
    private String subs_id="263a044b-1d69-4587-8ed8-f0f6239e22c6";
    private ApiInterface apiInterface;
    private Snackbar snackbar;
    private boolean is_security_money=false;
    private SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        sevenDaySubsBinding= FragmentSevenDaySubsBinding.inflate(inflater,container,false);

        return sevenDaySubsBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        whichMealReqObserver();

        checkedOperations();
        performAllTextFieldOperations();
        clickHandle(view);

    }
    private void clickHandle(View views) {
        sevenDaySubsBinding.btnAddSubs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fifthStep(views);

            }
        });
    }

    private void fifthStep(View view) {
        hideKeyboard();
        sevenDaySubsBinding.linearLoader.setVisibility(View.VISIBLE);
        String one_meal_price="not_serving", two_meal_price="not_serving", three_meal_price="not_serving";
        int security_deposit=0;

        if (is_one_req){
            one_meal_price= Objects.requireNonNull(sevenDaySubsBinding.etOneMealPerDay.getText()).toString();
        }
        if (is_two_req){
            two_meal_price= Objects.requireNonNull(sevenDaySubsBinding.etTwoMealPerDay.getText()).toString();

        }
        if (is_three_req){
            three_meal_price= Objects.requireNonNull(sevenDaySubsBinding.etThreeMealPerDay.getText()).toString();
        }


        if (is_security_money){
            security_deposit=Integer.parseInt(Objects.requireNonNull(sevenDaySubsBinding.etSecurityAmount.getText()).toString());

        }

        Call<ThirdStepResponse> fifthStepCall= apiInterface.fifth_step(Constant.TOKEN_TYPE_VALUE+sharedPreferences.getString(Constant.ACCESS_TOKEN,null), UUID.fromString(sharedPreferences.getString(Constant.MESS_ID,null)),one_meal_price,two_meal_price,three_meal_price,subs_id,is_security_money,security_deposit,false);
        fifthStepCall.enqueue(new Callback<ThirdStepResponse>() {
            @Override
            public void onResponse(@NonNull Call<ThirdStepResponse> call, @NonNull Response<ThirdStepResponse> response) {
                Gson gson = new GsonBuilder().create();
                if (response.code() == 200) {

                    ThirdStepResponse thirdStepResponse = response.body();
                    snackbar = Snackbar.make(view, "Subscription of 7 days added successfully" , Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(ContextCompat.getColor(requireContext(),R.color.green));
                    snackbar.show();


                    sevenDaySubsBinding.linearLoader.setVisibility(View.GONE);


                }else{
                    try {
                        DetailResponse detailResponse=gson.fromJson(response.errorBody().charStream(),DetailResponse.class);
                        snackbar = Snackbar.make(view, "" + detailResponse.getDetail(), Snackbar.LENGTH_SHORT);
                        snackbar.setBackgroundTint(ContextCompat.getColor(requireContext(),R.color.red));

                        snackbar.show();

                        sevenDaySubsBinding.linearLoader.setVisibility(View.GONE);


                    }catch (Exception e){
                        snackbar= Snackbar.make(view, response.code()+":Something went error ", Snackbar.LENGTH_SHORT);
                        snackbar.setBackgroundTint(ContextCompat.getColor(requireContext(),R.color.red));

                        snackbar.show();

                        sevenDaySubsBinding.linearLoader.setVisibility(View.GONE);

                    }

                }

            }

            @Override
            public void onFailure(@NonNull Call<ThirdStepResponse> call, @NonNull Throwable t) {

                snackbar=  Snackbar.make(view, "Failure: Check your internet connection/Something went error", Snackbar.LENGTH_SHORT);
                snackbar.setBackgroundTint(ContextCompat.getColor(requireContext(),R.color.red));

                snackbar.show();
                sevenDaySubsBinding.linearLoader.setVisibility(View.GONE);

            }
        });

    }


    private void whichMealReqObserver() {
        subsInfoCountViewModel.getIs_one_req().observe(requireActivity(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                is_one_req=aBoolean;


            }
        });

        subsInfoCountViewModel.getIs_two_req().observe(requireActivity(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                is_two_req=aBoolean;


            }
        });

        subsInfoCountViewModel.getIs_three_req().observe(requireActivity(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                is_three_req=aBoolean;


            }
        });


        enableMealsDay();


    }

    private void enableMealsDay() {
        if (is_one_req) {

            sevenDaySubsBinding.etlOneMealPerDay.setEnabled(true);
        }
        if (is_two_req) {

            sevenDaySubsBinding.etlTwoMealPerDay.setEnabled(true);


        }

        if (is_three_req) {
            sevenDaySubsBinding.etlThreeMealPerDay.setEnabled(true);


        }
    }

    private void checkedOperations(){
        sevenDaySubsBinding.checkboxYesSecurity.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    is_security_money=true;
                    sevenDaySubsBinding.etlSecurityAmount.setEnabled(true);
                    sevenDaySubsBinding.checkboxNoSecurity.setChecked(false);


                }else {
                    is_security_money=false;
                    sevenDaySubsBinding.etlSecurityAmount.setEnabled(false);

                }
                enableBtn();
            }
        });

        sevenDaySubsBinding.checkboxNoSecurity.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){

                    sevenDaySubsBinding.etlSecurityAmount.setEnabled(false);
                    sevenDaySubsBinding.checkboxYesSecurity.setChecked(false);


                }else {
                    sevenDaySubsBinding.etlSecurityAmount.setEnabled(true);

                }

                enableBtn();


            }
        });


    }


    private void performAllTextFieldOperations() {
        sevenDaySubsBinding.etOneMealPerDay.addTextChangedListener(new TextWatcher() {
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

        sevenDaySubsBinding.etTwoMealPerDay.addTextChangedListener(new TextWatcher() {
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
        sevenDaySubsBinding.etThreeMealPerDay.addTextChangedListener(new TextWatcher() {
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

        sevenDaySubsBinding.etSecurityAmount.addTextChangedListener(new TextWatcher() {
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
        if ((!TextUtils.isEmpty(sevenDaySubsBinding.etOneMealPerDay.getText())) == is_one_req && (!TextUtils.isEmpty(sevenDaySubsBinding.etTwoMealPerDay.getText())) == is_two_req && (!TextUtils.isEmpty(sevenDaySubsBinding.etThreeMealPerDay.getText())) == is_three_req) {
            if (sevenDaySubsBinding.checkboxYesSecurity.isChecked()) {
                sevenDaySubsBinding.btnAddSubs.setEnabled(!TextUtils.isEmpty(sevenDaySubsBinding.etSecurityAmount.getText()));
            }else {
                sevenDaySubsBinding.btnAddSubs.setEnabled(true);


            }
        } else {

            sevenDaySubsBinding.btnAddSubs.setEnabled(false);


        }
    }


    private void postInfoCount() {
        subsInfoCountViewModel.setSubs_info("Currently we offers four types of subscriptions, You can choose more than one type of subscription");
        subsInfoCountViewModel.setSubs_count("3/4");
    }

    private void initView() {
        Retrofit retrofit = ApiClient.getClient();
        sharedPreferences = EncryptedSharedPreferencesInstance.getSharedPreferences(Constant.MY_GLOBAL_PREFERENCES, requireActivity());
        apiInterface = retrofit.create(ApiInterface.class);

        subsInfoCountViewModel=new ViewModelProvider(requireActivity()).get(SubsInfoCountViewModel.class);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (subsInfoCountViewModel!=null){
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