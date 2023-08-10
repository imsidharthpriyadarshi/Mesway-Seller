package seller.in.mesway.fragments.home;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.google.android.material.snackbar.Snackbar;

import seller.in.mesway.R;
import seller.in.mesway.databinding.FragmentTotalEarningBinding;
import seller.in.mesway.response.todayMeal.TodayMeals;
import seller.in.mesway.reusable.Reusable;
import seller.in.mesway.viewModels.ActiveExpiredSubsViewModel;
import seller.in.mesway.viewModels.ActiveSubsViewModel;
import seller.in.mesway.viewModels.PendingSubsViewModel;


public class TotalEarningFragment extends Fragment {
    private FragmentTotalEarningBinding totalEarningBinding;

    private ActiveExpiredSubsViewModel activeExpiredSubsViewModel;

    private Activity activity;

    private Snackbar snackbar;

    private boolean isConnected;
    private AlertDialog alertDialog;
    private PendingSubsViewModel pendingSubsViewModel;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        totalEarningBinding = FragmentTotalEarningBinding.inflate(inflater, container, false);
        return totalEarningBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView();
        if (isConnected) {
            isActiveLoadingObserver();
            activeSubsObserver(view);


            pendingSubsStatusObserver(view);


        }else {
            snackbar = Snackbar.make(view, "No Internet Connection", Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction("Refresh", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    activeExpiredSubsViewModel.loadActiveSubs(activity);
                    pendingSubsViewModel.loadPendingSubs(activity);
                    snackbar.dismiss();
                }
            });
            snackbar.setBackgroundTint(ContextCompat.getColor(activity, R.color.red));
            snackbar.show();

        }
    }

    private void isPendingLoadingObserver() {
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

    private void isActiveLoadingObserver() {
        activeExpiredSubsViewModel.isLoading.observe((LifecycleOwner) activity, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    alertDialog.show();
                    isPendingLoadingObserver();


                } else {
                    alertDialog.dismiss();

                }
            }
        });

    }

    private void pendingSubsStatusObserver(View view) {
        pendingSubsViewModel.status_code.observe((LifecycleOwner) activity, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if (integer == 200) {
                    getPendingSubsObserver(view);
                } else {
                   // getPendingSubDetailObserver(view);
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
            }
        });
    }

    private void getPendingSubsObserver(View view) {
        pendingSubsViewModel.todayMealsMutableLiveData.observe((LifecycleOwner) activity, new Observer<TodayMeals>() {
            @Override
            public void onChanged(TodayMeals todayMeals) {
                pendingEarningOperation(todayMeals, view);

            }
        });
    }

    private void pendingEarningOperation(TodayMeals todayMeals, View view) {
int pending_earning=0;
        for (int i=0;i<todayMeals.getTodayMeal().size();i++){
            try {

                pending_earning=pending_earning+Integer.parseInt(todayMeals.getTodayMeal().get(i).getPlanPrice());
            }catch (Exception ignored){}

        }

        totalEarningBinding.tPendingEarningValue.setText("\u20B9 "+pending_earning+"");

    }

    private void activeSubsObserver(View view) {
        activeExpiredSubsViewModel.status_code.observe((LifecycleOwner) activity, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if (integer == 200) {
                    getActiveSubsObserver(view);
                } else {
                    //getActiveSubDetailObserver(view);
                }
            }
        });

    }


    private void getActiveSubDetailObserver(View view) {
        activeExpiredSubsViewModel.detail.observe((LifecycleOwner) activity, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                snackbar = Snackbar.make(view, s, Snackbar.LENGTH_SHORT);
                snackbar.setBackgroundTint(ContextCompat.getColor(activity, R.color.red));
                snackbar.show();

            }
        });
    }

    private void getActiveSubsObserver(View view) {
        activeExpiredSubsViewModel.todayMealsMutableLiveData.observe((LifecycleOwner) activity, new Observer<TodayMeals>() {
            @Override
            public void onChanged(TodayMeals todayMeals) {

                activeEarningOperation(todayMeals);

            }
        });


    }

    ///this is active earning

    private void activeEarningOperation(TodayMeals todayMeals) {
        int total_earning=0;
        int onTheWay=0;
        for (int i=0;i<todayMeals.getTodayMeal().size();i++){
            try {
                if (todayMeals.getTodayMeal().get(i).getPaymentStatus().equalsIgnoreCase("not done")) {
                    onTheWay = onTheWay + Integer.parseInt(todayMeals.getTodayMeal().get(i).getPlanPrice());

                }else {
                    total_earning = total_earning + Integer.parseInt(todayMeals.getTodayMeal().get(i).getPlanPrice());


                }
            }catch (Exception e){}


        }

        totalEarningBinding.tEarningValue.setText("\u20B9 "+total_earning+"");
        totalEarningBinding.tOnWayValue.setText("\u20B9 "+onTheWay+"");


    }




    private void initView() {
        activeExpiredSubsViewModel = new ViewModelProvider((ViewModelStoreOwner) activity).get(ActiveExpiredSubsViewModel.class);
        pendingSubsViewModel = new ViewModelProvider((ViewModelStoreOwner) activity).get(PendingSubsViewModel.class);
        alertDialog = Reusable.alertDialog(activity);
        isConnected=Reusable.CheckInternet(activity);
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