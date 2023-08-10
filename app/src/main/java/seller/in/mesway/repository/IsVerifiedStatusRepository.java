package seller.in.mesway.repository;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.MutableLiveData;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import seller.in.mesway.R;
import seller.in.mesway.client.ApiClient;
import seller.in.mesway.client.ApiInterface;
import seller.in.mesway.response.DetailResponse;
import seller.in.mesway.response.IsVerifiedResponse;
import seller.in.mesway.reusable.Constant;
import seller.in.mesway.reusable.EncryptedSharedPreferencesInstance;

public class IsVerifiedStatusRepository {
    private SharedPreferences sharedPreferences;
    private ApiInterface apiInterface;
    private static  IsVerifiedStatusRepository isVerifiedStatusRepository;
    public MutableLiveData<IsVerifiedResponse> isVerifiedResponseMutableLiveData;
    public  MutableLiveData<Boolean> isLoading;
    public MutableLiveData<Integer> status_code;
    public  MutableLiveData<String> detail;
    private Executor executor = Executors.newSingleThreadExecutor();
    public static IsVerifiedStatusRepository getInstance(Context context){

        return isVerifiedStatusRepository = new IsVerifiedStatusRepository(context);

    }

    public IsVerifiedStatusRepository(Context context){
        isVerifiedResponseMutableLiveData=new MutableLiveData<>();
        isLoading= new MutableLiveData<>();
        status_code = new MutableLiveData<>();
        detail = new MutableLiveData<>();
        getIsVerifiedInfoInBackgroundThread(context);



    }

    public void getIsVerifiedInfoInBackgroundThread(Context context){
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    getIsVerifiedInfo(context);
                }catch (Exception e){

                }
            }
        });

    }


    private void getIsVerifiedInfo(Context context) {
        isLoading.postValue(true);
        sharedPreferences= EncryptedSharedPreferencesInstance.getSharedPreferences(Constant.MY_GLOBAL_PREFERENCES,context);
        Retrofit retrofit = ApiClient.getClient();
        apiInterface= retrofit.create(ApiInterface.class);
        Call<IsVerifiedResponse> isVerifiedResponseCall= apiInterface.is_verified(Constant.TOKEN_TYPE_VALUE+sharedPreferences.getString(Constant.ACCESS_TOKEN,null), UUID.fromString(sharedPreferences.getString(Constant.MESS_ID,null)));
        isVerifiedResponseCall.enqueue(new Callback<IsVerifiedResponse>() {
            @Override
            public void onResponse(@NonNull Call<IsVerifiedResponse> call, @NonNull Response<IsVerifiedResponse> response) {
                Gson gson = new GsonBuilder().create();

                if (response.code()==200){
                    IsVerifiedResponse isVerifiedResponse= response.body();
                    assert isVerifiedResponse != null;
                    status_code.postValue(response.code());
                    isVerifiedResponseMutableLiveData.postValue(isVerifiedResponse);
                    isLoading.postValue(false);

                }else {
                    try {
                        DetailResponse detailResponse=gson.fromJson(response.errorBody().charStream(),DetailResponse.class);
                        detail.postValue(detailResponse.getDetail());
                        isLoading.postValue(false);
                        status_code.postValue(response.code());


                    }catch (Exception e){
                        detail.postValue("Something went error");
                        isLoading.postValue(false);
                        status_code.postValue(response.code());

                    }

                }
            }

            @Override
            public void onFailure(@NonNull Call<IsVerifiedResponse> call, @NonNull Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    detail.postValue("Slow internet connection");
                    status_code.postValue(421);
                    isLoading.postValue(false);


                } else if (t instanceof IOException) {
                    detail.postValue("Slow internet connection(time out)");
                    status_code.postValue(421);
                    isLoading.postValue(false);


                } else{
                    detail.postValue(t.getMessage());
                    isLoading.postValue(false);
                    status_code.postValue(500);
                }

            }
        });



    }


}
