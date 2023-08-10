package seller.in.mesway.fragments.signup;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Objects;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import seller.in.mesway.R;
import seller.in.mesway.activity.App;
import seller.in.mesway.activity.MainActivity;
import seller.in.mesway.activity.SignupActivity;
import seller.in.mesway.client.ApiClient;
import seller.in.mesway.client.ApiInterface;
import seller.in.mesway.databinding.FragmentLoginBinding;
import seller.in.mesway.response.DetailResponse;
import seller.in.mesway.response.LoginSignupResponse;
import seller.in.mesway.reusable.Constant;
import seller.in.mesway.reusable.EncryptedSharedPreferencesInstance;


public class LoginFragment extends Fragment {

    private FragmentLoginBinding loginBinding;

    private TextInputEditText et_mobile_number;
    private TextInputLayout etl_mobile_number;
    private MaterialButton btn_send_otp;
    private NavController navController;
    private ApiInterface apiInterface;
    private SignupActivity signupActivity;
    private SharedPreferences sharedPreferences;
    private ProgressBar progress_bar;
    private NavOptions navOptions;
    private Activity activity;
    private Snackbar snackbar;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        loginBinding = FragmentLoginBinding.inflate(inflater, container, false);
        return loginBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        signupActivity = (SignupActivity) getActivity();

        loginBinding.tTermConditionValue.setPaintFlags(loginBinding.tTermConditionValue.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        initAllView();
        navigate();
        numberOperation(view);

    }

    private void sendOtpClick(View view) {
        progress_bar.setVisibility(View.VISIBLE);
        btn_send_otp.setVisibility(View.INVISIBLE);
        Call<LoginSignupResponse> loginSignupResponseCall = apiInterface.do_sign_in(App.getAPIKey(), Objects.requireNonNull(loginBinding.etNumber.getText()).toString());
        loginSignupResponseCall.enqueue(new Callback<LoginSignupResponse>() {
            @Override
            public void onResponse(@NonNull Call<LoginSignupResponse> call, @NonNull Response<LoginSignupResponse> response) {
                Gson gson = new GsonBuilder().create();
                if (response.code()==200){
                    LoginSignupResponse loginSignupResponse=response.body();
                    if (sharedPreferences.getString(Constant.NOTIFICATION_TOKEN,null)==null){
                        buildNotificationToken();

                    }
                    assert loginSignupResponse != null;
                    sharedPreferences.edit()
                            .putString(Constant.MESS_ID, loginSignupResponse.getMess_id())
                            .putString(Constant.MESS_NUMBER,loginSignupResponse.getMobile_number())
                            .apply();

                    try {
                        navController.navigate(LoginFragmentDirections.actionLoginFragmentToReceiveOtpFragment(Objects.requireNonNull(loginBinding.etNumber.getText()).toString()));
                    } catch (Exception ignored) {
                    }
                    progress_bar.setVisibility(View.GONE);
                    btn_send_otp.setVisibility(View.VISIBLE);


                }else {
                    try {
                        DetailResponse detailResponse=gson.fromJson(response.errorBody().charStream(),DetailResponse.class);
                        snackbar = Snackbar.make(view, "" + detailResponse.getDetail(), Snackbar.LENGTH_SHORT);
                        snackbar.setBackgroundTint(ContextCompat.getColor(requireContext(),R.color.red));

                        snackbar.show();
                        progress_bar.setVisibility(View.GONE);
                        btn_send_otp.setVisibility(View.VISIBLE);


                    }catch (Exception e){
                        snackbar= Snackbar.make(view, response.code()+":Something went error ", Snackbar.LENGTH_SHORT);
                        snackbar.setBackgroundTint(ContextCompat.getColor(requireContext(),R.color.red));

                        snackbar.show();
                        progress_bar.setVisibility(View.GONE);
                        btn_send_otp.setVisibility(View.VISIBLE);

                    }


                }
            }

            @Override
            public void onFailure(Call<LoginSignupResponse> call, Throwable t) {
                snackbar=  Snackbar.make(view, "Failure: Check your internet connection/Something went error", Snackbar.LENGTH_SHORT);
                snackbar.setBackgroundTint(ContextCompat.getColor(requireContext(),R.color.red));

                snackbar.show();
                progress_bar.setVisibility(View.GONE);
                btn_send_otp.setVisibility(View.VISIBLE);
            }
        });

    }

    private void numberOperation(View views) {
        et_mobile_number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (numberInputCheck()) {
                    clickHandel(views);

                }
            }
        });
    }

    private boolean numberInputCheck() {
        if (!TextUtils.isEmpty(et_mobile_number.getText())) {

            if (et_mobile_number.length() == 10) {
                etl_mobile_number.setErrorEnabled(false);
                return true;

            } else {
                etl_mobile_number.setErrorEnabled(true);
                etl_mobile_number.setError("Not Valid number");
                return false;
            }
        }
        return false;


    }

    private void hideKeyboard() {
        View views = activity.getCurrentFocus();
        if (views != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(views.getWindowToken(), 0);

        }

    }

    private void initAllView() {
        sharedPreferences = EncryptedSharedPreferencesInstance.getSharedPreferences(Constant.MY_GLOBAL_PREFERENCES, activity);
        Retrofit retrofit = ApiClient.getClient();
        apiInterface = retrofit.create(ApiInterface.class);
        et_mobile_number = loginBinding.etNumber;
        btn_send_otp = loginBinding.btnSendOtp;
        progress_bar = loginBinding.progressBar;
        etl_mobile_number = loginBinding.etNumberLayout;

        navOptions = new NavOptions.Builder()
                .setPopUpTo(R.id.loginFragment, true)
                .build();
        loginBinding.tTermConditionValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://www.freeprivacypolicy.com/live/dcc163d2-8b6e-4d53-b885-e195c72bc43e"));
                startActivity(intent);
            }
        });
        if (sharedPreferences.getString(Constant.NOTIFICATION_TOKEN, null) == null) {

            buildNotificationToken();

        }
    }

    private void clickHandel(View views) {


        loginBinding.btnSendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                hideKeyboard();
                sendOtpClick(views);
            }
        });
    }

    private void navigate() {

        if (sharedPreferences.getString(Constant.ACCESS_TOKEN, null) != null) {
            if (Integer.parseInt(sharedPreferences.getString(Constant.REGISTRATION_STEP, null)) > 7) {
                if(requireActivity().getIntent() !=null && requireActivity().getIntent().hasExtra("where")){
                    //   reusableViewModel.setNotification_where(requireActivity().getIntent().getExtras().getString("where"));
                    Bundle bundle = new Bundle();
                    bundle.putString("where",requireActivity().getIntent().getExtras().getString("where"));
                    Intent intent = new Intent(requireActivity(), MainActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);


                }else {

                    startActivity(new Intent(requireActivity(), MainActivity.class));
                }

                requireActivity().finish();

            } else if (Integer.parseInt(sharedPreferences.getString(Constant.REGISTRATION_STEP, null)) <= 1) {
                try {

                    navController.navigate(LoginFragmentDirections.actionLoginFragmentToFirstSignupFragment(), navOptions);
                } catch (Exception ignored) {
                }

            } else if (Integer.parseInt(sharedPreferences.getString(Constant.REGISTRATION_STEP, null)) == 2) {

                try {

                    navController.navigate(LoginFragmentDirections.actionLoginFragmentToSecondStepFragment(), navOptions);
                } catch (Exception ignored) {
                }

            } else if (Integer.parseInt(sharedPreferences.getString(Constant.REGISTRATION_STEP, null)) == 3) {

                try {

                    navController.navigate(LoginFragmentDirections.actionLoginFragmentToThirdStepFragment(), navOptions);
                } catch (Exception ignored) {
                }

            } else if (Integer.parseInt(sharedPreferences.getString(Constant.REGISTRATION_STEP, null)) == 4) {

                try {

                    navController.navigate(LoginFragmentDirections.actionLoginFragmentToFourthStepFragment(), navOptions);
                } catch (Exception ignored) {
                }

            } else if (Integer.parseInt(sharedPreferences.getString(Constant.REGISTRATION_STEP, null)) == 5) {

                try {

                    navController.navigate(LoginFragmentDirections.actionLoginFragmentToFifthStepFragment(), navOptions);
                } catch (Exception ignored) {
                }

            } else if (Integer.parseInt(sharedPreferences.getString(Constant.REGISTRATION_STEP, null)) == 6) {

                try {

                    navController.navigate(LoginFragmentDirections.actionLoginFragmentToSixthStepFragment(), navOptions);
                } catch (Exception ignored) {
                }

            } else if (Integer.parseInt(sharedPreferences.getString(Constant.REGISTRATION_STEP, null)) == 7) {

                try {

                    navController.navigate(LoginFragmentDirections.actionLoginFragmentToSeventhStepFragment(), navOptions);
                } catch (Exception ignored) {
                }

            }
        }


    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = context instanceof Activity ? (Activity) context : null;


    }

    private void buildNotificationToken() {
        SharedPreferences sharedPreferences = EncryptedSharedPreferencesInstance.getSharedPreferences(Constant.MY_GLOBAL_PREFERENCES, activity);


        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            String token = "not_generated";
            try {
                token = task.getResult();
            } catch (Exception ignored) {

            }
            if (task.isSuccessful()) {
                sharedPreferences.edit()
                        .putString(Constant.NOTIFICATION_TOKEN, token)
                        .apply();
                Log.e("notification_token", token);

            } else {

                Log.e("notification_token", "error");

            }

        });


    }

    @Override
    public void onPause() {
        super.onPause();
        if (snackbar != null) {
            snackbar.dismiss();

        }
    }
}