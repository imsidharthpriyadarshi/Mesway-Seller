package seller.in.mesway.viewModels;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import seller.in.mesway.repository.ActiveExpiredSubsRepository;
import seller.in.mesway.repository.ActiveSubsRepository;
import seller.in.mesway.response.todayMeal.TodayMeals;

public class ActiveExpiredSubsViewModel extends AndroidViewModel {
    private static ActiveExpiredSubsRepository activeExpiredSubsRepository;
    public MutableLiveData<TodayMeals> todayMealsMutableLiveData;
    public  MutableLiveData<Boolean> isLoading;
    public MutableLiveData<Integer> status_code;
    public  MutableLiveData<String> detail;
    public ActiveExpiredSubsViewModel(@NonNull Application application) {
        super(application);
        activeExpiredSubsRepository= ActiveExpiredSubsRepository.getInstance(application.getApplicationContext());
        todayMealsMutableLiveData=activeExpiredSubsRepository.todayMealsMutableLiveData;
        isLoading= activeExpiredSubsRepository.isLoading;
        detail=activeExpiredSubsRepository.detail;
        status_code = activeExpiredSubsRepository.status_code;

    }
    public void loadActiveSubs(Context context){
        activeExpiredSubsRepository.getTotalEarningBackgroundThread(context);

    }
}
