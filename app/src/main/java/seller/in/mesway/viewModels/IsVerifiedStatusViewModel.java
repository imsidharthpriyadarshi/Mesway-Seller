package seller.in.mesway.viewModels;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import seller.in.mesway.repository.IsVerifiedStatusRepository;
import seller.in.mesway.response.IsVerifiedResponse;

public class IsVerifiedStatusViewModel extends AndroidViewModel {
    private static IsVerifiedStatusRepository isVerifiedStatusRepository;
    public MutableLiveData<IsVerifiedResponse> isVerifiedResponseMutableLiveData;
    public  MutableLiveData<Boolean> isLoading;
    public MutableLiveData<Integer> status_code;
    public  MutableLiveData<String> detail;

    public IsVerifiedStatusViewModel(@NonNull Application application) {
        super(application);

        isVerifiedStatusRepository= IsVerifiedStatusRepository.getInstance(application.getApplicationContext());
        isVerifiedResponseMutableLiveData=isVerifiedStatusRepository.isVerifiedResponseMutableLiveData;
        isLoading= isVerifiedStatusRepository.isLoading;
        detail=isVerifiedStatusRepository.detail;
        status_code = isVerifiedStatusRepository.status_code;
    }

    public void loadIsVerified(Context context){
        isVerifiedStatusRepository.getIsVerifiedInfoInBackgroundThread(context);


    }}
