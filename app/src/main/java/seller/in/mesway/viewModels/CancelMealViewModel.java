package seller.in.mesway.viewModels;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import seller.in.mesway.repository.CancelMealRepository;
import seller.in.mesway.repository.TodayMealsRepository;
import seller.in.mesway.response.todayMeal.TodayMeals;

public class CancelMealViewModel extends AndroidViewModel {
    private static CancelMealRepository cancelMealRepository;
    public MutableLiveData<TodayMeals> todayMealsMutableLiveData;
    public  MutableLiveData<Boolean> isLoading;
    public MutableLiveData<Integer> status_code;
    public  MutableLiveData<String> detail;


    public CancelMealViewModel(@NonNull Application application) {
        super(application);
        cancelMealRepository= CancelMealRepository.getInstance(application.getApplicationContext());
        todayMealsMutableLiveData=cancelMealRepository.todayMealsMutableLiveData;
        isLoading= cancelMealRepository.isLoading;
        detail=cancelMealRepository.detail;
        status_code = cancelMealRepository.status_code;
    }

    public void loadCancelMeal(Context context){
        cancelMealRepository.getTodayMealsInBackgroundThread(context);

    }
}
