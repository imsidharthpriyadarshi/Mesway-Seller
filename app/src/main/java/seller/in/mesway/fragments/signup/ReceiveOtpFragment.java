package seller.in.mesway.fragments.signup;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import seller.in.mesway.R;
import seller.in.mesway.activity.App;
import seller.in.mesway.activity.MainActivity;
import seller.in.mesway.client.ApiClient;
import seller.in.mesway.client.ApiInterface;
import seller.in.mesway.databinding.FragmentReceiveOtpBinding;
import seller.in.mesway.response.DetailResponse;
import seller.in.mesway.response.OtpResponse;
import seller.in.mesway.reusable.Constant;
import seller.in.mesway.reusable.EncryptedSharedPreferencesInstance;
import seller.in.mesway.reusable.Reusable;


public class ReceiveOtpFragment extends Fragment {
    private FragmentReceiveOtpBinding otpBinding;
    private Activity activity;
    private Snackbar snackbar;
    private TextInputEditText et_otp;
    private TextInputLayout etl_otp;
    private MaterialButton btn_verify,btn_resend;
    private NavController navController;
    private SharedPreferences sharedPreferences;
    private ProgressBar progressBar;
    private ApiInterface apiInterface;
    private NavOptions navOptions;



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        otpBinding=FragmentReceiveOtpBinding.inflate(inflater,container,false);
        return otpBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        otpOperation();
        verifyOtpClick(view);


    }

    private void verifyOtpClick(View view) {
        btn_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();
                progressBar.setVisibility(View.VISIBLE);
                btn_verify.setVisibility(View.INVISIBLE);
                btn_resend.setEnabled(false);

                Call<OtpResponse> otpResponseCall = apiInterface.verify_number(App.getAPIKey(),sharedPreferences.getString(Constant.MESS_NUMBER,null), Objects.requireNonNull(et_otp.getText()).toString().trim());
                otpResponseCall.enqueue(new Callback<OtpResponse>() {
                    @Override
                    public void onResponse(Call<OtpResponse> call, Response<OtpResponse> response) {
                        Gson gson = new GsonBuilder().create();
                        Reusable.updateNotificationToken(activity,sharedPreferences.getString(Constant.NOTIFICATION_TOKEN,null));

                        if (response.code()==200){
                            OtpResponse otpResponse = response.body();

                            assert otpResponse != null;

                            sharedPreferences.edit()
                                    .putString(Constant.ACCESS_TOKEN,otpResponse.getAccess_token())
                                    .putString(Constant.REGISTRATION_STEP,String.valueOf(otpResponse.getReg_steps()))
                                    .putString(Constant.MESS_EMAIL,otpResponse.getEmail())
                                    .putString(Constant.MESS_SERVING_MEAL,otpResponse.getMess_serving())
                                    .apply();

                            navigate();
                            progressBar.setVisibility(View.GONE);
                            btn_verify.setVisibility(View.VISIBLE);
                            btn_resend.setEnabled(true);



                        }else {
                            try {

                                assert response.errorBody() != null;
                                DetailResponse errorResponse = gson.fromJson(response.errorBody().charStream(), DetailResponse.class);
                                snackbar=  Snackbar.make(view,""+errorResponse.getDetail(),Snackbar.LENGTH_LONG);
                                snackbar.setBackgroundTint(ContextCompat.getColor(requireContext(),R.color.red));
                                snackbar.show();
                                progressBar.setVisibility(View.GONE);
                                btn_verify.setVisibility(View.VISIBLE);
                                btn_resend.setEnabled(true);

                            }catch (Exception e){
                                snackbar= Snackbar.make(view,response.code()+": "+"Something went wrong",Snackbar.LENGTH_LONG);
                                snackbar.show();
                                progressBar.setVisibility(View.GONE);
                                btn_verify.setVisibility(View.VISIBLE);
                                btn_resend.setEnabled(true);


                            }


                        }

                    }

                    @Override
                    public void onFailure(@NonNull Call<OtpResponse> call, @NonNull Throwable t) {
                        snackbar=     Snackbar.make(view,"Failure: Check internet connection/Something went wrong ",Snackbar.LENGTH_LONG);
                        snackbar.setBackgroundTint(ContextCompat.getColor(requireContext(),R.color.red));
                        snackbar.show();
                        progressBar.setVisibility(View.GONE);
                        btn_verify.setVisibility(View.VISIBLE);
                        btn_resend.setEnabled(true);

                    }
                });

            }
        });

    }

    private void otpOperation() {
        et_otp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                checkOtpInput();

            }
        });
    }



    private void initView(View views) {
        navController= Navigation.findNavController(views);
        et_otp=otpBinding.etOtp;
        etl_otp=otpBinding.etlOtp;
        btn_verify=otpBinding.btnVerify;
        btn_resend=otpBinding.btnResend;
        progressBar=otpBinding.progressBar;
        sharedPreferences= EncryptedSharedPreferencesInstance.getSharedPreferences(Constant.MY_GLOBAL_PREFERENCES,activity);
        ReceiveOtpFragmentArgs args = ReceiveOtpFragmentArgs.fromBundle(getArguments());
        otpBinding.tVerificationNumber.setText("+91"+args.getNumber());
        navOptions = new NavOptions.Builder()
                .setPopUpTo(R.id.loginFragment, true)
                .build();
        Retrofit retrofit= ApiClient.getClient();
        apiInterface= retrofit.create(ApiInterface.class);


    }

    private void hideKeyboard() {
        View views = activity.getCurrentFocus();
        if (views != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(views.getWindowToken(), 0);

        }

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity=context instanceof Activity ?(Activity) context:null;



    }

    private void checkOtpInput(){
        if (!TextUtils.isEmpty(et_otp.getText())){
            if (et_otp.length()==6){
                etl_otp.setErrorEnabled(false);

                btn_verify.setEnabled(true);
            }else {
                etl_otp.setErrorEnabled(true);
                btn_verify.setEnabled(false);
            }

        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (snackbar!=null){
            snackbar.dismiss();

        }
    }

    private void navigate() {

        if (sharedPreferences.getString(Constant.ACCESS_TOKEN, null) != null) {
            if (Integer.parseInt(sharedPreferences.getString(Constant.REGISTRATION_STEP, null)) > 7) {
                startActivity(new Intent(requireActivity(), MainActivity.class));
                requireActivity().finish();

            } else if (Integer.parseInt(sharedPreferences.getString(Constant.REGISTRATION_STEP, null)) <= 1) {
                try {

                    navController.navigate(ReceiveOtpFragmentDirections.actionReceiveOtpFragmentToFirstSignupFragment(), navOptions);
                } catch (Exception ignored) {
                }

            } else if (Integer.parseInt(sharedPreferences.getString(Constant.REGISTRATION_STEP, null)) == 2) {

                try {

                    navController.navigate(ReceiveOtpFragmentDirections.actionReceiveOtpFragmentToSecondStepFragment(), navOptions);
                } catch (Exception ignored) {
                }

            } else if (Integer.parseInt(sharedPreferences.getString(Constant.REGISTRATION_STEP, null)) == 3) {

                try {

                    navController.navigate(ReceiveOtpFragmentDirections.actionReceiveOtpFragmentToThirdStepFragment(), navOptions);
                } catch (Exception ignored) {
                }

            } else if (Integer.parseInt(sharedPreferences.getString(Constant.REGISTRATION_STEP, null)) == 4) {

                try {

                    navController.navigate(ReceiveOtpFragmentDirections.actionReceiveOtpFragmentToFourthStepFragment(), navOptions);
                } catch (Exception ignored) {
                }

            } else if (Integer.parseInt(sharedPreferences.getString(Constant.REGISTRATION_STEP, null)) == 5) {

                try {

                    navController.navigate(ReceiveOtpFragmentDirections.actionReceiveOtpFragmentToFifthStepFragment(), navOptions);
                } catch (Exception ignored) {
                }

            } else if (Integer.parseInt(sharedPreferences.getString(Constant.REGISTRATION_STEP, null)) == 6) {

                try {

                    navController.navigate(ReceiveOtpFragmentDirections.actionReceiveOtpFragmentToSixthStepFragment(), navOptions);
                } catch (Exception ignored) {
                }

            } else if (Integer.parseInt(sharedPreferences.getString(Constant.REGISTRATION_STEP, null)) == 7) {

                try {

                    navController.navigate(ReceiveOtpFragmentDirections.actionReceiveOtpFragmentToSeventhStepFragment(), navOptions);
                } catch (Exception ignored) {
                }

            }
        }


    }


}