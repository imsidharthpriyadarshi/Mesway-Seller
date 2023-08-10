package seller.in.mesway.fragments.home;

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

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
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
import seller.in.mesway.client.ApiClient;
import seller.in.mesway.client.ApiInterface;
import seller.in.mesway.databinding.FragmentRejectSubsBottomSheetBinding;
import seller.in.mesway.response.DetailResponse;
import seller.in.mesway.response.ThirdStepResponse;
import seller.in.mesway.reusable.Constant;
import seller.in.mesway.reusable.EncryptedSharedPreferencesInstance;
import seller.in.mesway.reusable.Reusable;
import seller.in.mesway.viewModels.HaveToloadViewModel;

public class RejectSubsBottomSheetFragment extends BottomSheetDialogFragment {
    private FragmentRejectSubsBottomSheetBinding rejectSubsBottomSheetBinding;
    private ApiInterface apiInterface;
    private AlertDialog alertDialog;
    private SharedPreferences sharedPreferences;
    private Activity activity;

    private ArrayAdapter<String> reason_array_adapter;
    private HaveToloadViewModel haveToloadViewModel;

    private RejectSubsBottomSheetFragmentArgs args;
    private Snackbar snackbar;

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
        rejectSubsBottomSheetBinding=FragmentRejectSubsBottomSheetBinding.inflate(inflater,container,false);
        Objects.requireNonNull(getDialog()).requestWindowFeature(STYLE_NO_TITLE);
        Objects.requireNonNull(getDialog()).getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        return rejectSubsBottomSheetBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        if (getDialog()!=null){
            clickHandel(Objects.requireNonNull(getDialog()).getWindow().getDecorView());

        }
    }


    private void clickHandel(View decorView) {
        rejectSubsBottomSheetBinding.autoCompleteTvSelcetReason.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (rejectSubsBottomSheetBinding.autoCompleteTvSelcetReason.getText().toString().equals("Other")){

                    rejectSubsBottomSheetBinding.etlOtherReason.setVisibility(View.VISIBLE);
                    rejectSubsBottomSheetBinding.btnYesIReceived.setEnabled(false);

                    otherReasonTextWatcher();


                }else {
                    rejectSubsBottomSheetBinding.etlOtherReason.setVisibility(View.GONE);
                    rejectSubsBottomSheetBinding.btnYesIReceived.setEnabled(true);
                }

            }
        });


        rejectSubsBottomSheetBinding.autoCompleteTvSelcetReason.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rejectSubsBottomSheetBinding.autoCompleteTvSelcetReason.setAdapter(reason_array_adapter);
                rejectSubsBottomSheetBinding.autoCompleteTvSelcetReason.showDropDown();

            }
        });
        rejectSubsBottomSheetBinding.autoCompleteTvSelcetReason.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


            }
        });

        rejectSubsBottomSheetBinding.btnYesIReceived.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (args!=null) {
                    rejectSubs(decorView, args.getSubsId());
                }else {

                    Toast.makeText(activity, "Try again: Something went error", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void rejectSubs(View views,String subs_id) {
        String reason="Wrong delivery address";
        if (!rejectSubsBottomSheetBinding.autoCompleteTvSelcetReason.getText().toString().equals("Other")){
            reason=rejectSubsBottomSheetBinding.autoCompleteTvSelcetReason.getText().toString();
        }else if (rejectSubsBottomSheetBinding.autoCompleteTvSelcetReason.getText().toString().equals("Other")){
            reason= Objects.requireNonNull(rejectSubsBottomSheetBinding.etOtherReason.getText()).toString();
        }

        Call<ThirdStepResponse> thirdStepResponseCall= apiInterface.subs_status_change(Constant.TOKEN_TYPE_VALUE+sharedPreferences.getString(Constant.ACCESS_TOKEN,null), UUID.fromString(sharedPreferences.getString(Constant.MESS_ID,null)),UUID.fromString(subs_id),reason,"rejected");
        thirdStepResponseCall.enqueue(new Callback<ThirdStepResponse>() {
            @Override
            public void onResponse(Call<ThirdStepResponse> call, Response<ThirdStepResponse> response) {
                Gson gson = new GsonBuilder().create();
                if (response.code()==200){
                    Toast.makeText(activity, "Successfully rejected subscription", Toast.LENGTH_SHORT).show();
                    alertDialog.dismiss();
                    haveToloadViewModel.setHave_to_load(true);

                    dismiss();

                }else {
                    try {
                        DetailResponse detailResponse = gson.fromJson(response.errorBody().charStream(), DetailResponse.class);
                        snackbar = Snackbar.make(views, "" + detailResponse.getDetail(), Snackbar.LENGTH_SHORT);
                        snackbar.setBackgroundTint(ContextCompat.getColor(activity, R.color.red));
                        snackbar.show();
                        alertDialog.dismiss();



                    } catch (Exception e) {
                        snackbar = Snackbar.make(views, response.code() + e.getMessage()+":Something went wrong ", Snackbar.LENGTH_SHORT);
                        snackbar.setBackgroundTint(ContextCompat.getColor(activity, R.color.red));

                        snackbar.show();
                        alertDialog.dismiss();


                    }


                }
            }

            @Override
            public void onFailure(Call<ThirdStepResponse> call, Throwable t) {
                snackbar = Snackbar.make(views,  "Failure: Check your internet/Something went wrong ", Snackbar.LENGTH_SHORT);
                snackbar.setBackgroundTint(ContextCompat.getColor(activity, R.color.red));

                snackbar.show();
                alertDialog.dismiss();
            }
        });

    }



    private void otherReasonTextWatcher() {
        rejectSubsBottomSheetBinding.etOtherReason.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(rejectSubsBottomSheetBinding.etOtherReason.getText())){
                    rejectSubsBottomSheetBinding.btnYesIReceived.setEnabled(true);
                }else {
                    rejectSubsBottomSheetBinding.btnYesIReceived.setEnabled(false);
                }

            }
        });
    }

    private void initView(View view) {
        Bundle bundle;
        Retrofit retrofit = ApiClient.getClient();
        apiInterface = retrofit.create(ApiInterface.class);
        alertDialog = Reusable.alertDialog(activity);
        bundle = getArguments();
        haveToloadViewModel= new ViewModelProvider((ViewModelStoreOwner) activity).get(HaveToloadViewModel.class);
        args= RejectSubsBottomSheetFragmentArgs.fromBundle(bundle);
        sharedPreferences = EncryptedSharedPreferencesInstance.getSharedPreferences(Constant.MY_GLOBAL_PREFERENCES, activity);
        List<String> reason_list= new ArrayList<>();
        reason_list.add("Wrong Delivery Address");
        reason_list.add("Other");

        reason_array_adapter= new ArrayAdapter<>(activity, android.R.layout.simple_list_item_1,reason_list);



    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = context instanceof Activity ? (Activity) context : null;


    }
}