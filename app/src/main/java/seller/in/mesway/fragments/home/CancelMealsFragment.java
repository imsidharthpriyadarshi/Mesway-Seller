package seller.in.mesway.fragments.home;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
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
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import seller.in.mesway.R;
import seller.in.mesway.adapters.CancelMealAdapter;
import seller.in.mesway.databinding.FragmentCanceMealsBinding;
import seller.in.mesway.models.CancelMealsModel;
import seller.in.mesway.response.todayMeal.TodayMeals;
import seller.in.mesway.reusable.Reusable;
import seller.in.mesway.viewModels.CancelMealViewModel;

public class CancelMealsFragment extends Fragment {

    private FragmentCanceMealsBinding cancelMealsBinding;
    private Snackbar snackbar;
    private boolean isConnected;
    private Activity activity;
    private CancelMealViewModel cancelMealViewModel;
    private TodayMeals today_Meals;
    private CancelMealView iCancelMealView;
    private CancelMealAdapter cancelMealAdapter;
    private AlertDialog alertDialog;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        cancelMealsBinding = FragmentCanceMealsBinding.inflate(inflater, container, false);
        return cancelMealsBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        if (isConnected) {
            cancelMealStatusObserver(view);
            isLoadingObserver();
            clickHandel();

        } else {
            snackbar = Snackbar.make(view, "No Internet Connection", Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction("Refresh", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cancelMealViewModel.loadCancelMeal(activity);
                    snackbar.dismiss();
                }
            });
            snackbar.setBackgroundTint(ContextCompat.getColor(activity, R.color.red));
            snackbar.show();


        }
    }

    private void isLoadingObserver() {
        cancelMealViewModel.isLoading.observe((LifecycleOwner) activity, new Observer<Boolean>() {
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

    private void cancelMealStatusObserver(View view) {
        alertDialog.show();

        cancelMealViewModel.status_code.observe((LifecycleOwner) activity, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if (integer == 200) {
                    getCancelMealObserver(view);
                } else {
                    getCancelMealDetailObserver(view);
                }
            }
        });
    }

    private void getCancelMealDetailObserver(View view) {
        cancelMealViewModel.detail.observe((LifecycleOwner) activity, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                snackbar = Snackbar.make(view, s, Snackbar.LENGTH_SHORT);
                snackbar.setBackgroundTint(ContextCompat.getColor(activity, R.color.red));
                snackbar.show();
            }
        });
    }

    private void getCancelMealObserver(View view) {
        cancelMealViewModel.todayMealsMutableLiveData.observe((LifecycleOwner) activity, new Observer<TodayMeals>() {
            @Override
            public void onChanged(TodayMeals todayMeals) {
                today_Meals = todayMeals;

                cancelMealRecView(todayMeals);
            }
        });

    }

    private void cancelMealRecView(TodayMeals todayMeals) {
        iCancelMealView = new CancelMealView() {
            @Override
            public void getCancelMealView(ImageView img_down, ConstraintLayout cons_more_detail, TextView t_more_detail, ConstraintLayout cons_normal_info) {
                final int[] cons_more_detail_count = {0};
                cons_more_detail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (cons_more_detail_count[0] == 0) {
                            cons_normal_info.setVisibility(View.VISIBLE);
                            t_more_detail.setText("Less detail");
                            img_down.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.ic_baseline_arrow_drop_up_24));
                            cons_more_detail_count[0]++;
                        } else {
                            cons_normal_info.setVisibility(View.GONE);
                            t_more_detail.setText("More detail");
                            img_down.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.ic_baseline_arrow_drop_down));

                            cons_more_detail_count[0] = 0;


                        }
                    }
                });


            }
        };


        cancelMealAdapter = new CancelMealAdapter(getCancelMealOperationsList(todayMeals), activity, iCancelMealView);
        cancelMealsBinding.recCancelMeal.setAdapter(cancelMealAdapter);
        cancelMealsBinding.recCancelMeal.setLayoutManager(new LinearLayoutManager(activity));
        cancelMealsBinding.recCancelMeal.setNestedScrollingEnabled(false);


    }

    @SuppressLint("SetTextI18n")
    private List<CancelMealsModel> getCancelMealOperationsList(TodayMeals todayMeals) {
        List<CancelMealsModel> cancelMealsModelsList = new ArrayList<>();
        int cancel_meal_count = 0;
        for (int i = 0; i < todayMeals.getTodayMeal().size(); i++) {
            if (Objects.equals(todayMeals.getTodayMeal().get(i).getBreakfast().toLowerCase(), "cancel")) {
                cancelMealsModelsList.add(new CancelMealsModel("Breakfast", "Cancelled", todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getFullName(), todayMeals.getTodayMeal().get(i).getMessInfo().getMessTime().get(0).getBreakfastTime(), todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getMobileNumber(), todayMeals.getTodayMeal().get(i).getUserInfo().getMobileNumber(), todayMeals.getTodayMeal().get(i).getUserId(), todayMeals.getTodayMeal().get(i).getPaymentValue(), todayMeals.getTodayMeal().get(i).getSecurityMoney() + "", todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getFullAddress() + ", " + todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getLandmark(), todayMeals.getTodayMeal().get(i).getSubscriptionId(), todayMeals.getTodayMeal().get(i).getDeliveredMeal() + "", todayMeals.getTodayMeal().get(i).getLeftMeals() + "", todayMeals.getTodayMeal().get(i).getPaymentBy(), todayMeals.getTodayMeal().get(i).getPaymentStatus(), todayMeals.getTodayMeal().get(i).getPlanType(), todayMeals.getTodayMeal().get(i).getPlanPrice(), todayMeals.getTodayMeal().get(i).getAllowedCancelMeal() + ""));

                cancel_meal_count++;
            }

            if (Objects.equals(todayMeals.getTodayMeal().get(i).getLunch().toLowerCase(), "cancel")) {
                cancelMealsModelsList.add(new CancelMealsModel("Lunch", "Cancelled", todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getFullName(), todayMeals.getTodayMeal().get(i).getMessInfo().getMessTime().get(0).getLunchTime(), todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getMobileNumber(), todayMeals.getTodayMeal().get(i).getUserInfo().getMobileNumber(), todayMeals.getTodayMeal().get(i).getUserId(), todayMeals.getTodayMeal().get(i).getPaymentValue(), todayMeals.getTodayMeal().get(i).getSecurityMoney() + "", todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getFullAddress() + ", " + todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getLandmark(), todayMeals.getTodayMeal().get(i).getSubscriptionId(), todayMeals.getTodayMeal().get(i).getDeliveredMeal() + "", todayMeals.getTodayMeal().get(i).getLeftMeals() + "", todayMeals.getTodayMeal().get(i).getPaymentBy(), todayMeals.getTodayMeal().get(i).getPaymentStatus(), todayMeals.getTodayMeal().get(i).getPlanType(), todayMeals.getTodayMeal().get(i).getPlanPrice(), todayMeals.getTodayMeal().get(i).getAllowedCancelMeal() + ""));
                cancel_meal_count++;

            }
            if (Objects.equals(todayMeals.getTodayMeal().get(i).getDinner().toLowerCase(), "cancel")) {

                cancelMealsModelsList.add(new CancelMealsModel("Dinner", "Cancelled", todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getFullName(), todayMeals.getTodayMeal().get(i).getMessInfo().getMessTime().get(0).getDinnerTime(), todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getMobileNumber(), todayMeals.getTodayMeal().get(i).getUserInfo().getMobileNumber(), todayMeals.getTodayMeal().get(i).getUserId(), todayMeals.getTodayMeal().get(i).getPaymentValue(), todayMeals.getTodayMeal().get(i).getSecurityMoney() + "", todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getFullAddress() + ", " + todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getLandmark(), todayMeals.getTodayMeal().get(i).getSubscriptionId(), todayMeals.getTodayMeal().get(i).getDeliveredMeal() + "", todayMeals.getTodayMeal().get(i).getLeftMeals() + "", todayMeals.getTodayMeal().get(i).getPaymentBy(), todayMeals.getTodayMeal().get(i).getPaymentStatus(), todayMeals.getTodayMeal().get(i).getPlanType(), todayMeals.getTodayMeal().get(i).getPlanPrice(), todayMeals.getTodayMeal().get(i).getAllowedCancelMeal() + ""));
                cancel_meal_count++;
            }
        }

        if (cancelMealsModelsList.size() == 0) {
            cancelMealsBinding.recCancelMeal.setVisibility(View.GONE);
            cancelMealsBinding.lnNoCancel.setVisibility(View.VISIBLE);


        } else {

            cancelMealsBinding.recCancelMeal.setVisibility(View.VISIBLE);
            cancelMealsBinding.lnNoCancel.setVisibility(View.GONE);

        }
        cancelMealsBinding.txtCancelMealCount.setText(cancel_meal_count + "");


        alertDialog.dismiss();

        return cancelMealsModelsList;
    }

    private void initView(View view) {
        alertDialog = Reusable.alertDialog(activity);
        isConnected = Reusable.CheckInternet(activity);
        cancelMealViewModel = new ViewModelProvider((ViewModelStoreOwner) activity).get(CancelMealViewModel.class);

    }

    private void clickHandel() {
        cancelMealsBinding.imgSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelMealViewModel.loadCancelMeal(activity);
            }
        });
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
}