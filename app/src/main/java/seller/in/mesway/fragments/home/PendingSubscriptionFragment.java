package seller.in.mesway.fragments.home;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import seller.in.mesway.R;
import seller.in.mesway.adapters.PendingSubscriptionAdapter;
import seller.in.mesway.client.ApiClient;
import seller.in.mesway.client.ApiInterface;
import seller.in.mesway.databinding.FragmentPendingSubscriptionBinding;
import seller.in.mesway.models.PendingSubscriptionModel;
import seller.in.mesway.response.DetailResponse;
import seller.in.mesway.response.ThirdStepResponse;
import seller.in.mesway.response.todayMeal.TodayMeals;
import seller.in.mesway.reusable.Constant;
import seller.in.mesway.reusable.EncryptedSharedPreferencesInstance;
import seller.in.mesway.reusable.Reusable;
import seller.in.mesway.viewModels.HaveToloadViewModel;
import seller.in.mesway.viewModels.PendingSubsViewModel;


public class PendingSubscriptionFragment extends Fragment {

    private FragmentPendingSubscriptionBinding pendingSubscriptionBinding;
    private Snackbar snackbar;
    private boolean isConnected;
    private Activity activity;
    private PendingSubscriptionAdapter pendingSubscriptionAdapter;
    private PendingSubsView iPendingSubsView;
    private ApiInterface apiInterface;
    private SharedPreferences sharedPreferences;
    private NavController navController;

    private HaveToloadViewModel haveToloadViewModel;


    private PendingSubsViewModel pendingSubsViewModel;
    private TodayMeals today_Meals;
    private AlertDialog alertDialog;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        pendingSubscriptionBinding = FragmentPendingSubscriptionBinding.inflate(inflater, container, false);
        return pendingSubscriptionBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        haveToloadViewModel.isHave_to_load().observe((LifecycleOwner) activity, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {

                    pendingSubsViewModel.loadPendingSubs(activity);
                }
            }
        });

        if (isConnected) {
            pendingSubsStatusObserver(view);
            isLoadingObserver();
            clickHandel();


        } else {
            snackbar = Snackbar.make(view, "No Internet Connection", Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction("Refresh", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    pendingSubsViewModel.loadPendingSubs(activity);
                    snackbar.dismiss();
                }
            });
            snackbar.setBackgroundTint(ContextCompat.getColor(activity, R.color.red));
            snackbar.show();

        }

    }

    private void isLoadingObserver() {
        pendingSubsViewModel.isLoading.observe((LifecycleOwner) activity, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    alertDialog.show();

                } else {
                    alertDialog.dismiss();

                }
            }
        });


    }

    private void clickHandel() {
        pendingSubscriptionBinding.imgSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pendingSubsViewModel.loadPendingSubs(activity);
            }
        });
    }

    private void pendingSubsStatusObserver(View view) {
        alertDialog.show();

        pendingSubsViewModel.status_code.observe((LifecycleOwner) activity, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if (integer == 200) {
                    getPendingSubsObserver(view);
                } else {
                    getPendingSubDetailObserver(view);
                }
            }
        });

    }

    private void getPendingSubDetailObserver(View view) {
        pendingSubsViewModel.detail.observe((LifecycleOwner) activity, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                snackbar = Snackbar.make(view, s, Snackbar.LENGTH_SHORT);
                snackbar.setBackgroundTint(ContextCompat.getColor(activity, R.color.red));
                snackbar.show();
                pendingSubscriptionBinding.recPendingSubs.setVisibility(View.GONE);
                pendingSubscriptionBinding.lnNoCancel.setVisibility(View.VISIBLE);
                alertDialog.dismiss();
            }
        });
    }

    private void getPendingSubsObserver(View view) {
        pendingSubsViewModel.todayMealsMutableLiveData.observe((LifecycleOwner) activity, new Observer<TodayMeals>() {
            @Override
            public void onChanged(TodayMeals todayMeals) {
                today_Meals = todayMeals;
                pendingSubsRecOperation(todayMeals, view);

            }
        });
    }

    private void pendingSubsRecOperation(TodayMeals todayMeals, View views) {
        iPendingSubsView = new PendingSubsView() {
            @Override
            public void getPendingSubsView(ImageView img_top, ConstraintLayout cons_more_detail, TextView t_more_detail, MaterialButton btn_address_approved, MaterialButton btn_rejected, MaterialButton btn_activated, ConstraintLayout cons_normal_info, String subs_id, String user_id, MaterialButton btn_call_user, String address_number, String user_number) {
                final int[] cons_more_detail_count = {0};
                cons_more_detail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (cons_more_detail_count[0] == 0) {
                            cons_normal_info.setVisibility(View.VISIBLE);
                            t_more_detail.setText("Less detail");
                            img_top.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.ic_baseline_arrow_drop_up_24));
                            cons_more_detail_count[0]++;
                        } else {
                            cons_normal_info.setVisibility(View.GONE);
                            t_more_detail.setText("More detail");
                            img_top.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.ic_baseline_arrow_drop_down));

                            cons_more_detail_count[0] = 0;


                        }
                    }
                });

                btn_call_user.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent call = new Intent(Intent.ACTION_DIAL);
                        String number = "tel:" + address_number;
                        call.setData(Uri.parse(number));
                        startActivity(call);
                    }
                });

                btn_call_user.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        Intent call = new Intent(Intent.ACTION_DIAL);
                        String number = "tel:" + user_number;
                        call.setData(Uri.parse(number));
                        startActivity(call);
                        return false;
                    }
                });

                btn_address_approved.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        btn_activated.setEnabled(true);
                        btn_address_approved.setVisibility(View.GONE);
                    }
                });

                btn_activated.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new android.app.AlertDialog.Builder(activity)
                                .setTitle("Do you want activate this Subscription")
                                .setMessage("Make sure address is correct, Once you activated this subscription, You have to serve this subscription from starting date")
                                .setPositiveButton("Activate", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        alertDialog.show();
                                        dialogInterface.dismiss();
                                        activateSubs(views, subs_id);

                                    }
                                })

                                .create().show();

                    }
                });

                btn_rejected.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        navController.navigate(PendingSubscriptionFragmentDirections.actionPendingSubscriptionFragmentToRejectSubsBottomSheetFragment(subs_id));
                    }
                });


            }
        };

        pendingSubscriptionAdapter = new PendingSubscriptionAdapter(getListPendingModel(todayMeals), activity, iPendingSubsView);
        pendingSubscriptionBinding.recPendingSubs.setAdapter(pendingSubscriptionAdapter);
        pendingSubscriptionBinding.recPendingSubs.setLayoutManager(new LinearLayoutManager(activity));


    }

    private void activateSubs(View views, String subs_id) {

        Call<ThirdStepResponse> thirdStepResponseCall = apiInterface.subs_status_change(Constant.TOKEN_TYPE_VALUE + sharedPreferences.getString(Constant.ACCESS_TOKEN, null), UUID.fromString(sharedPreferences.getString(Constant.MESS_ID, null)), UUID.fromString(subs_id), "", "active");
        thirdStepResponseCall.enqueue(new Callback<ThirdStepResponse>() {
            @Override
            public void onResponse(Call<ThirdStepResponse> call, Response<ThirdStepResponse> response) {
                Gson gson = new GsonBuilder().create();
                if (response.code() == 200) {
                    snackbar = Snackbar.make(views, "Subscription Activated Successfully", Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(ContextCompat.getColor(activity, R.color.green));
                    snackbar.show();
                    alertDialog.dismiss();
                    alertDialog.show();
                    pendingSubsViewModel.loadPendingSubs(activity);

                } else {
                    try {
                        DetailResponse detailResponse = gson.fromJson(response.errorBody().charStream(), DetailResponse.class);
                        snackbar = Snackbar.make(views, "" + detailResponse.getDetail(), Snackbar.LENGTH_SHORT);
                        snackbar.setBackgroundTint(ContextCompat.getColor(activity, R.color.red));
                        snackbar.show();
                        alertDialog.dismiss();


                    } catch (Exception e) {
                        snackbar = Snackbar.make(views, response.code() + e.getMessage() + ":Something went wrong ", Snackbar.LENGTH_SHORT);
                        snackbar.setBackgroundTint(ContextCompat.getColor(activity, R.color.red));

                        snackbar.show();
                        alertDialog.dismiss();


                    }


                }
            }

            @Override
            public void onFailure(Call<ThirdStepResponse> call, Throwable t) {
                snackbar = Snackbar.make(views, "Failure: Check your internet/Something went wrong ", Snackbar.LENGTH_SHORT);
                snackbar.setBackgroundTint(ContextCompat.getColor(activity, R.color.red));

                snackbar.show();
                alertDialog.dismiss();
            }
        });

    }

    @SuppressLint("SetTextI18n")
    private List<PendingSubscriptionModel> getListPendingModel(TodayMeals todayMeals) {
        List<PendingSubscriptionModel> subscriptionModelList = new ArrayList<>();
        int pending_subs_count = 0;

        for (int i = 0; i < todayMeals.getTodayMeal().size(); i++) {
            subscriptionModelList.add(new PendingSubscriptionModel(todayMeals.getTodayMeal().get(i).getStartingMeal(),"Pending", todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getFullName(), todayMeals.getTodayMeal().get(i).getStartFrom(), todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getMobileNumber(), todayMeals.getTodayMeal().get(i).getUserInfo().getMobileNumber(), todayMeals.getTodayMeal().get(i).getUserId(), todayMeals.getTodayMeal().get(i).getPaymentValue(), todayMeals.getTodayMeal().get(i).getSecurityMoney() + "", todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getFullAddress() + ", " + todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getLandmark(), todayMeals.getTodayMeal().get(i).getSubscriptionId(), todayMeals.getTodayMeal().get(i).getDeliveredMeal() + "", todayMeals.getTodayMeal().get(i).getLeftMeals() + "", todayMeals.getTodayMeal().get(i).getPaymentBy(), todayMeals.getTodayMeal().get(i).getPaymentStatus(), todayMeals.getTodayMeal().get(i).getPlanType(), todayMeals.getTodayMeal().get(i).getPaymentValue(), todayMeals.getTodayMeal().get(i).getPlanPrice(), todayMeals.getTodayMeal().get(i).getAllowedCancelMeal() + ""));
            pending_subs_count++;
        }

        if (subscriptionModelList.size() == 0) {
            pendingSubscriptionBinding.recPendingSubs.setVisibility(View.GONE);
            pendingSubscriptionBinding.lnNoCancel.setVisibility(View.VISIBLE);

        } else {

            pendingSubscriptionBinding.recPendingSubs.setVisibility(View.VISIBLE);
            pendingSubscriptionBinding.lnNoCancel.setVisibility(View.GONE);
        }
        pendingSubscriptionBinding.txtPendingSubscriptionCount.setText(pending_subs_count + "");

        alertDialog.dismiss();
        return subscriptionModelList;
    }

    private void initView(View view) {
        isConnected = Reusable.CheckInternet(activity);
        pendingSubsViewModel = new ViewModelProvider((ViewModelStoreOwner) activity).get(PendingSubsViewModel.class);
        alertDialog = Reusable.alertDialog(activity);
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        navController = Navigation.findNavController(view);
        haveToloadViewModel = new ViewModelProvider((ViewModelStoreOwner) activity).get(HaveToloadViewModel.class);

        sharedPreferences = EncryptedSharedPreferencesInstance.getSharedPreferences(Constant.MY_GLOBAL_PREFERENCES, activity);

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = context instanceof Activity ? (Activity) context : null;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (snackbar != null) {
            snackbar.dismiss();

        }


    }

    @Override
    public void onStop() {
        super.onStop();

    }


}