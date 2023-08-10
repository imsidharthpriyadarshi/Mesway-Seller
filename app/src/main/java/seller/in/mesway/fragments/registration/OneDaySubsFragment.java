package seller.in.mesway.fragments.registration;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;

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
import seller.in.mesway.databinding.FragmentOneDaySubsBinding;
import seller.in.mesway.response.DetailResponse;
import seller.in.mesway.response.ThirdStepResponse;
import seller.in.mesway.reusable.Constant;
import seller.in.mesway.reusable.EncryptedSharedPreferencesInstance;
import seller.in.mesway.reusable.Reusable;
import seller.in.mesway.viewModels.SubsInfoCountViewModel;


public class OneDaySubsFragment extends Fragment {
    private FragmentOneDaySubsBinding oneDaySubsBinding;
    private SubsInfoCountViewModel subsInfoCountViewModel;
    private NavController navController;
    private boolean is_one_req = false, is_two_req = false, is_three_req = false;
    private String subs_id = "842a60a3-d805-43e1-b07a-a58f05a7ae41";

    private ApiInterface apiInterface;
    private Snackbar snackbar;
    private boolean is_security_money = false;
    private SharedPreferences sharedPreferences;
    private androidx.appcompat.app.AlertDialog alertDialog;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        oneDaySubsBinding = FragmentOneDaySubsBinding.inflate(inflater, container, false);
        return oneDaySubsBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        whichMealReqObserver();
        checkedOperations();
        performAllTextFieldOperations();
        clickOperation(view);


    }

    private void whichMealReqObserver() {
        subsInfoCountViewModel.getIs_one_req().observe(requireActivity(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                is_one_req = aBoolean;


            }
        });

        subsInfoCountViewModel.getIs_two_req().observe(requireActivity(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                is_two_req = aBoolean;


            }
        });

        subsInfoCountViewModel.getIs_three_req().observe(requireActivity(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                is_three_req = aBoolean;


            }
        });


        enableMealsDay();


    }

    private void enableMealsDay() {
        if (is_one_req) {

            oneDaySubsBinding.etlOneMealPerDay.setEnabled(true);
        }
        if (is_two_req) {

            oneDaySubsBinding.etlTwoMealPerDay.setEnabled(true);


        }

        if (is_three_req) {
            oneDaySubsBinding.etlThreeMealPerDay.setEnabled(true);


        }
    }

    private void clickOperation(View views) {
        oneDaySubsBinding.btnFinalize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getAllSubsObserver(views);
            }
        });

        oneDaySubsBinding.btnAddSubs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fifthStep(views);

            }
        });
    }

    private void fifthStep(View view) {
        hideKeyboard();
        oneDaySubsBinding.linearLoader.setVisibility(View.VISIBLE);
        String one_meal_price = "not_serving", two_meal_price = "not_serving", three_meal_price = "not_serving";
        int security_deposit = 0;

        if (is_one_req) {
            one_meal_price = Objects.requireNonNull(oneDaySubsBinding.etOneMealPerDay.getText()).toString();
        }
        if (is_two_req) {
            two_meal_price = Objects.requireNonNull(oneDaySubsBinding.etTwoMealPerDay.getText()).toString();

        }
        if (is_three_req) {
            three_meal_price = Objects.requireNonNull(oneDaySubsBinding.etThreeMealPerDay.getText()).toString();
        }


        if (is_security_money) {
            security_deposit = Integer.parseInt(Objects.requireNonNull(oneDaySubsBinding.etSecurityAmount.getText()).toString());

        }

        Call<ThirdStepResponse> fifthStepCall = apiInterface.fifth_step(Constant.TOKEN_TYPE_VALUE + sharedPreferences.getString(Constant.ACCESS_TOKEN, null), UUID.fromString(sharedPreferences.getString(Constant.MESS_ID, null)), one_meal_price, two_meal_price, three_meal_price, subs_id, is_security_money, security_deposit, false);
        fifthStepCall.enqueue(new Callback<ThirdStepResponse>() {
            @Override
            public void onResponse(@NonNull Call<ThirdStepResponse> call, @NonNull Response<ThirdStepResponse> response) {
                Gson gson = new GsonBuilder().create();
                if (response.code() == 200) {

                    ThirdStepResponse thirdStepResponse = response.body();
                    snackbar = Snackbar.make(view, "Subscription of 1 day added successfully", Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.green));
                    snackbar.show();


                    oneDaySubsBinding.linearLoader.setVisibility(View.GONE);


                } else {
                    try {
                        DetailResponse detailResponse = gson.fromJson(response.errorBody().charStream(), DetailResponse.class);
                        snackbar = Snackbar.make(view, "" + detailResponse.getDetail(), Snackbar.LENGTH_SHORT);
                        snackbar.setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.red));

                        snackbar.show();

                        oneDaySubsBinding.linearLoader.setVisibility(View.GONE);


                    } catch (Exception e) {
                        snackbar = Snackbar.make(view, response.code() + ":Something went error ", Snackbar.LENGTH_SHORT);
                        snackbar.setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.red));

                        snackbar.show();

                        oneDaySubsBinding.linearLoader.setVisibility(View.GONE);

                    }

                }

            }

            @Override
            public void onFailure(@NonNull Call<ThirdStepResponse> call, @NonNull Throwable t) {

                snackbar = Snackbar.make(view, "Failure: Check your internet connection/Something went error", Snackbar.LENGTH_SHORT);
                snackbar.setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.red));

                snackbar.show();
                oneDaySubsBinding.linearLoader.setVisibility(View.GONE);

            }
        });

    }

    private void getAllSubsObserver(View views) {
        new AlertDialog.Builder(requireActivity())
                .setTitle("Plz Confirm, Do you want to Finalize Subscription ?")
                .setMessage("After finalizing the subscription you can not add/change any subscription type")
                .setPositiveButton("Ok, Finalize", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finalizeSubs(views);
                        dialogInterface.dismiss();


                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).create().show();

    }

    private void finalizeSubs(View view) {
        alertDialog.show();
        Call<Boolean> fifthFinalizeCall = apiInterface.fifth_step_finalize(Constant.TOKEN_TYPE_VALUE + sharedPreferences.getString(Constant.ACCESS_TOKEN, null), UUID.fromString(sharedPreferences.getString(Constant.MESS_ID, null)), true);
        fifthFinalizeCall.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(@NonNull Call<Boolean> call, @NonNull Response<Boolean> response) {
                Gson gson = new GsonBuilder().create();
                if (response.code() == 200) {
                    NavOptions navOptions = new NavOptions.Builder()
                            .setPopUpTo(R.id.fifthStepFragment, true)
                            .build();
                    sharedPreferences.edit()
                            .putString(Constant.REGISTRATION_STEP, "6")
                            .apply();
                    try {
                        navController.navigate(OneDaySubsFragmentDirections.actionGlobalSixthStepFragment(), navOptions);
                    } catch (Exception ignored) {
                    }
                    alertDialog.dismiss();


                } else {
                    try {
                        assert response.errorBody() != null;
                        DetailResponse detailResponse = gson.fromJson(response.errorBody().charStream(), DetailResponse.class);
                        snackbar = Snackbar.make(view, "" + detailResponse.getDetail(), Snackbar.LENGTH_SHORT);
                        snackbar.setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.red));

                        snackbar.show();

                        alertDialog.dismiss();

                    } catch (Exception e) {
                        snackbar = Snackbar.make(view, response.code() + ":Something went error ", Snackbar.LENGTH_SHORT);
                        snackbar.setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.red));

                        snackbar.show();

                        alertDialog.dismiss();
                    }

                }

            }

            @Override
            public void onFailure(@NonNull Call<Boolean> call, @NonNull Throwable t) {
                snackbar = Snackbar.make(view, "Failure: Check your internet connection/Something went error", Snackbar.LENGTH_SHORT);
                snackbar.setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.red));

                snackbar.show();
                alertDialog.dismiss();
            }
        });
    }

    private void checkedOperations() {
        oneDaySubsBinding.checkboxYesSecurity.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {

                    is_security_money = true;
                    oneDaySubsBinding.etlSecurityAmount.setEnabled(true);
                    oneDaySubsBinding.checkboxNoSecurity.setChecked(false);


                } else {
                    is_security_money = false;
                    oneDaySubsBinding.etlSecurityAmount.setEnabled(false);
                }
                enableBtn();
            }
        });

        oneDaySubsBinding.checkboxNoSecurity.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {

                    oneDaySubsBinding.etlSecurityAmount.setEnabled(false);
                    oneDaySubsBinding.checkboxYesSecurity.setChecked(false);


                } else {
                    oneDaySubsBinding.etlSecurityAmount.setEnabled(true);

                }

                enableBtn();


            }
        });


    }


    private void performAllTextFieldOperations() {
        oneDaySubsBinding.etOneMealPerDay.addTextChangedListener(new TextWatcher() {
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

        oneDaySubsBinding.etTwoMealPerDay.addTextChangedListener(new TextWatcher() {
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
        oneDaySubsBinding.etThreeMealPerDay.addTextChangedListener(new TextWatcher() {
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

        oneDaySubsBinding.etSecurityAmount.addTextChangedListener(new TextWatcher() {
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
        if ((!TextUtils.isEmpty(oneDaySubsBinding.etOneMealPerDay.getText())) == is_one_req && (!TextUtils.isEmpty(oneDaySubsBinding.etTwoMealPerDay.getText())) == is_two_req && (!TextUtils.isEmpty(oneDaySubsBinding.etThreeMealPerDay.getText())) == is_three_req) {
            if (oneDaySubsBinding.checkboxYesSecurity.isChecked()) {
                oneDaySubsBinding.btnAddSubs.setEnabled(!TextUtils.isEmpty(oneDaySubsBinding.etSecurityAmount.getText()));
            } else {
                oneDaySubsBinding.btnAddSubs.setEnabled(true);


            }
        } else {

            oneDaySubsBinding.btnAddSubs.setEnabled(false);


        }
    }


    private void postInfoCount() {
        subsInfoCountViewModel.setSubs_info("If Customer wants to test your meal, Then this is best subscription that you can offer");
        subsInfoCountViewModel.setSubs_count("4/4");
    }

    private void initView(View view) {
        alertDialog = Reusable.alertDialog(requireActivity());
        Retrofit retrofit = ApiClient.getClient();
        sharedPreferences = EncryptedSharedPreferencesInstance.getSharedPreferences(Constant.MY_GLOBAL_PREFERENCES, requireActivity());
        apiInterface = retrofit.create(ApiInterface.class);

        NavHostFragment navHostFragment = (NavHostFragment) requireActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_signup);

        subsInfoCountViewModel = new ViewModelProvider(requireActivity()).get(SubsInfoCountViewModel.class);

        navController = navHostFragment.getNavController();
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