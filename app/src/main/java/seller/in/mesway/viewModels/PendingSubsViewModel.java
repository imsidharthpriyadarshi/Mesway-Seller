package seller.in.mesway.viewModels;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import seller.in.mesway.repository.PendingSubsRepository;
import seller.in.mesway.response.todayMeal.TodayMeals;

public class PendingSubsViewModel extends AndroidViewModel {
    private static PendingSubsRepository pendingSubsRepository;
    public MutableLiveData<TodayMeals> todayMealsMutableLiveData;
    public  MutableLiveData<Boolean> isLoading;
    public MutableLiveData<Integer> status_code;
    public  MutableLiveData<String> detail;

    public PendingSubsViewModel(@NonNull Application application) {
        super(application);
        pendingSubsRepository= PendingSubsRepository.getInstance(application.getApplicationContext());
        todayMealsMutableLiveData=pendingSubsRepository.todayMealsMutableLiveData;
        isLoading= pendingSubsRepository.isLoading;
        detail=pendingSubsRepository.detail;
        status_code = pendingSubsRepository.status_code;


    }

    public void loadPendingSubs(Context context){
        pendingSubsRepository.getTodayMealsInBackgroundThread(context);

    }
}
