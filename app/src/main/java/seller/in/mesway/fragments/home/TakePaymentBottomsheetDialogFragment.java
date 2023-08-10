package seller.in.mesway.fragments.home;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
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
import seller.in.mesway.databinding.FragmentTakePaymentBottomsheetDialogBinding;
import seller.in.mesway.fragments.registration.ThirdStepFragment;
import seller.in.mesway.response.DetailResponse;
import seller.in.mesway.response.ThirdStepResponse;
import seller.in.mesway.reusable.Constant;
import seller.in.mesway.reusable.EncryptedSharedPreferencesInstance;
import seller.in.mesway.reusable.Reusable;

public class TakePaymentBottomsheetDialogFragment extends BottomSheetDialogFragment {
    private FragmentTakePaymentBottomsheetDialogBinding takePaymentBottomsheetDialogBinding;
    private ApiInterface apiInterface;
    private AlertDialog alertDialog;
    private SharedPreferences sharedPreferences;
    private Activity activity;
   private TakePaymentBottomsheetDialogFragmentArgs args;
   private boolean is_checked=false;


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        setStyle(BottomSheetDialogFragment.STYLE_NO_FRAME,R.style.MyBottomSheetTheme);
        return super.onCreateDialog(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        takePaymentBottomsheetDialogBinding=FragmentTakePaymentBottomsheetDialogBinding.inflate(inflater,container,false);

        Objects.requireNonNull(getDialog()).requestWindowFeature(STYLE_NO_TITLE);
       Objects.requireNonNull(getDialog()).getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        return takePaymentBottomsheetDialogBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        initView(view);
        if(getDialog()!=null) {
            clickHandel(Objects.requireNonNull(getDialog()).getWindow().getDecorView());
            checkOperation();
        }


    }

    private void checkOperation() {
        takePaymentBottomsheetDialogBinding.checkBoxYes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                takePaymentBottomsheetDialogBinding.btnYesIReceived.setEnabled(isChecked);
                if (isChecked){

                    is_checked=true;
                }
            }
        });
    }

    private void clickHandel(View decorView) {
        takePaymentBottomsheetDialogBinding.btnYesIReceived.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                do_payment(decorView);
            }
        });


    }

    private void do_payment(View view) {
        if (is_checked) {
            alertDialog.show();
            Call<ThirdStepResponse> thirdStepResponseCall = apiInterface.get_payment(Constant.TOKEN_TYPE_VALUE + sharedPreferences.getString(Constant.ACCESS_TOKEN, null), UUID.fromString(args.getMessId()), UUID.fromString(args.getSubsId()), is_checked);

            thirdStepResponseCall.enqueue(new Callback<ThirdStepResponse>() {
                @Override
                public void onResponse(Call<ThirdStepResponse> call, Response<ThirdStepResponse> response) {
                    Gson gson = new GsonBuilder().create();

                    if (response.code()==200){

                        alertDialog.dismiss();
                        Toast.makeText(activity, "Subscription fee paid successfully", Toast.LENGTH_SHORT).show();

                        dismiss();

                    }else {
                        try {
                            assert response.errorBody() != null;
                            DetailResponse detailResponse = gson.fromJson(response.errorBody().charStream(), DetailResponse.class);
                            Snackbar.make(view, response.code() + ": " + detailResponse.getDetail(), Snackbar.LENGTH_SHORT)
                                    .setAnchorView(takePaymentBottomsheetDialogBinding.btnYesIReceived)
                                    .setBackgroundTint(ContextCompat.getColor(activity,R.color.red))
                                    .show();
                            alertDialog.dismiss();
                        } catch (Exception e) {

                            Snackbar.make(view, response.code() + ": Something went wrong", Snackbar.LENGTH_SHORT)
                                    .setAnchorView(takePaymentBottomsheetDialogBinding.btnYesIReceived)
                                    .setBackgroundTint(ContextCompat.getColor(activity,R.color.red))
                                    .show();

                            alertDialog.dismiss();


                        }


                    }
                }

                @Override
                public void onFailure(Call<ThirdStepResponse> call, Throwable t) {
                    Snackbar.make(view, "Failure: Check Internet connection/Something went wrong " + t.getMessage(), Snackbar.LENGTH_SHORT)
                            .setAnchorView(takePaymentBottomsheetDialogBinding.btnYesIReceived)
                            .setBackgroundTint(ContextCompat.getColor(activity,R.color.red))
                            .show();

                    alertDialog.dismiss();
                }
            });
        }
    }


    @SuppressLint("SetTextI18n")
    private void initView(View view) {
        Bundle bundle;
        Retrofit retrofit = ApiClient.getClient();
        apiInterface = retrofit.create(ApiInterface.class);
        alertDialog = Reusable.alertDialog(activity);
        bundle = getArguments();
        args= TakePaymentBottomsheetDialogFragmentArgs.fromBundle(bundle);
        sharedPreferences = EncryptedSharedPreferencesInstance.getSharedPreferences(Constant.MY_GLOBAL_PREFERENCES, activity);
        takePaymentBottomsheetDialogBinding.tDetail.setText(args.getDetail());
        takePaymentBottomsheetDialogBinding.tHeading.setText("Take Subscription Fee \u20B9 "+args.getAmount());


    }

    private void enableBtn(){
        if (is_checked){
            takePaymentBottomsheetDialogBinding.btnYesIReceived.setEnabled(true);


        }else{
            takePaymentBottomsheetDialogBinding.btnYesIReceived.setEnabled(false);


        }
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = context instanceof Activity ? (Activity) context : null;


    }

}