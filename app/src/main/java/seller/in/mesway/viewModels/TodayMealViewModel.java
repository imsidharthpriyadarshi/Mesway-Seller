package seller.in.mesway.viewModels;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import seller.in.mesway.repository.TodayMealsRepository;
import seller.in.mesway.response.todayMeal.TodayMeals;

public class TodayMealViewModel extends AndroidViewModel {
    private static TodayMealsRepository todayMealsRepository;
    public MutableLiveData<TodayMeals> todayMealsMutableLiveData;
    public  MutableLiveData<Boolean> isLoading;
    public MutableLiveData<Integer> status_code;
    public  MutableLiveData<String> detail;



    public TodayMealViewModel(@NonNull Application application) {
        super(application);

        todayMealsRepository= TodayMealsRepository.getInstance(application.getApplicationContext());
        todayMealsMutableLiveData=todayMealsRepository.todayMealsMutableLiveData;
        isLoading= todayMealsRepository.isLoading;
        detail=todayMealsRepository.detail;
        status_code = todayMealsRepository.status_code;
    }
    public void loadTodayMeals(Context context){
        todayMealsRepository.getTodayMealsInBackgroundThread(context);

    }
}
