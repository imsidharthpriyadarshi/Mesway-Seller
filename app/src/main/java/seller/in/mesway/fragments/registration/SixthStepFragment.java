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

import java.util.Objects;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import seller.in.mesway.R;
import seller.in.mesway.client.ApiClient;
import seller.in.mesway.client.ApiInterface;
import seller.in.mesway.databinding.FragmentSixthStepBinding;
import seller.in.mesway.response.DetailResponse;
import seller.in.mesway.response.ThirdStepResponse;
import seller.in.mesway.reusable.Constant;
import seller.in.mesway.reusable.EncryptedSharedPreferencesInstance;


public class SixthStepFragment extends Fragment {
    private FragmentSixthStepBinding sixthStepBinding;
    private NavController navController;
    private boolean is_delivery_charge = false;
    private ApiInterface apiInterface;
    private SharedPreferences sharedPreferences;
    private Snackbar snackbar;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        sixthStepBinding = FragmentSixthStepBinding.inflate(inflater, container, false);
        return sixthStepBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        checkedOperations();
        textFieldOperations();
        clickHandle(view);

    }

    private void clickHandle(View views) {
        sixthStepBinding.btn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sixthStep(views);
            }
        });
    }

    private void sixthStep(View view) {
        hideKeyboard();
        sixthStepBinding.linearLoader.setVisibility(View.VISIBLE);
        int delivery_charge=0;
        if (is_delivery_charge){
        delivery_charge=Integer.parseInt(Objects.requireNonNull(sixthStepBinding.etDeliveryChargePerDay.getText()).toString());
        }

        Call<ThirdStepResponse> sixthStepCall= apiInterface.sixth_step(Constant.TOKEN_TYPE_VALUE+sharedPreferences.getString(Constant.ACCESS_TOKEN,null), UUID.fromString(sharedPreferences.getString(Constant.MESS_ID,null)), Objects.requireNonNull(sixthStepBinding.etFullName.getText()).toString(), Objects.requireNonNull(sixthStepBinding.etNumber.getText()).toString(),delivery_charge);
        sixthStepCall.enqueue(new Callback<ThirdStepResponse>() {
            @Override
            public void onResponse(@NonNull Call<ThirdStepResponse> call, @NonNull Response<ThirdStepResponse> response) {
                Gson gson = new GsonBuilder().create();
                if (response.code() == 200) {
                    NavOptions navOptions = new NavOptions.Builder()
                            .setPopUpTo(R.id.sixthStepFragment, true)
                            .build();
                    ThirdStepResponse thirdStepResponse = response.body();
                    sharedPreferences.edit()
                            .putString(Constant.REGISTRATION_STEP, "7")
                            .apply();
                    try {
                        navController.navigate(SixthStepFragmentDirections.actionGlobalSeventhStepFragment(), navOptions);
                    } catch (Exception ignored) {
                    }

                    sixthStepBinding.linearLoader.setVisibility(View.GONE);


                }else{
                    try {
                        assert response.errorBody() != null;
                        DetailResponse detailResponse=gson.fromJson(response.errorBody().charStream(),DetailResponse.class);
                        snackbar = Snackbar.make(view, response.code()+": " + detailResponse.getDetail(), Snackbar.LENGTH_SHORT);
                        snackbar.setBackgroundTint(ContextCompat.getColor(requireContext(),R.color.red));

                        snackbar.show();

                        sixthStepBinding.linearLoader.setVisibility(View.GONE);


                    }catch (Exception e){
                        snackbar= Snackbar.make(view, response.code()+":Something went error ", Snackbar.LENGTH_SHORT);
                        snackbar.setBackgroundTint(ContextCompat.getColor(requireContext(),R.color.red));

                        snackbar.show();

                        sixthStepBinding.linearLoader.setVisibility(View.GONE);

                    }

                }

            }

            @Override
            public void onFailure(@NonNull Call<ThirdStepResponse> call, @NonNull Throwable t) {
                snackbar=  Snackbar.make(view, "Failure: Check your internet connection/Something went error", Snackbar.LENGTH_SHORT);
                snackbar.setBackgroundTint(ContextCompat.getColor(requireContext(),R.color.red));

                snackbar.show();
                sixthStepBinding.linearLoader.setVisibility(View.GONE);
            }
        });




    }

    private void textFieldOperations() {
        sixthStepBinding.etFullName.addTextChangedListener(new TextWatcher() {
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

        sixthStepBinding.etNumber.addTextChangedListener(new TextWatcher() {
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

        sixthStepBinding.etDeliveryChargePerDay.addTextChangedListener(new TextWatcher() {
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

    private void initView(View view) {
        navController = Navigation.findNavController(view);
        sharedPreferences= EncryptedSharedPreferencesInstance.getSharedPreferences(Constant.MY_GLOBAL_PREFERENCES,requireActivity());
        Retrofit retrofit= ApiClient.getClient();
        apiInterface= retrofit.create(ApiInterface.class);


    }

    private void checkedOperations() {
        sixthStepBinding.checkboxYesDelivery.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    is_delivery_charge = true;
                    sixthStepBinding.etlSecurityAmount.setEnabled(true);
                    sixthStepBinding.checkboxNoDelivery.setChecked(false);


                } else {
                    is_delivery_charge = false;
                    sixthStepBinding.etlSecurityAmount.setEnabled(false);


                }
                sixthStepBinding.etDeliveryChargePerDay.setText(null);

                enableBtn();
            }
        });

        sixthStepBinding.checkboxNoDelivery.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {

                    sixthStepBinding.etlSecurityAmount.setEnabled(false);
                    sixthStepBinding.checkboxYesDelivery.setChecked(false);


                } else {
                    sixthStepBinding.etlSecurityAmount.setEnabled(true);


                }
                sixthStepBinding.etDeliveryChargePerDay.setText(null);

                enableBtn();


            }
        });


    }


    private void enableBtn() {
        if (!TextUtils.isEmpty(sixthStepBinding.etFullName.getText()) && !TextUtils.isEmpty(sixthStepBinding.etNumber.getText()) &&numberInputCheck()) {
            if (sixthStepBinding.checkboxYesDelivery.isChecked()) {
                sixthStepBinding.btn6.setEnabled(!TextUtils.isEmpty(sixthStepBinding.etDeliveryChargePerDay.getText()));
            } else {
                sixthStepBinding.btn6.setEnabled(true);


            }
        } else {

            sixthStepBinding.btn6.setEnabled(false);


        }
    }



    private void hideKeyboard() {
        View views = requireActivity().getCurrentFocus();
        if (views != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(views.getWindowToken(), 0);

        }

    }

    private boolean numberInputCheck() {
        if (!TextUtils.isEmpty(sixthStepBinding.etNumber.getText())) {

            if (sixthStepBinding.etNumber.length() == 10) {
                sixthStepBinding.etlDeliveryBoyNumber.setErrorEnabled(false);
                return true;

            } else {
                sixthStepBinding.etlDeliveryBoyNumber.setErrorEnabled(true);
                sixthStepBinding.etlDeliveryBoyNumber.setError("Not Valid number");
                return false;
            }
        }
        return false;


    }
}