package seller.in.mesway.repository;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import seller.in.mesway.activity.App;
import seller.in.mesway.client.ApiClient;
import seller.in.mesway.client.ApiInterface;
import seller.in.mesway.response.DetailResponse;
import seller.in.mesway.response.ExtraInfoResponse;
import seller.in.mesway.reusable.Constant;
import seller.in.mesway.reusable.EncryptedSharedPreferencesInstance;

public class OtherInformationRepository {

    private SharedPreferences sharedPreferences;
    private ApiInterface apiInterface;
    private static  OtherInformationRepository otherInformationRepository;
    public MutableLiveData<ExtraInfoResponse> extraInfoResponseMutableLiveData;
    public  MutableLiveData<Boolean> isLoading;
    public MutableLiveData<Integer> status_code;
    public  MutableLiveData<String> detail;
    private Executor executor = Executors.newSingleThreadExecutor();

    public static OtherInformationRepository getInstance(Context context){

        return otherInformationRepository = new OtherInformationRepository(context);

    }

    public OtherInformationRepository(Context context){
        extraInfoResponseMutableLiveData=new MutableLiveData<>();
        isLoading= new MutableLiveData<>();
        status_code = new MutableLiveData<>();
        detail = new MutableLiveData<>();
        getExtraInfoInBackgroundThread(context);



    }

    public void getExtraInfoInBackgroundThread(Context context){
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    getExtraInfo(context);
                }catch (Exception e){

                }
            }
        });

    }

    private void getExtraInfo(Context context){
        isLoading.postValue(true);
        sharedPreferences= EncryptedSharedPreferencesInstance.getSharedPreferences(Constant.MY_GLOBAL_PREFERENCES,context);
        Retrofit retrofit = ApiClient.getClient();
        apiInterface = retrofit.create(ApiInterface.class);

        Call<ExtraInfoResponse> extraInfoResponseCall = apiInterface.get_extra_info(App.getAPIKey());
        extraInfoResponseCall.enqueue(new Callback<ExtraInfoResponse>() {
            @Override
            public void onResponse(@NonNull Call<ExtraInfoResponse> call, @NonNull Response<ExtraInfoResponse> response) {
                Gson gson = new GsonBuilder().create();
                if (response.code()==200){
                    status_code.postValue(response.code());
                    extraInfoResponseMutableLiveData.postValue(response.body());
                    isLoading.postValue(false);


                }else {

                    try {

                        assert response.errorBody() != null;
                        DetailResponse detailResponse = gson.fromJson(response.errorBody().charStream(),DetailResponse.class);
                        detail.postValue(detailResponse.getDetail());
                        isLoading.postValue(false);
                        status_code.postValue(response.code());




                    }catch (Exception exception){
                        detail.postValue("Something went error");
                        isLoading.postValue(false);
                        status_code.postValue(response.code());





                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ExtraInfoResponse> call, @NonNull Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    detail.postValue("Slow internet connection");
                    isLoading.postValue(false);
                    status_code.postValue(421);

                } else if (t instanceof IOException) {
                    detail.postValue("Slow internet connection(time out)");
                    isLoading.postValue(false);
                    status_code.postValue(999);

                } else{
                    detail.postValue(t.getMessage());
                    isLoading.postValue(false);
                    status_code.postValue(500);
                }


            }
        });

    }


}
