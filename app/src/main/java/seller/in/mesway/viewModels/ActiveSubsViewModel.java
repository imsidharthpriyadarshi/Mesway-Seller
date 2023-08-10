package seller.in.mesway.viewModels;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import seller.in.mesway.repository.ActiveSubsRepository;
import seller.in.mesway.repository.TodayMealsRepository;
import seller.in.mesway.response.todayMeal.TodayMeals;

public class ActiveSubsViewModel extends AndroidViewModel {
    private static ActiveSubsRepository activeSubsRepository;
    public MutableLiveData<TodayMeals> todayMealsMutableLiveData;
    public  MutableLiveData<Boolean> isLoading;
    public MutableLiveData<Integer> status_code;
    public  MutableLiveData<String> detail;
    public ActiveSubsViewModel(@NonNull Application application) {
        super(application);
        activeSubsRepository= ActiveSubsRepository.getInstance(application.getApplicationContext());
        todayMealsMutableLiveData=activeSubsRepository.todayMealsMutableLiveData;
        isLoading= activeSubsRepository.isLoading;
        detail=activeSubsRepository.detail;
        status_code = activeSubsRepository.status_code;

    }
    public void loadActiveSubs(Context context){
        activeSubsRepository.getTodayMealsInBackgroundThread(context);

    }
}
