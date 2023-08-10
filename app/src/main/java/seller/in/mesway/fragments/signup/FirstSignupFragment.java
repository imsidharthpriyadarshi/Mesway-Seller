package seller.in.mesway.fragments.signup;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
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
import seller.in.mesway.databinding.FragmentFirstSignupBinding;
import seller.in.mesway.response.DetailResponse;
import seller.in.mesway.response.FirstTimeSellerResponse;
import seller.in.mesway.reusable.Constant;
import seller.in.mesway.reusable.EncryptedSharedPreferencesInstance;

public class FirstSignupFragment extends Fragment {

    private FragmentFirstSignupBinding firstSignupBinding;
    private SharedPreferences sharedPreferences;
    private NavController navController;
    private ApiInterface apiInterface;
    private Snackbar snackbar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        firstSignupBinding= FragmentFirstSignupBinding.inflate(inflater,container,false);
        return firstSignupBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView(view);
        allFieldValidator();
        clickHandle();
    }

    private void allFieldValidator() {

        firstSignupBinding.etMessName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                firstSignupBinding.btnRegister.setEnabled(textFieldValidate("Mess name", Objects.requireNonNull(firstSignupBinding.etMessName.getText()).toString(), firstSignupBinding.etlMessName) && emailChecker() && textFieldValidate("Owner full name", Objects.requireNonNull(firstSignupBinding.etOwnerFullName.getText()).toString(), firstSignupBinding.etlFullName) && textFieldValidate("This Field", Objects.requireNonNull(firstSignupBinding.etDescription.getText()).toString(), firstSignupBinding.etlDescription) && textFieldValidate("Reference id", Objects.requireNonNull(firstSignupBinding.etRefId.getText()).toString(), firstSignupBinding.etlRefId));

            }
        });

        firstSignupBinding.etEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                firstSignupBinding.btnRegister.setEnabled(textFieldValidate("Mess name", Objects.requireNonNull(firstSignupBinding.etMessName.getText()).toString(), firstSignupBinding.etlMessName) && emailChecker() && textFieldValidate("Owner full name", Objects.requireNonNull(firstSignupBinding.etOwnerFullName.getText()).toString(), firstSignupBinding.etlFullName) && textFieldValidate("This Field", Objects.requireNonNull(firstSignupBinding.etDescription.getText()).toString(), firstSignupBinding.etlDescription) && textFieldValidate("Reference id", Objects.requireNonNull(firstSignupBinding.etRefId.getText()).toString(), firstSignupBinding.etlRefId));

            }
        });

        firstSignupBinding.etOwnerFullName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                firstSignupBinding.btnRegister.setEnabled(textFieldValidate("Mess name", Objects.requireNonNull(firstSignupBinding.etMessName.getText()).toString(), firstSignupBinding.etlMessName) && emailChecker() && textFieldValidate("Owner full name", Objects.requireNonNull(firstSignupBinding.etOwnerFullName.getText()).toString(), firstSignupBinding.etlFullName) && textFieldValidate("This Field", Objects.requireNonNull(firstSignupBinding.etDescription.getText()).toString(), firstSignupBinding.etlDescription) && textFieldValidate("Reference id", Objects.requireNonNull(firstSignupBinding.etRefId.getText()).toString(), firstSignupBinding.etlRefId));

            }
        });

        firstSignupBinding.etDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                firstSignupBinding.btnRegister.setEnabled(textFieldValidate("Mess name", Objects.requireNonNull(firstSignupBinding.etMessName.getText()).toString(), firstSignupBinding.etlMessName) && emailChecker() && textFieldValidate("Owner full name", Objects.requireNonNull(firstSignupBinding.etOwnerFullName.getText()).toString(), firstSignupBinding.etlFullName) && textFieldValidate("This Field", Objects.requireNonNull(firstSignupBinding.etDescription.getText()).toString(), firstSignupBinding.etlDescription) && textFieldValidate("Reference id", Objects.requireNonNull(firstSignupBinding.etRefId.getText()).toString(), firstSignupBinding.etlRefId));

            }
        });

        firstSignupBinding.etRefId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                firstSignupBinding.btnRegister.setEnabled(textFieldValidate("Mess name", Objects.requireNonNull(firstSignupBinding.etMessName.getText()).toString(), firstSignupBinding.etlMessName) && emailChecker() && textFieldValidate("Owner full name", Objects.requireNonNull(firstSignupBinding.etOwnerFullName.getText()).toString(), firstSignupBinding.etlFullName) && textFieldValidate("This Field", Objects.requireNonNull(firstSignupBinding.etDescription.getText()).toString(), firstSignupBinding.etlDescription) && textFieldValidate("Reference id", Objects.requireNonNull(firstSignupBinding.etRefId.getText()).toString(), firstSignupBinding.etlRefId));

            }
        });
    }

    private void clickHandle() {
        firstSignupBinding.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();
                firstSignupBinding.linerLoader.setVisibility(View.VISIBLE);

                Call<FirstTimeSellerResponse> firstTimeSellerResponseCall = apiInterface.first_time_user(Constant.TOKEN_TYPE_VALUE+sharedPreferences.getString(Constant.ACCESS_TOKEN,null), UUID.fromString(sharedPreferences.getString(Constant.MESS_ID,null)), Objects.requireNonNull(firstSignupBinding.etOwnerFullName.getText()).toString(), Objects.requireNonNull(firstSignupBinding.etMessName.getText()).toString(), Objects.requireNonNull(firstSignupBinding.etEmail.getText()).toString(),sharedPreferences.getString(Constant.NOTIFICATION_TOKEN,null), Objects.requireNonNull(firstSignupBinding.etDescription.getText()).toString(), Objects.requireNonNull(firstSignupBinding.etRefId.getText()).toString());
                firstTimeSellerResponseCall.enqueue(new Callback<FirstTimeSellerResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<FirstTimeSellerResponse> call, @NonNull Response<FirstTimeSellerResponse> response) {
                        Gson gson = new GsonBuilder().create();
                        if(response.code()==200){
                            FirstTimeSellerResponse firstTimeSellerResponse= response.body();
                            assert firstTimeSellerResponse != null;
                            NavOptions navOptions= new NavOptions.Builder()
                                    .setPopUpTo(R.id.firstSignupFragment,true)
                                            .build();
                            sharedPreferences.edit()
                                    .putString(Constant.REGISTRATION_STEP, String.valueOf(firstTimeSellerResponse.getMess_reg_steps()))
                                    .putString(Constant.MESS_EMAIL,firstTimeSellerResponse.getMess_email())
                                    .putString(Constant.MESS_FULL_NAME,firstTimeSellerResponse.getMess_name())
                                    .putString(Constant.OWNER_FULL_NAME, firstTimeSellerResponse.getOwner_full_name())
                                    .apply();

                            try {
                                navController.navigate(FirstSignupFragmentDirections.actionGlobalSecondStepFragment(),navOptions);
                            }catch (Exception ignored){}
                            firstSignupBinding.linerLoader.setVisibility(View.GONE);

                        }else {
                            try {

                                assert response.errorBody() != null;
                                DetailResponse detailResponse = gson.fromJson(response.errorBody().charStream(), DetailResponse.class);
                                snackbar=  Snackbar.make(view, response.code()+": " + detailResponse.getDetail(), Snackbar.LENGTH_SHORT);
                                snackbar.setBackgroundTint(ContextCompat.getColor(requireContext(),R.color.red));

                                snackbar.show();
                                firstSignupBinding.linerLoader.setVisibility(View.GONE);



                            } catch (Exception e) {

                                snackbar= Snackbar.make(view,  response.code()+ e.getMessage() , Snackbar.LENGTH_SHORT);
                                snackbar.setBackgroundTint(ContextCompat.getColor(requireContext(),R.color.red));

                                snackbar.show();
                                firstSignupBinding.linerLoader.setVisibility(View.GONE);

                            }

                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<FirstTimeSellerResponse> call, @NonNull Throwable t) {
                        snackbar=  Snackbar.make(view, "Failure: Check your internet connection/Something went wrong" , Snackbar.LENGTH_SHORT);
                        snackbar.setBackgroundTint(ContextCompat.getColor(requireContext(),R.color.red));

                        snackbar.show();
                        firstSignupBinding.linerLoader.setVisibility(View.GONE);

                    }
                });
            }
        });
    }

    private void initView(View view) {
        navController= Navigation.findNavController(view);
        Retrofit retrofit = ApiClient.getClient();
        apiInterface= retrofit.create(ApiInterface.class);
        sharedPreferences= EncryptedSharedPreferencesInstance.getSharedPreferences(Constant.MY_GLOBAL_PREFERENCES,requireActivity());


    }

    private boolean textFieldValidate(String name,String text, TextInputLayout editTextLayout){
        if (!TextUtils.isEmpty(text)){
            editTextLayout.setErrorEnabled(false);

            return  true;

        }
        editTextLayout.setErrorEnabled(true);
        editTextLayout.setError(name+" Required*");
        return  false;

    }

    private boolean emailChecker() {
        if (!TextUtils.isEmpty(firstSignupBinding.etEmail.getText())){

            if(Patterns.EMAIL_ADDRESS.matcher(Objects.requireNonNull(firstSignupBinding.etEmail.getText())).matches()){
                firstSignupBinding.etlEmail.setErrorEnabled(false);
                return true;

            }
            firstSignupBinding.etlEmail.setErrorEnabled(true);
            firstSignupBinding.etlEmail.setError("Not a valid email");
            return false;


        }
        return false;
    }
    private void hideKeyboard() {
        View views = requireActivity().getCurrentFocus();
        if (views != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(views.getWindowToken(), 0);

        }

    }

}