package seller.in.mesway.fragments.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import seller.in.mesway.R;
import seller.in.mesway.adapters.ActiveSubscriptionAdapter;
import seller.in.mesway.adapters.PendingSubscriptionAdapter;
import seller.in.mesway.databinding.FragmentActiveSubscriptionBinding;
import seller.in.mesway.models.ActiveSubscriptionModel;
import seller.in.mesway.models.PendingSubscriptionModel;
import seller.in.mesway.response.todayMeal.TodayMeals;
import seller.in.mesway.reusable.Reusable;
import seller.in.mesway.viewModels.ActiveSubsViewModel;


public class ActiveSubscriptionFragment extends Fragment {
    private FragmentActiveSubscriptionBinding activeSubscriptionBinding;
    private Snackbar snackbar;
    private boolean isConnected;
    private Activity activity;

    private ActiveSubsViewModel activeSubsViewModel;
    private TodayMeals today_Meals;

    private ActiveSubscriptionAdapter activeSubscriptionAdapter;
    private ActiveSubsView iActiveSubsView;
    private AlertDialog alertDialog;




    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        activeSubscriptionBinding=FragmentActiveSubscriptionBinding.inflate(inflater,container,false);
        return activeSubscriptionBinding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        if (isConnected){
            activeSubsStatusObserver(view);
            clickHandel();
            isLoadingObserver();



        }else {

            snackbar = Snackbar.make(view, "No Internet Connection", Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction("Refresh", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    activeSubsViewModel.loadActiveSubs(activity);
                    snackbar.dismiss();
                }
            });
            snackbar.setBackgroundTint(ContextCompat.getColor(activity, R.color.red));
            snackbar.show();
        }

    }

    private void activeSubsStatusObserver(View view) {
        alertDialog.show();
        activeSubsViewModel.status_code.observe((LifecycleOwner) activity, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if (integer==200){
                    getActiveSubsObserver(view);
                }else {
                    getActiveSubDetailObserver(view);
                }
            }
        });

    }

    private void getActiveSubDetailObserver(View view) {
        activeSubsViewModel.detail.observe((LifecycleOwner) activity, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                snackbar=Snackbar.make(view,s,Snackbar.LENGTH_SHORT);
                snackbar.setBackgroundTint(ContextCompat.getColor(activity,R.color.red));
                snackbar.show();
                activeSubscriptionBinding.recActiveSubs.setVisibility(View.GONE);
                activeSubscriptionBinding.lnNoCancel.setVisibility(View.VISIBLE);

                alertDialog.dismiss();
            }
        });
    }

    private void getActiveSubsObserver(View view) {
        activeSubsViewModel.todayMealsMutableLiveData.observe((LifecycleOwner) activity, new Observer<TodayMeals>() {
            @Override
            public void onChanged(TodayMeals todayMeals) {
                today_Meals=todayMeals;

                activeSubRecViewOperation(todayMeals);

            }
        });


    }

    private void activeSubRecViewOperation(TodayMeals todayMeals) {
        iActiveSubsView= new ActiveSubsView() {
            @Override
            public void getActiveSubsView(ImageView img_top, ConstraintLayout cons_more_detail, TextView t_more_detail, ConstraintLayout cons_normal_info, MaterialButton btn_call_user,String address_number,String user_number) {
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




            }
        };



        activeSubscriptionAdapter = new ActiveSubscriptionAdapter(getListActiveModel(todayMeals), activity, iActiveSubsView);
        activeSubscriptionBinding.recActiveSubs.setAdapter(activeSubscriptionAdapter);
        activeSubscriptionBinding.recActiveSubs.setLayoutManager(new LinearLayoutManager(activity));




    }

    private List<ActiveSubscriptionModel> getListActiveModel(TodayMeals todayMeals) {
        List<ActiveSubscriptionModel> activeSubscriptionModelsList = new ArrayList<>();
        int active_subs_count = 0;

        for (int i = 0; i < todayMeals.getTodayMeal().size(); i++) {
            activeSubscriptionModelsList.add(new ActiveSubscriptionModel(todayMeals.getTodayMeal().get(i).getStartingMeal(),todayMeals.getTodayMeal().get(i).getStatus(),todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getFullName(), todayMeals.getTodayMeal().get(i).getStartFrom(), todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getMobileNumber(), todayMeals.getTodayMeal().get(i).getUserInfo().getMobileNumber(), todayMeals.getTodayMeal().get(i).getUserId(), todayMeals.getTodayMeal().get(i).getPaymentValue(), todayMeals.getTodayMeal().get(i).getSecurityMoney() + "", todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getFullAddress() + ", " + todayMeals.getTodayMeal().get(i).getUserSubscriptionLocation().getLandmark(), todayMeals.getTodayMeal().get(i).getSubscriptionId(), todayMeals.getTodayMeal().get(i).getDeliveredMeal() + "", todayMeals.getTodayMeal().get(i).getLeftMeals() + "", todayMeals.getTodayMeal().get(i).getPaymentBy(), todayMeals.getTodayMeal().get(i).getPaymentStatus(), todayMeals.getTodayMeal().get(i).getPlanType(), todayMeals.getTodayMeal().get(i).getPaymentValue(), todayMeals.getTodayMeal().get(i).getPlanPrice(), todayMeals.getTodayMeal().get(i).getAllowedCancelMeal() + ""));

            active_subs_count++;
        }

        if (activeSubscriptionModelsList.size() == 0) {
            activeSubscriptionBinding.recActiveSubs.setVisibility(View.GONE);
            activeSubscriptionBinding.lnNoCancel.setVisibility(View.VISIBLE);

        } else {

            activeSubscriptionBinding.recActiveSubs.setVisibility(View.VISIBLE);
            activeSubscriptionBinding.lnNoCancel.setVisibility(View.GONE);
        }
        activeSubscriptionBinding.txtActiveSubscriptionCount.setText(active_subs_count+"");

        alertDialog.dismiss();
        return activeSubscriptionModelsList;

    }

    private void initView(View view) {
        isConnected= Reusable.CheckInternet(activity);
        activeSubsViewModel=new ViewModelProvider((ViewModelStoreOwner) activity).get(ActiveSubsViewModel.class);
        alertDialog = Reusable.alertDialog(activity);


    }
    private void clickHandel() {
        activeSubscriptionBinding.imgSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activeSubsViewModel.loadActiveSubs(activity);
            }
        });
    }
    private void isLoadingObserver() {
        activeSubsViewModel.isLoading.observe((LifecycleOwner) activity, new Observer<Boolean>() {
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